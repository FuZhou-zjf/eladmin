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
    package me.zhengjie.finance.service.impl;
    
    import me.zhengjie.finance.domain.FinanceRecords;
    import me.zhengjie.order.domain.Order;
    import me.zhengjie.utils.ValidationUtil;
    import me.zhengjie.utils.FileUtil;
    import lombok.RequiredArgsConstructor;
    import me.zhengjie.finance.repository.FinanceRecordsRepository;
    import me.zhengjie.finance.service.FinanceRecordsService;
    import me.zhengjie.finance.service.dto.FinanceRecordsDto;
    import me.zhengjie.finance.service.dto.FinanceRecordsQueryCriteria;
    import me.zhengjie.finance.service.mapstruct.FinanceRecordsMapper;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import me.zhengjie.utils.PageUtil;
    import me.zhengjie.utils.QueryHelp;
    import java.math.BigDecimal;
    import java.sql.Timestamp;
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
    * @author Laozhao
    * @date 2024-11-01
    **/
    @Service
    @RequiredArgsConstructor
    public class FinanceRecordsServiceImpl implements FinanceRecordsService {

        private final FinanceRecordsRepository financeRecordsRepository;
        private final FinanceRecordsMapper financeRecordsMapper;
        private static final Logger logger = LoggerFactory.getLogger(FinanceRecordsServiceImpl.class);


        @Override
        public PageResult<FinanceRecordsDto> queryAll(FinanceRecordsQueryCriteria criteria, Pageable pageable) {
            Page<FinanceRecords> page = financeRecordsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
            return PageUtil.toPage(page.map(financeRecordsMapper::toDto));
        }

        @Override
        public List<FinanceRecordsDto> queryAll(FinanceRecordsQueryCriteria criteria) {
            return financeRecordsMapper.toDto(financeRecordsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
        }

        @Override
        @Transactional
        public FinanceRecordsDto findById(Long id) {
            FinanceRecords financeRecords = financeRecordsRepository.findById(id).orElseGet(FinanceRecords::new);
            ValidationUtil.isNull(financeRecords.getId(), "FinanceRecords", "id", id);
            return financeRecordsMapper.toDto(financeRecords);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void create(FinanceRecords resources) {
            financeRecordsRepository.save(resources);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void update(FinanceRecords resources) {
            FinanceRecords financeRecords = financeRecordsRepository.findById(resources.getId()).orElseGet(FinanceRecords::new);
            ValidationUtil.isNull(financeRecords.getId(), "FinanceRecords", "id", resources.getId());
            financeRecords.copy(resources);
            financeRecordsRepository.save(financeRecords);
        }

        @Override
        public void deleteAll(Long[] ids) {
            for (Long id : ids) {
                financeRecordsRepository.deleteById(id);
            }
        }

        @Override
        public void download(List<FinanceRecordsDto> all, HttpServletResponse response) throws IOException {
            List<Map<String, Object>> list = new ArrayList<>();
            for (FinanceRecordsDto financeRecords : all) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("订单ID", financeRecords.getOrderId());
                map.put("交易日期", financeRecords.getDate());
                map.put("账户ID", financeRecords.getAccountId());
                map.put("账户类型: 员工/卖家/推荐人", financeRecords.getAccountType());
                map.put("交易类型: 收入/支出", financeRecords.getType());
                map.put("交易类别: 销售/佣金/推荐费", financeRecords.getCategory());
                map.put("金额", financeRecords.getAmount());
                map.put("备注", financeRecords.getDescription());
                map.put("创建时间", financeRecords.getCreatedAt());
                map.put("更新时间", financeRecords.getUpdatedAt());
                list.add(map);
            }
            FileUtil.downloadExcel(list, response);
        }

        @Transactional
        public void createFinanceRecordsForOrder(Order resources) {
            // 销售收入记录
            if (resources.getOrderAmount() != null && resources.getOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
                createFinanceRecord(
                        resources.getOrderId(),
                        resources.getOrderEmployeeId(),
                        "employee",
                        "amount",
                        "income",
                        resources.getOrderAmount(),
                        resources.getOrderCreatedAt(),
                        resources.getOrderRemark()
                );
            } else {
                // 添加日志记录
                logger.warn("无法创建销售收入财务记录，订单金额无效。订单ID：" + resources.getOrderId());
            }

            // 卖家佣金记录
            if (resources.getOrderCommission() != null && resources.getOrderCommission().compareTo(BigDecimal.ZERO) > 0) {
                if (resources.getOrderSeller() != null && resources.getOrderSeller().getSellerId() != null) {
                    createFinanceRecord(
                            resources.getOrderId(),
                            resources.getOrderSeller().getSellerId(),
                            "seller",
                            "commission",
                            "expense",
                            resources.getOrderCommission(),
                            resources.getOrderCreatedAt(),
                            resources.getOrderRemark()
                    );
                } else {
                    // 添加日志记录
                    logger.warn("无法创建卖家佣金财务记录，卖家信息缺失或无效。订单ID：" + resources.getOrderId());
                }
            } else {
                // 添加日志记录
                logger.warn("无法创建卖家佣金财务记录，佣金金额无效。订单ID：" + resources.getOrderId());
            }

            // 推荐费记录
            if (resources.getOrderReferralFee() != null && resources.getOrderReferralFee().compareTo(BigDecimal.ZERO) > 0) {
                if (resources.getOrderReferrer() != null && resources.getOrderReferrer().getSellerId() != null) {
                    createFinanceRecord(
                            resources.getOrderId(),
                            resources.getOrderReferrer().getSellerId(),
                            "referrer",
                            "referral_fee",
                            "expense",
                            resources.getOrderReferralFee(),
                            resources.getOrderCreatedAt(),
                            resources.getOrderRemark()
                    );
                } else {
                    // 添加日志记录
                    logger.warn("无法创建推荐费财务记录，推荐人信息缺失或无效。订单ID：" + resources.getOrderId());
                }
            } else {
                // 添加日志记录
                logger.warn("无法创建推荐费财务记录，推荐费金额无效。订单ID：" + resources.getOrderId());
            }
        }


        private void createFinanceRecord(Long orderId, Long accountId, String accountType, String category, String type, BigDecimal amount, Timestamp date, String description) {
            if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                FinanceRecords record = new FinanceRecords();
                record.setOrderId(orderId);
                record.setAccountId(accountId);
                record.setAccountType(accountType);
                record.setCategory(category);
                record.setType(type);
                record.setAmount(amount);
                record.setDate(date);
                record.setDescription(description);
                financeRecordsRepository.save(record);
            }
        }
        @Transactional
        public void updateFinanceRecordsForOrder(Order order) {
            // 删除原有的财务记录
            financeRecordsRepository.deleteByOrderId(order.getOrderId());

            // 重新创建财务记录
            createFinanceRecordsForOrder(order);
        }



    }