/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.order.service.impl;

import me.zhengjie.order.domain.Order;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.seller.domain.SellerInfo;
import me.zhengjie.seller.service.SellerInfoService;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.order.repository.OrderRepository;
import me.zhengjie.order.service.OrderService;
import me.zhengjie.order.service.dto.OrderDto;
import me.zhengjie.order.service.dto.OrderQueryCriteria;
import me.zhengjie.order.service.mapstruct.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import me.zhengjie.utils.PageResult;

/**
* @website https://eladmin.vip
* @description 服务实现
* @author LaoZhao
* @date 2024-10-21
**/
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final SellerInfoService sellerInfoService;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Override
    public PageResult<OrderDto> queryAll(OrderQueryCriteria criteria, Pageable pageable){
        Page<Order> page = orderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(orderMapper::toDto));
    }

    @Override
    public List<OrderDto> queryAll(OrderQueryCriteria criteria){
        return orderMapper.toDto(orderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public OrderDto findById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseGet(Order::new);
        ValidationUtil.isNull(order.getOrderId(),"Order","orderId",orderId);
        return orderMapper.toDto(order);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(Order resources) {
        // 打印接收到的订单请求数据
        logger.info("接收到的订单请求数据: {}", resources);

        // 检查订单号是否重复
        if (orderRepository.findByOrderNumber(resources.getOrderNumber()) != null) {
            throw new EntityExistException(Order.class, "order_number", resources.getOrderNumber());
        }

        // 获取或创建卖家信息，并确保支付方式有默认值
        SellerInfo seller = sellerInfoService.getOrCreateSellerWithInfo(
                resources.getOrderSellerName(),
                resources.getOrderSellerSsn(),
                resources.getOrderContactInfo(),
                ensurePaymentMethod(resources.getOrderPaymentMethod(), true)  // 卖家支付方式
        );
        resources.setOrderSeller(seller);

        // 设置订单的支付方式，确保有默认值
        resources.setOrderPaymentMethod(ensurePaymentMethod(resources.getOrderPaymentMethod(), true));

        // 如果填写了推荐人信息，处理推荐人逻辑并建档
        if (isReferrerInfoProvided(resources)) {
            SellerInfo recommender = sellerInfoService.getOrCreateSellerWithInfo(
                    resources.getOrderReferrerName(),  // 推荐人姓名
                    null,  // 推荐人没有 SSN
                    resources.getOrderReferrerInfo(),  // 推荐人联系方式
                    ensurePaymentMethod(resources.getOrderReferrerMethod(), false)  // 推荐人支付方式
            );
            resources.setOrderReferrer(recommender);
        } else {
            resources.setOrderReferrer(null);  // 如果推荐人信息未填写，则忽略
        }

        // 打印日志
        logger.info("后推荐人信息: {}", resources.getOrderReferrer());
        logger.info("订单支付方式: {}", resources.getOrderPaymentMethod());
        logger.info("完整表单信息: {}", resources);

        // 保存订单
        orderRepository.save(resources);
    }

    /**
     * 确保支付方式有默认值。
     * @param paymentMethod 支付方式
     * @param isSeller 是否为卖家
     * @return 最终保存的支付方式
     */
    private String ensurePaymentMethod(String paymentMethod, boolean isSeller) {
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return "未提供";  // 无论卖家或推荐人都返回默认值 "未提供"
        }
        return paymentMethod;
    }

    /**
     * 判断是否填写了推荐人信息。
     * @param resources 订单资源
     * @return true 如果填写了推荐人姓名和联系方式，否则 false
     */
    private boolean isReferrerInfoProvided(Order resources) {
        return resources.getOrderReferrerName() != null && !resources.getOrderReferrerName().trim().isEmpty()
                && resources.getOrderReferrerInfo() != null && !resources.getOrderReferrerInfo().trim().isEmpty();
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Order resources) {
        Order order = orderRepository.findById(resources.getOrderId()).orElseGet(Order::new);
        ValidationUtil.isNull( order.getOrderId(),"Order","id",resources.getOrderId());
        Order order1 = null;
        order1 = orderRepository.findByOrderNumber(resources.getOrderNumber());
        if(order1 != null && !order1.getOrderId().equals(order.getOrderId())){
            throw new EntityExistException(Order.class,"order_number",resources.getOrderNumber());
        }
        order.copy(resources);
        orderRepository.save(order);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long orderId : ids) {
            orderRepository.deleteById(orderId);
        }
    }

    @Override
    public void download(List<OrderDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OrderDto order : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单编号", order.getOrderNumber());
            map.put("订单状态", order.getOrderStatus());
            map.put("账号名", order.getOrderAccountUsername());
            map.put("密码", order.getOrderAccountPassword());
            map.put("App项目编号", order.getOrderAppId());
            map.put("App名称", order.getOrderAppName());
            map.put("卖家ID", order.getOrderSellerId());
            map.put("卖家名称", order.getOrderSellerName());
            map.put("卖家SSN", order.getOrderSellerSsn());
            map.put("卖家联系方式", order.getOrderContactInfo());
            map.put("支付方式", order.getOrderPaymentMethod());
            map.put("紧急联系方式", order.getOrderContactOther());
            map.put("推荐人ID", order.getOrderReferrerId());
            map.put("推荐人名称", order.getOrderReferrerName());
            map.put("推荐人SSN", order.getOrderReferrerSsn());
            map.put("推荐人联系方式", order.getOrderReferrerInfo());
            map.put("推荐人紧急联系方式", order.getOrderReferrerOther());
            map.put("推荐人支付方式", order.getOrderReferrerMethod());
            map.put("推荐费", order.getOrderReferralFee());
            map.put("订单金额", order.getOrderAmount());
            map.put("下单员工ID", order.getOrderEmployeeId());
            map.put("佣金", order.getOrderCommission());
            map.put("创建时间", order.getOrderCreatedAt());
            map.put("更新时间", order.getOrderUpdatedAt());
            map.put("备注", order.getOrderRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }


}