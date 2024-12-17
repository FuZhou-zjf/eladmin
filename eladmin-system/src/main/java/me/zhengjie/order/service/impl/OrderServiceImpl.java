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

import me.zhengjie.appinfo.domain.AppInfo;
import me.zhengjie.appinfo.service.AppInfoService;
import me.zhengjie.appinfo.service.dto.AppInfoDto;
import me.zhengjie.finance.service.FinanceRecordsService;
import me.zhengjie.order.domain.Order;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.seller.domain.SellerInfo;
import me.zhengjie.seller.service.SellerInfoService;
import me.zhengjie.seller.service.dto.SellerInfoDto;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import me.zhengjie.utils.PageResult;
import java.math.BigDecimal;
import java.math.RoundingMode;
import me.zhengjie.utils.NicknameUtil;
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
    private final AppInfoService appInfoService;
    private final FinanceRecordsService financeRecordsService;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private static final BigDecimal HUNDRED = new BigDecimal("100");


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
        logger.info("接收到的订单请求数据: {}", resources);

        // 1. 校验订单号是否重复
        validateOrderNumber(resources.getOrderNumber());

        // 生成订单号
        if (resources.getOrderNumber() == null || resources.getOrderNumber().isEmpty()) {
            String orderNumber = generateOrderNumber();  // 添加这个方法
            resources.setOrderNumber(orderNumber);
        }

        // 2. 处理卖家信息
        SellerInfo seller = handleSellerInfo(
                resources.getOrderSellerName(),
                resources.getOrderSellerSsn(),
                resources.getOrderContactInfo(),
                resources.getOrderPaymentMethod()
        );
        resources.setOrderSeller(seller);

        // 拼接 nickname：name + phone 后四位
        // 使用新的方法生成卖家的昵称
        String nickname = NicknameUtil.generateNickname(resources.getOrderSellerName(), resources.getOrderContactInfo());
        resources.setOrderSellerNickname(nickname);
        seller.setNickName(nickname);
        sellerInfoService.save(seller);


        // 3. 保存 App 信息并获取其 ID
        Long appAccountId = saveAppInfo(resources);
        resources.setOrderAppId(appAccountId);

        // 4. 处理推荐人信息（如果存在）
        SellerInfo referrer = handleReferrerInfo(resources);
        resources.setOrderReferrer(referrer);


        // 5. 保存订单并打印日志
        orderRepository.save(resources);
        logger.info("订单保存成功: {}", resources);


        // 6. 订单保存成功后，生成财务记录
        financeRecordsService.createFinanceRecordsForOrder(resources);

    }

    // 校验订单号是否重复
    private void validateOrderNumber(String orderNumber) {
        if (orderRepository.findByOrderNumber(orderNumber) != null) {
            throw new EntityExistException(Order.class, "order_number", orderNumber);
        }
    }

    // 处理卖家信息
    private SellerInfo handleSellerInfo(String name, String ssn, String contactInfo, String paymentMethod) {
        return sellerInfoService.getOrCreateSellerWithInfo(
                name,
                ssn,
                contactInfo,
                ensurePaymentMethod(paymentMethod, true)
        );
    }

    // 保存 App 信息并返回其 ID
    private Long saveAppInfo(Order resources) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAccountUsername(resources.getOrderAccountUsername());
        appInfo.setAccountPassword(resources.getOrderAccountPassword());
        appInfo.setAppName(resources.getOrderAppName());
        appInfo.setFullName(resources.getOrderSellerNickname());
        appInfo.setSsn(resources.getOrderSellerSsn());
        appInfo.setAccountStatus(resources.getOrderStatus());
        appInfo.setOrderNumber(resources.getOrderNumber());
        // 保存 App 信息
        appInfoService.create(appInfo);

        // 返回 accountId
        return appInfo.getAccountId();
    }

    // 处理推荐人信息（如果提供）
    private SellerInfo handleReferrerInfo(Order resources) {
        if (isReferrerInfoProvided(resources)) {
            // 处理推荐人信息，拼接昵称
            SellerInfo referrer = sellerInfoService.getOrCreateSellerWithInfo(
                    resources.getOrderReferrerName(),
                    null,  // 假设推荐人没有提供SSN
                    resources.getOrderReferrerInfo(),
                    ensurePaymentMethod(resources.getOrderReferrerMethod(), false)
            );

            // 使用相同的昵称生成方法
            String referrerNickname = NicknameUtil.generateNickname(
                resources.getOrderReferrerName(), 
                resources.getOrderReferrerInfo()
            );
            resources.setOrderReferrerNickname(referrerNickname);
            referrer.setNickName(referrerNickname);
            sellerInfoService.save(referrer);

            return referrer;
        }
        return null;
    }

    // 判断是否填写了推荐人信息
    private boolean isReferrerInfoProvided(Order resources) {
        return resources.getOrderReferrerName() != null && !resources.getOrderReferrerName().trim().isEmpty()
                && resources.getOrderReferrerInfo() != null && !resources.getOrderReferrerInfo().trim().isEmpty();
    }

    // 确保支付方式有默认值
    private String ensurePaymentMethod(String paymentMethod, boolean isSeller) {
        return (paymentMethod == null || paymentMethod.trim().isEmpty()) ? "未提供" : paymentMethod;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Order resources) {
        try {
            // 1. 获取原订单信息
            Order oldOrder = orderRepository.findById(resources.getOrderId())
                .orElseThrow(() -> new RuntimeException("订单不存在"));

          

            // [修改] 处理订单金额
            if (resources.getOrderAmount() != null) {
                // 如果是抽成比例（带小数点）
                if (resources.getOrderAmount().compareTo(BigDecimal.ONE) < 0 && 
                    resources.getOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
                    calculatePercentageAmount(resources);
                }
                // 如果是固定金额，直接更新财务记录
                financeRecordsService.updateFinanceRecordsForOrder(resources);
            }

            // 2. 验证订单号唯一性
            if (!Objects.equals(oldOrder.getOrderNumber(), resources.getOrderNumber())) {
                Order existingOrder = orderRepository.findByOrderNumber(resources.getOrderNumber());
                if (existingOrder != null && !existingOrder.getOrderId().equals(oldOrder.getOrderId())) {
                    throw new EntityExistException(Order.class, "order_number", resources.getOrderNumber());
                }
            }

            // 3. 打印更前的值，用于调试
            logger.info("更新前的订单信息 - 订单号: {}, 卖家名称: {}, 联系方式: {}, 昵称: {}", 
                oldOrder.getOrderNumber(),
                oldOrder.getOrderSellerName(), 
                oldOrder.getOrderContactInfo(),
                oldOrder.getOrderSellerNickname());
            logger.info("新的订单信息 - 订单号: {}, 卖家名称: {}, 联系方式: {}, 昵称: {}", 
                resources.getOrderNumber(),
                resources.getOrderSellerName(), 
                resources.getOrderContactInfo(),
                resources.getOrderSellerNickname());

            // 4. 如果卖家姓名或联系方式变更，必须重新生成昵称
            if (!Objects.equals(oldOrder.getOrderSellerName(), resources.getOrderSellerName()) 
                || !Objects.equals(oldOrder.getOrderContactInfo(), resources.getOrderContactInfo())) {
                
                String newNickname = NicknameUtil.generateNickname(
                    resources.getOrderSellerName(), 
                    resources.getOrderContactInfo()
                );
                logger.info("卖家信息变更，重新生成昵称: {} -> {}", oldOrder.getOrderSellerNickname(), newNickname);
                resources.setOrderSellerNickname(newNickname);
            }

            // 5. 更新订单信息
            oldOrder.copy(resources);
            Order savedOrder = orderRepository.save(oldOrder);
            logger.info("订单基本信息更新成功 - 订单号: {}, 更新后昵称: {}", 
                savedOrder.getOrderNumber(), savedOrder.getOrderSellerNickname());

            // 6. 同步更新卖家信息
            Long sellerId = oldOrder.getOrderSeller() != null ? oldOrder.getOrderSeller().getSellerId() : null;
            logger.info("开始检查卖家信息变更 - 卖家ID: {}", sellerId);
            
            if (sellerId != null) {
                try {
                    SellerInfoDto sellerInfoDto = sellerInfoService.findById(sellerId);
                    if (sellerInfoDto != null) {
                        SellerInfo seller = new SellerInfo();
                        seller.setSellerId(sellerInfoDto.getSellerId());
                        seller.setName(resources.getOrderSellerName());
                        seller.setContactInfo(resources.getOrderContactInfo());
                        seller.setSsn(resources.getOrderSellerSsn());
                        seller.setPaymentMethod(resources.getOrderPaymentMethod());
                        seller.setNickName(resources.getOrderSellerNickname());
                        
                        sellerInfoService.update(seller);
                        logger.info("同步更新卖家信息成功 - 卖家ID: {}, 新昵称: {}", seller.getSellerId(), seller.getNickName());
                    }
                } catch (Exception e) {
                    logger.error("更新卖家信息失败: {}", e.getMessage());
                }
            }

            // 7. 同步更新App信息
            Long appId = oldOrder.getOrderAppId();
            logger.info("开始检查App信息变更 - AppID: {}", appId);
            
            if (appId != null) {
                try {
                    AppInfoDto oldAppInfo = appInfoService.findById(appId);
                    if (oldAppInfo != null) {
                        AppInfo appInfo = new AppInfo();
                        appInfo.setAccountId(appId);
                        // 保留原有的账号密码
                        appInfo.setAccountUsername(oldAppInfo.getAccountUsername());
                        appInfo.setAccountPassword(oldAppInfo.getAccountPassword());
                        // 更新变化的字段
                        appInfo.setAppName(resources.getOrderAppName());
                        appInfo.setFullName(resources.getOrderSellerNickname());
                        appInfo.setSsn(resources.getOrderSellerSsn());
                        appInfo.setAccountStatus(resources.getOrderStatus());
                        appInfo.setOrderNumber(resources.getOrderNumber());
                        appInfoService.update(appInfo);
                        logger.info("同步更新App信息成功 - AppID: {}", appInfo.getAccountId());
                    } else {
                        logger.warn("未找到对应的App信息 - AppID: {}", appId);
                    }
                } catch (Exception e) {
                    logger.error("更新App信息失败: {}", e.getMessage());
                }
            }

            // 8. 检查并更新财务记录
            if (isFinancialDataChanged(oldOrder, resources)) {
                financeRecordsService.updateFinanceRecordsForOrder(oldOrder);
                logger.info("同步更新财务记录成功 - 订单号: {}", oldOrder.getOrderNumber());
            }

        } catch (Exception e) {
            logger.error("更新订单失败: {}", e.getMessage(), e);
            throw new RuntimeException("更新订单失败: " + e.getMessage());
        }
    }

    // 检查卖家信息是否变更
    private boolean isSellerInfoChanged(Order oldOrder, Order newOrder) {
        return !Objects.equals(oldOrder.getOrderSellerName(), newOrder.getOrderSellerName())
                || !Objects.equals(oldOrder.getOrderContactInfo(), newOrder.getOrderContactInfo())
                || !Objects.equals(oldOrder.getOrderSellerSsn(), newOrder.getOrderSellerSsn())
                || !Objects.equals(oldOrder.getOrderPaymentMethod(), newOrder.getOrderPaymentMethod());
    }

    // 检查App信息是否变更
    private boolean isAppInfoChanged(Order oldOrder, Order newOrder) {
        return !Objects.equals(oldOrder.getOrderAppName(), newOrder.getOrderAppName())
                || !Objects.equals(oldOrder.getOrderStatus(), newOrder.getOrderStatus())
                || !Objects.equals(oldOrder.getOrderSellerSsn(), newOrder.getOrderSellerSsn())
                || !Objects.equals(oldOrder.getOrderNumber(), newOrder.getOrderNumber());
    }

    // 检查财务相关数据是否变更
    private boolean isFinancialDataChanged(Order oldOrder, Order newOrder) {
        return !Objects.equals(oldOrder.getOrderAmount(), newOrder.getOrderAmount())
                || !Objects.equals(oldOrder.getOrderCommission(), newOrder.getOrderCommission())
                || !Objects.equals(oldOrder.getOrderReferralFee(), newOrder.getOrderReferralFee())
                || !Objects.equals(oldOrder.getOrderCreatedAt(), newOrder.getOrderCreatedAt())
                || !Objects.equals(oldOrder.getOrderRemark(), newOrder.getOrderRemark())
                || !Objects.equals(oldOrder.getOrderReferrer(), newOrder.getOrderReferrer());
    }

    // 生成订单号的方法
    private String generateOrderNumber() {
        // 格式：年月日时分秒 + 3位随机数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String random = String.format("%03d", new Random().nextInt(1000));
        return timestamp + random;
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
            map.put("备", order.getOrderRemark());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * [修改] 计算抽成金额
     */
    private void calculatePercentageAmount(Order resources) {
        // 直接使用小数作为抽成比例
        BigDecimal percentage = resources.getOrderAmount();
        
        // 获取 App 信息
        AppInfoDto appInfo = appInfoService.findById(resources.getOrderAppId());
        if (appInfo == null) {
            throw new RuntimeException("未找到关联的App信息");
        }
        
        if (appInfo.getSaleFee() == null || appInfo.getSaleFee().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("App售出金额为空或无效，无法计算抽成金额");
        }
        
        // 计算订单总支出
        BigDecimal totalExpenses = BigDecimal.ZERO;
        if (resources.getOrderCommission() != null) {
            totalExpenses = totalExpenses.add(resources.getOrderCommission());
        }
        if (resources.getOrderReferralFee() != null) {
            totalExpenses = totalExpenses.add(resources.getOrderReferralFee());
        }
        
        // 计算净收入和实际金额
        BigDecimal netIncome = appInfo.getSaleFee().subtract(totalExpenses);
        BigDecimal actualAmount = netIncome.multiply(percentage)
            .setScale(2, RoundingMode.HALF_UP);
        
        // 更新订单金额
        resources.setOrderAmount(actualAmount);
        
        StringBuilder remark = new StringBuilder(100)  // 预分配合适的容量
            .append("抽成计算详情: ")
            .append("售出金额$").append(appInfo.getSaleFee())
            .append(" - 总支出$").append(totalExpenses)
            .append(" = 净收入$").append(netIncome)
            .append(" × ").append(percentage.multiply(HUNDRED)).append("%")
            .append(" = $").append(actualAmount);
        
        resources.setOrderRemark(remark.toString());
        
        logger.info("抽成金额计算详情 - 订单号: {}, 售出金额: {}, 总支出: {}, 净收入: {}, 抽成比例: {}%, 计算后金额: {}", 
            resources.getOrderNumber(), 
            appInfo.getSaleFee(),
            totalExpenses,
            netIncome,
            percentage.multiply(new BigDecimal("100")),
            actualAmount);
    }

}