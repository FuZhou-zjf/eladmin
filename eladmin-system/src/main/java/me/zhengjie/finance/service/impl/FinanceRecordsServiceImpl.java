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
    
    import me.zhengjie.appinfo.domain.AppInfo;
    import me.zhengjie.finance.domain.FinanceRecords;
    import me.zhengjie.modules.system.service.dto.UserDto;
    import me.zhengjie.order.domain.Order;
    import me.zhengjie.utils.*;
    import lombok.RequiredArgsConstructor;
    import me.zhengjie.finance.repository.FinanceRecordsRepository;
    import me.zhengjie.finance.service.FinanceRecordsService;
    import me.zhengjie.finance.service.dto.FinanceRecordsDto;
    import me.zhengjie.finance.service.dto.FinanceRecordsQueryCriteria;
    import me.zhengjie.finance.service.mapstruct.FinanceRecordsMapper;
    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;

    import java.math.BigDecimal;
    import java.sql.Timestamp;
    import java.time.LocalDate;
    import java.time.temporal.WeekFields;
    import java.util.*;
    import java.io.IOException;
    import javax.servlet.http.HttpServletResponse;

    import me.zhengjie.modules.system.service.UserService;
    
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

        @Autowired
        private UserService userService;
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
            UserDto employeeDto = userService.findById(resources.getOrderEmployeeId());
            String employeeName = employeeDto.getNickName();
            Long operatorId = SecurityUtils.getCurrentUserId();
            String operatorName = SecurityUtils.getCurrentUsername();
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            // 销售收入记录
            if (resources.getOrderAmount() != null && resources.getOrderAmount().compareTo(BigDecimal.ZERO) > 0) {
                createFinanceRecord(
                        resources.getOrderId(),
                        resources.getOrderEmployeeId(),
                        "employee",
                        employeeName,  // 从订单获取员工名称
                        "amount",
                        "income",
                        resources.getOrderAmount(),
                        resources.getOrderCreatedAt(),
                        resources.getOrderRemark(),
                        resources.getOrderNumber() // 从订单获取订单编号

                );
                //跟踪记录
                createFinanceRecord(
                        resources.getOrderId(),
                        operatorId,
                        "employee",
                        operatorName,  // 当前用户名称
                        "pay_fee",
                        "expense",
                        resources.getOrderAmount(),
                        currentTimestamp,
                        "销售收入支付",
                        resources.getOrderNumber() // 从订单获取订单编号

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
                            resources.getOrderSellerName(),  // 从订单获取卖家名称
                            "commission",
                            "expense",
                            resources.getOrderCommission(),
                            resources.getOrderCreatedAt(),
                            resources.getOrderRemark(),
                            resources.getOrderNumber()  // 从订单获取订单编号
                    );
                    createFinanceRecord(
                            resources.getOrderId(),
                            operatorId,
                            "employee",
                            operatorName,  // 从订单获取员工名称
                            "pay_fee",
                            "expense",
                            resources.getOrderCommission(),
                            currentTimestamp,
                            "佣金支付",
                            resources.getOrderNumber() // 从订单获取订单编号

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
                            resources.getOrderReferrerName(), // 从订单获取推荐人名称
                            "referral_fee",
                            "expense",
                            resources.getOrderReferralFee(),
                            resources.getOrderCreatedAt(),
                            resources.getOrderRemark(),
                            resources.getOrderNumber()  // 从订单获取订单编号
                    );
                    createFinanceRecord(
                            resources.getOrderId(),
                            operatorId,
                            "employee",
                            operatorName,  // 从订单获取员工名称
                            "pay_fee",
                            "expense",
                            resources.getOrderReferralFee(),
                            currentTimestamp,
                            "推荐费支付",
                            resources.getOrderNumber() // 从订单获取订单编号

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


        private void createFinanceRecord(Long orderId, Long accountId, String accountType, String accountName, String category, String type, BigDecimal amount, Timestamp date, String description, String orderNumber) {
            if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
                FinanceRecords record = new FinanceRecords();
                record.setOrderId(orderId);
                record.setOrderNumber(orderNumber);  // 新增订单编号
                record.setAccountId(accountId);
                record.setAccountType(accountType);
                record.setAccountName(accountName);  // 新增账号名称
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
        @Transactional
        public void createFinanceRecordsForApp(AppInfo resources) {
            // 检查 AppInfo 中是否有售出金额
            if (resources.getSaleFee() != null && resources.getSaleFee().compareTo(BigDecimal.ZERO) > 0) {

                // 获取当前用户ID，
                Long userId = SecurityUtils.getCurrentUserId() ;  // 你需要实现获取当前用户ID的方法
                String userName = SecurityUtils.getCurrentUsername();  // 你需要实现获取当前用户名的方法
                // 在财务记录表中生成数据

                // 创建财务记录
                createFinanceRecord(
                        resources.getAccountId(),  // 订单 ID，如果与订单无关，可以传 null
                        userId,  // 用户 ID（例如 AppInfo 关联的用户 ID）
                        "employee",  // 账户类型，这里假设是“用户”
                        userName,  // 账户名称，这里是动态生成的用户名（根据 userId）
                        "sale_fee",  // 交易类别：售出金额
                        "income",  // 交易类型：收入
                        resources.getSaleFee(),  // 售出金额
                        resources.getCreatedAt(),  // 交易日期
                        resources.getRemark(),  // 备注
                        resources.getOrderNumber()  // 订单编号，如果没有订单关联，可以传 null
                );
            } else {
                // 如果没有售出金额，则不做任何记录
                logger.warn("没有售出金额，跳过财务记录创建。AppInfo ID: " + resources.getAccountId());
            }
        }

        @Override
        public Map<String, Object> getDailySummary(FinanceRecordsQueryCriteria criteria) {
            return getSummaryByDimension(criteria, "daily");
        }

        @Override
        public Map<String, Object> getWeeklySummary(FinanceRecordsQueryCriteria criteria) {
            return getSummaryByDimension(criteria, "weekly");
        }

        @Override
        public Map<String, Object> getMonthlySummary(FinanceRecordsQueryCriteria criteria) {
            return getSummaryByDimension(criteria, "monthly");
        }

        /**
         * 通用的财务记录统计方法，支持按日、周、月维度统计
         *
         * @param criteria 查询条件
         * @param dimension 统计维度（daily, weekly, monthly）
         * @return 统计结果列表
         */
        @Transactional
        public Map<String, Object> getSummaryByDimension(FinanceRecordsQueryCriteria criteria, String dimension) {
            // 获取当前日期
            LocalDate today = LocalDate.now();

            if (criteria.getStartDate() == null) {
                // 定义默认起始日期
                LocalDate startDateLocal;
                switch (dimension) {
                    case "weekly":
                        // 默认展示过去 4 周的数据
                        startDateLocal = today.minusWeeks(4);
                        break;
                    case "monthly":
                        // 默认展示过去 12 个月的数据
                        startDateLocal = today.minusMonths(12);
                        break;
                    default:
                        // 默认展示当前月的每一天（从月初开始到今天）
                        startDateLocal = LocalDate.now().withDayOfMonth(1);
                        break;
                }
                // 统一转换为 Timestamp 类型，并设置为查询条件的开始日期
                Timestamp startDate = new Timestamp(DateUtil.toDate(startDateLocal).getTime());
                criteria.setStartDate(startDate);
            }

            // 查询财务记录并处理统计结果
            List<FinanceRecords> records = financeRecordsRepository.findAll(
                    (root, query, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)
            );

            return processSummary(records, dimension);
        }

        private Map<String, Object> processSummary(List<FinanceRecords> records, String dimension) {
            // 使用 TreeSet 保持日期顺序（升序）
            Set<String> dateSet = new TreeSet<>();
            Map<String, BigDecimal> incomeMap = new HashMap<>();
            Map<String, BigDecimal> expenseMap = new HashMap<>();
            Map<String, BigDecimal> commissionMap = new HashMap<>();
            Map<String, BigDecimal> referralFeeMap = new HashMap<>();
            Map<String, BigDecimal> collectionAmountMap = new HashMap<>();
            Map<String, BigDecimal> employeePrepaymentMap = new HashMap<>();

            // 遍历财务记录并分类统计
            for (FinanceRecords record : records) {
                String dateKey = getDateKey(record.getDate(), dimension);
                BigDecimal amount = record.getAmount();
                String type = record.getType();
                String category = record.getCategory();

                // 根据字典表映射进行分类统计
                switch (category) {
                    case "amount": // 销售提成
                        incomeMap.merge(dateKey, amount, BigDecimal::add);
                        break;
                    case "commission": // 卖家佣金
                        commissionMap.merge(dateKey, amount, BigDecimal::add);
                        expenseMap.merge(dateKey, amount, BigDecimal::add);
                        break;
                    case "referral_fee": // 推荐费
                        referralFeeMap.merge(dateKey, amount, BigDecimal::add);
                        expenseMap.merge(dateKey, amount, BigDecimal::add);
                        break;
                    case "sale_fee": // 收款金额
                        collectionAmountMap.merge(dateKey, amount, BigDecimal::add);
                        incomeMap.merge(dateKey, amount, BigDecimal::add);
                        break;
                    case "pay_fee": // 预付款金额
                        employeePrepaymentMap.merge(dateKey, amount, BigDecimal::add);
                        expenseMap.merge(dateKey, amount, BigDecimal::add);
                        break;
                    default:
                        if ("income".equalsIgnoreCase(type)) {
                            incomeMap.merge(dateKey, amount, BigDecimal::add);
                        } else if ("expense".equalsIgnoreCase(type)) {
                            expenseMap.merge(dateKey, amount, BigDecimal::add);
                        }
                        break;
                }

                // 添加日期到日期集合
                dateSet.add(dateKey);
            }

            // 准备返回的数据结构
            List<String> categories = new ArrayList<>(dateSet);
            List<Double> incomeData = new ArrayList<>();
            List<Double> paymentData = new ArrayList<>();
            List<Double> commissionData = new ArrayList<>();
            List<Double> referralFeeData = new ArrayList<>();
            List<Double> collectionAmountData = new ArrayList<>();
            List<Double> employeePrepaymentData = new ArrayList<>();

            // 填充数据列表，并补充缺失日期的数据为 0
            for (String date : categories) {
                incomeData.add(incomeMap.getOrDefault(date, BigDecimal.ZERO).doubleValue());
                paymentData.add(expenseMap.getOrDefault(date, BigDecimal.ZERO).doubleValue());
                commissionData.add(commissionMap.getOrDefault(date, BigDecimal.ZERO).doubleValue());
                referralFeeData.add(referralFeeMap.getOrDefault(date, BigDecimal.ZERO).doubleValue());
                collectionAmountData.add(collectionAmountMap.getOrDefault(date, BigDecimal.ZERO).doubleValue());
                employeePrepaymentData.add(employeePrepaymentMap.getOrDefault(date, BigDecimal.ZERO).doubleValue());
            }

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("categories", categories);
            result.put("incomeData", incomeData);
            result.put("paymentData", paymentData);
            result.put("commissionData", commissionData);
            result.put("referralFeeData", referralFeeData);
            result.put("collectionAmountData", collectionAmountData);
            result.put("employeePrepaymentData", employeePrepaymentData);

            logger.info("返回的数据: {}", result);
            return result;
        }




        private String getDateKey(Timestamp timestamp, String dimension) {
            if (timestamp == null) {
                return "N/A";
            }

            LocalDate localDate = timestamp.toLocalDateTime().toLocalDate();

            switch (dimension) {
                case "daily":
                    // 格式：YYYY-MM-DD
                    return localDate.toString();
                case "weekly": {
                    // 使用 ISO 标准，计算周一为一周开始
                    LocalDate startOfWeek = localDate.with(WeekFields.ISO.getFirstDayOfWeek());
                    LocalDate endOfWeek = startOfWeek.plusDays(6);

                    // 格式化日期为用户友好的格式：10月23日 - 10月29日
                    String start = String.format("%d月%d日", startOfWeek.getMonthValue(), startOfWeek.getDayOfMonth());
                    String end = String.format("%d月%d日", endOfWeek.getMonthValue(), endOfWeek.getDayOfMonth());
                    return start + " - " + end;
                }
                case "monthly": {
                    int year = localDate.getYear();
                    int month = localDate.getMonthValue();
                    // 格式化为：YYYY年MM月
                    return String.format("%d年%02d月", year, month);
                }
                default:
                    return localDate.toString();
            }
        }

    }