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
package me.zhengjie.seller.service.impl;

import me.zhengjie.order.service.impl.OrderServiceImpl;
import me.zhengjie.seller.domain.SellerInfo;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.seller.repository.SellerInfoRepository;
import me.zhengjie.seller.service.SellerInfoService;
import me.zhengjie.seller.service.dto.SellerInfoDto;
import me.zhengjie.seller.service.dto.SellerInfoQueryCriteria;
import me.zhengjie.seller.service.mapstruct.SellerInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* @website https://eladmin.vip
* @description 服务实现
* @author Laozhao
* @date 2024-10-09
**/
@Service
@RequiredArgsConstructor
public class SellerInfoServiceImpl implements SellerInfoService {

    private final SellerInfoRepository sellerInfoRepository;
    private final SellerInfoMapper sellerInfoMapper;
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);


    @Override
    public PageResult<SellerInfoDto> queryAll(SellerInfoQueryCriteria criteria, Pageable pageable){
        Page<SellerInfo> page = sellerInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(sellerInfoMapper::toDto));
    }

    @Override
    public List<SellerInfoDto> queryAll(SellerInfoQueryCriteria criteria){
        return sellerInfoMapper.toDto(sellerInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public SellerInfoDto findById(Long sellerId) {
        SellerInfo sellerInfo = sellerInfoRepository.findById(sellerId).orElseGet(SellerInfo::new);
        ValidationUtil.isNull(sellerInfo.getSellerId(),"SellerInfo","sellerId",sellerId);
        return sellerInfoMapper.toDto(sellerInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(SellerInfo resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setSellerId(snowflake.nextId()); 
        sellerInfoRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SellerInfo resources) {
        SellerInfo sellerInfo = sellerInfoRepository.findById(resources.getSellerId()).orElseGet(SellerInfo::new);
        ValidationUtil.isNull( sellerInfo.getSellerId(),"SellerInfo","id",resources.getSellerId());
        sellerInfo.copy(resources);
        sellerInfoRepository.save(sellerInfo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long sellerId : ids) {
            sellerInfoRepository.deleteById(sellerId);
        }
    }

    @Override
    public void download(List<SellerInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (SellerInfoDto sellerInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("卖家姓名", sellerInfo.getName());
            map.put("联系方式", sellerInfo.getContactInfo());
            map.put("电子邮件", sellerInfo.getEmail());
            map.put("电话号码", sellerInfo.getPhoneNumber());
            map.put("身份证号码", sellerInfo.getIdentityNumber());
            map.put("社会安全号", sellerInfo.getSsn());
            map.put("身份证正面照片", sellerInfo.getIdFront());
            map.put("身份证背面照片", sellerInfo.getIdBack());
            map.put("手持身份证照片", sellerInfo.getIdHandheld());
            map.put("SSN正面照片", sellerInfo.getSsnFront());
            map.put("SSN背面照片", sellerInfo.getSsnBack());
            map.put("水电账单照片", sellerInfo.getUtilityBill());
            map.put("银行对账单照片", sellerInfo.getBankStatement());
            map.put("卖家视频链接", sellerInfo.getVideoUrl());
            map.put("总收入", sellerInfo.getTotalIncome());
            map.put("创建时间", sellerInfo.getCreatedAt());
            map.put("最后更新时间", sellerInfo.getLastUpdated());
            map.put("备注", sellerInfo.getRemarks());
            map.put("收款方式", sellerInfo.getPaymentMethod());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
    @Override
    public boolean checkSellerExists(String name, String ssn) {
        return sellerInfoRepository.existsByNameAndSsn(name, ssn);
    }

    @Override
    public boolean checkNameExists(String name) {
        return sellerInfoRepository.existsByName(name);
    }




    @Override
    public boolean existsByName(String name) {
        return sellerInfoRepository.existsByName(name);
    }


    @Override
    public boolean existsByNameAndContactInfo(String name, String contactInfo) {
        return sellerInfoRepository.existsByNameAndContactInfo(name, contactInfo);
    }

    // 查询或创建卖家信息
    @Override
    @Transactional(rollbackFor = Exception.class)
    public SellerInfo getOrCreateSellerWithInfo(String name, String ssn, String contactInfo, String orderPaymentMethod) {
        // 如果推荐人姓名和联系信息为空，则直接返回 null
        if ((name == null || name.trim().isEmpty()) &&
                (contactInfo == null || contactInfo.trim().isEmpty())) {
            return null;
        }

        // 如果 SSN 为空，则使用默认值 "N/A"
        if (ssn == null || ssn.trim().isEmpty()) {
            ssn = "N/A";
        }
        // 使用 Criteria 查询卖家信息
        SellerInfoQueryCriteria criteria = new SellerInfoQueryCriteria();
        criteria.setName(name);
        criteria.setSsn(ssn);

        List<SellerInfo> sellers = sellerInfoRepository.findAll(
                (root, query, cb) -> QueryHelp.getPredicate(root, criteria, cb)
        );

        // 如果存在，返回第一个匹配的卖家
        if (!sellers.isEmpty()) {

            return sellers.get(0);

        }

        SellerInfo newSeller = new SellerInfo();
        newSeller.setName(name);
        newSeller.setSsn(ssn);
        newSeller.setContactInfo(contactInfo);
        newSeller.setPaymentMethod(orderPaymentMethod);
        logger.info("保存卖家信息: {}", newSeller.getPaymentMethod());
        return sellerInfoRepository.save(newSeller);
    }



    @Override
    @Transactional
    public SellerInfo save(SellerInfo sellerInfo) {
        return sellerInfoRepository.save(sellerInfo);
    }

    @Override
    public boolean existsByContactInfo(String contactInfo) {
        return sellerInfoRepository.existsByContactInfo(contactInfo);
    }
    @Override
    public boolean checkSsnExists(String ssn) {
        return sellerInfoRepository.existsBySsn(ssn);
    }

}