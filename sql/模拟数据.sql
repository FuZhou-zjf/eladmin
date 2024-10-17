/**
  卖家模拟数据
 */
-- 1. 正常信息完整的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, utility_bill, bank_statement, video_url, total_income, created_at, last_updated, remarks)
VALUES ('张三', '微信：zhangsan', 'zhangsan@example.com', '13800138000', '11010119900101001X', 'SSN12345', 'link_to_id_front_1', 'link_to_id_back_1', 'link_to_id_handheld_1', 'link_to_ssn_front_1', 'link_to_ssn_back_1', 'link_to_utility_bill_1', 'link_to_bank_statement_1', 'link_to_video_1', 10000.00, '2024-01-10 10:00:00', '2024-02-15 15:00:00', '信用良好');

-- 2. 缺少部分认证信息的卖家（没有身份证手持照片）
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, ssn_front, ssn_back, utility_bill, bank_statement, total_income, created_at, last_updated)
VALUES ('李四', 'QQ：12345678', 'lisi@example.com', '13800138001', '11010119850101002X', 'SSN67890', 'link_to_id_front_2', 'link_to_id_back_2', 'link_to_ssn_front_2', 'link_to_ssn_back_2', 'link_to_utility_bill_2', 'link_to_bank_statement_2', 5000.00, '2024-02-01 09:30:00', '2024-03-01 14:00:00');

-- 3. 没有收入记录的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, total_income, created_at, last_updated)
VALUES ('王五', '电话：13900139000', 'wangwu@example.com', '13900139000', '11010119790101003X', 'SSN13579', 'link_to_id_front_3', 'link_to_id_back_3', 'link_to_id_handheld_3', 0.00, '2024-03-05 08:00:00', '2024-03-06 12:00:00');

-- 4. 联系方式缺失的卖家（没有电话号码）
INSERT INTO bus_seller_info (name, contact_info, email, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, utility_bill, total_income, created_at, last_updated, remarks)
VALUES ('赵六', '微信：zhaoliu', 'zhaoliu@example.com', '11010119670101004X', 'SSN24680', 'link_to_id_front_4', 'link_to_id_back_4', 'link_to_id_handheld_4', 'link_to_ssn_front_4', 'link_to_ssn_back_4', 'link_to_utility_bill_4', 8000.00, '2024-04-10 11:00:00', '2024-04-15 13:00:00', '需要进一步验证联系方式');

-- 5. 近期注册的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, video_url, total_income, created_at, last_updated)
VALUES ('孙七', '电话：13888888888', 'sunqi@example.com', '13888888888', '11010119950101005X', 'SSN54321', 'link_to_id_front_5', 'link_to_id_back_5', 'link_to_id_handheld_5', 'link_to_ssn_front_5', 'link_to_ssn_back_5', 'link_to_video_5', 0.00, '2024-09-20 16:00:00', '2024-09-20 16:00:00');

-- 6. 总收入较高的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, utility_bill, bank_statement, total_income, created_at, last_updated, remarks)
VALUES ('周八', 'QQ：87654321', 'zhouba@example.com', '13700137000', '11010119720101006X', 'SSN98765', 'link_to_id_front_6', 'link_to_id_back_6', 'link_to_id_handheld_6', 'link_to_utility_bill_6', 'link_to_bank_statement_6', 20000.00, '2024-01-15 10:00:00', '2024-06-10 12:00:00', '高收入卖家');

-- 7. 没有电子邮件的卖家
INSERT INTO bus_seller_info (name, contact_info, phone_number, identity_number, ssn, id_front, id_back, id_handheld, total_income, created_at, last_updated)
VALUES ('吴九', '电话：13600136000', '13600136000', '11010119990101007X', 'SSN19283', 'link_to_id_front_7', 'link_to_id_back_7', 'link_to_id_handheld_7', 3000.00, '2024-05-01 14:00:00', '2024-05-15 18:00:00');

-- 8. 没有身份证件照片的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, total_income, created_at, last_updated)
VALUES ('郑十', '微信：zhengshi', 'zhengshi@example.com', '13500135000', '11010119830101008X', 'SSN87654', 1000.00, '2024-03-20 09:00:00', '2024-03-25 10:00:00');

-- 9. 已完成全部验证的卖家（含视频验证）
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, utility_bill, bank_statement, video_url, total_income, created_at, last_updated)
VALUES ('王十一', 'QQ：11112222', 'wangshiyi@example.com', '13400134000', '11010119880101009X', 'SSN01928', 'link_to_id_front_9', 'link_to_id_back_9', 'link_to_id_handheld_9', 'link_to_ssn_front_9', 'link_to_ssn_back_9', 'link_to_utility_bill_9', 'link_to_bank_statement_9', 'link_to_video_9', 15000.00, '2024-06-05 11:00:00', '2024-06-10 17:00:00');

-- 10. 总收入为负值的卖家（特殊情况，例如退款导致）
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, total_income, created_at, last_updated, remarks)
VALUES ('冯十二', '电话：13912345678', 'fengshier@example.com', '13912345678', '11010119750101010X', 'SSN56473', 'link_to_id_front_10', 'link_to_id_back_10', -500.00, '2024-02-10 08:30:00', '2024-02-15 13:45:00', '收入异常，需核实');-- 1. 正常信息完整的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, utility_bill, bank_statement, video_url, total_income, created_at, last_updated, remarks)
VALUES ('张三', '微信：zhangsan', 'zhangsan@example.com', '13800138000', '11010119900101001X', 'SSN12345', 'link_to_id_front_1', 'link_to_id_back_1', 'link_to_id_handheld_1', 'link_to_ssn_front_1', 'link_to_ssn_back_1', 'link_to_utility_bill_1', 'link_to_bank_statement_1', 'link_to_video_1', 10000.00, '2024-01-10 10:00:00', '2024-02-15 15:00:00', '信用良好');

-- 2. 缺少部分认证信息的卖家（没有身份证手持照片）
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, ssn_front, ssn_back, utility_bill, bank_statement, total_income, created_at, last_updated)
VALUES ('李四', 'QQ：12345678', 'lisi@example.com', '13800138001', '11010119850101002X', 'SSN67890', 'link_to_id_front_2', 'link_to_id_back_2', 'link_to_ssn_front_2', 'link_to_ssn_back_2', 'link_to_utility_bill_2', 'link_to_bank_statement_2', 5000.00, '2024-02-01 09:30:00', '2024-03-01 14:00:00');

-- 3. 没有收入记录的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, total_income, created_at, last_updated)
VALUES ('王五', '电话：13900139000', 'wangwu@example.com', '13900139000', '11010119790101003X', 'SSN13579', 'link_to_id_front_3', 'link_to_id_back_3', 'link_to_id_handheld_3', 0.00, '2024-03-05 08:00:00', '2024-03-06 12:00:00');

-- 4. 联系方式缺失的卖家（没有电话号码）
INSERT INTO bus_seller_info (name, contact_info, email, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, utility_bill, total_income, created_at, last_updated, remarks)
VALUES ('赵六', '微信：zhaoliu', 'zhaoliu@example.com', '11010119670101004X', 'SSN24680', 'link_to_id_front_4', 'link_to_id_back_4', 'link_to_id_handheld_4', 'link_to_ssn_front_4', 'link_to_ssn_back_4', 'link_to_utility_bill_4', 8000.00, '2024-04-10 11:00:00', '2024-04-15 13:00:00', '需要进一步验证联系方式');

-- 5. 近期注册的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, video_url, total_income, created_at, last_updated)
VALUES ('孙七', '电话：13888888888', 'sunqi@example.com', '13888888888', '11010119950101005X', 'SSN54321', 'link_to_id_front_5', 'link_to_id_back_5', 'link_to_id_handheld_5', 'link_to_ssn_front_5', 'link_to_ssn_back_5', 'link_to_video_5', 0.00, '2024-09-20 16:00:00', '2024-09-20 16:00:00');

-- 6. 总收入较高的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, utility_bill, bank_statement, total_income, created_at, last_updated, remarks)
VALUES ('周八', 'QQ：87654321', 'zhouba@example.com', '13700137000', '11010119720101006X', 'SSN98765', 'link_to_id_front_6', 'link_to_id_back_6', 'link_to_id_handheld_6', 'link_to_utility_bill_6', 'link_to_bank_statement_6', 20000.00, '2024-01-15 10:00:00', '2024-06-10 12:00:00', '高收入卖家');

-- 7. 没有电子邮件的卖家
INSERT INTO bus_seller_info (name, contact_info, phone_number, identity_number, ssn, id_front, id_back, id_handheld, total_income, created_at, last_updated)
VALUES ('吴九', '电话：13600136000', '13600136000', '11010119990101007X', 'SSN19283', 'link_to_id_front_7', 'link_to_id_back_7', 'link_to_id_handheld_7', 3000.00, '2024-05-01 14:00:00', '2024-05-15 18:00:00');

-- 8. 没有身份证件照片的卖家
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, total_income, created_at, last_updated)
VALUES ('郑十', '微信：zhengshi', 'zhengshi@example.com', '13500135000', '11010119830101008X', 'SSN87654', 1000.00, '2024-03-20 09:00:00', '2024-03-25 10:00:00');

-- 9. 已完成全部验证的卖家（含视频验证）
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, id_handheld, ssn_front, ssn_back, utility_bill, bank_statement, video_url, total_income, created_at, last_updated)
VALUES ('王十一', 'QQ：11112222', 'wangshiyi@example.com', '13400134000', '11010119880101009X', 'SSN01928', 'link_to_id_front_9', 'link_to_id_back_9', 'link_to_id_handheld_9', 'link_to_ssn_front_9', 'link_to_ssn_back_9', 'link_to_utility_bill_9', 'link_to_bank_statement_9', 'link_to_video_9', 15000.00, '2024-06-05 11:00:00', '2024-06-10 17:00:00');

-- 10. 总收入为负值的卖家（特殊情况，例如退款导致）
INSERT INTO bus_seller_info (name, contact_info, email, phone_number, identity_number, ssn, id_front, id_back, total_income, created_at, last_updated, remarks)
VALUES ('冯十二', '电话：13912345678', 'fengshier@example.com', '13912345678', '11010119750101010X', 'SSN56473', 'link_to_id_front_10', 'link_to_id_back_10', -500.00, '2024-02-10 08:30:00', '2024-02-15 13:45:00', '收入异常，需核实');

/**
  订单模拟
 */
-- 订单表数据插入

-- 1. 正常订单，关联张三
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER001', 1, '亚马逊', 'zhangsan_user', '已付款', 1, '张三', 'SSN12345', 1500.00, 150.00, 1001, '微信支付', '2024-07-10 12:00:00');

-- 2. 缺少部分认证的卖家李四的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER002', 2, 'eBay', 'lisi_user', '待处理', 2, '李四', 'SSN67890', 2000.00, 200.00, 1002, '支付宝', '2024-07-11 15:00:00');

-- 3. 没有收入记录的卖家王五的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER003', 3, '阿里巴巴', 'wangwu_user', '已完成', 3, '王五', 'SSN13579', 500.00, 50.00, 1003, '银行卡', '2024-07-12 10:30:00');

-- 4. 联系方式缺失的卖家赵六的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER004', 4, '京东', 'zhaoliu_user', '已取消', 4, '赵六', 'SSN24680', 800.00, 80.00, 1004, '信用卡', '2024-07-13 14:00:00');
-- 5. 近期注册的卖家孙七的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER005', 5, '拼多多', 'sunqi_user', '挂起', 5, '孙七', 'SSN54321', 1000.00, 100.00, 1005, '现金', '2024-07-14 09:00:00');

-- 6. 总收入较高的卖家周八的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER006', 6, '淘宝', 'zhouba_user', '已发货', 6, '周八', 'SSN98765', 3000.00, 300.00, 1006, '微信支付', '2024-07-15 13:00:00');

-- 7. 没有电子邮件的卖家吴九的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER007', 7, '沃尔玛', 'wujiu_user', '已退款', 7, '吴九', 'SSN19283', 1200.00, 120.00, 1007, '支付宝', '2024-07-16 11:30:00');
-- 8. 没有身份证件照片的卖家郑十的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER008', 8, '抖音', 'zhengshi_user', '已完成', 8, '郑十', 'SSN87654', 700.00, 70.00, 1008, '信用卡', '2024-07-17 10:00:00');

-- 9. 已完成全部验证的卖家王十一的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER009', 9, '抖音', 'wangshiyi_user', '已完成', 9, '王十一', 'SSN01928', 2500.00, 250.00, 1009, '银行卡', '2024-07-18 15:00:00');

-- 10. 总收入为负值的卖家冯十二的订单（特殊情况）
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER010', 10, 'Facebook', 'fengshier_user', '已完成', 10, '冯十二', 'SSN56473', -500.00, -50.00, 1010, '现金', '2024-07-19 17:00:00');
-- 11. 张三的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER011', 1, '亚马逊', 'zhangsan_user', '已付款', 1, '张三', 'SSN12345', 1800.00, 180.00, 1001, '支付宝', '2024-07-20 12:00:00');

-- 12. 李四的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER012', 2, 'eBay', 'lisi_user', '已发货', 2, '李四', 'SSN67890', 2200.00, 220.00, 1002, '微信支付', '2024-07-21 15:30:00');
-- 13. 王五的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER013', 3, '阿里巴巴', 'wangwu_user', '待处理', 3, '王五', 'SSN13579', 600.00, 60.00, 1003, '银行卡', '2024-07-22 14:00:00');

-- 14. 赵六的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER014', 4, '京东', 'zhaoliu_user', '已完成', 4, '赵六', 'SSN24680', 900.00, 90.00, 1004, '信用卡', '2024-07-23 11:00:00');
-- 15. 孙七的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER015', 5, '拼多多', 'sunqi_user', '已付款', 5, '孙七', 'SSN54321', 1100.00, 110.00, 1005, '现金', '2024-07-24 10:00:00');

-- 16. 周八的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER016', 6, '淘宝', 'zhouba_user', '挂起', 6, '周八', 'SSN98765', 3500.00, 350.00, 1006, '支付宝', '2024-07-25 16:00:00');

-- 17. 吴九的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER017', 7, '沃尔玛', 'wujiu_user', '已发货', 7, '吴九', 'SSN19283', 1300.00, 130.00, 1007, '微信支付', '2024-07-26 13:00:00');

-- 18. 郑十的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER018', 8, 'T k', 'zhengshi_user', '已取消', 8, '郑十', 'SSN87654', 800.00, 80.00, 1008, '信用卡', '2024-07-27 09:00:00');-- 15. 孙七的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER015', 5, '拼多多', 'sunqi_user', '已付款', 5, '孙七', 'SSN54321', 1100.00, 110.00, 1005, '现金', '2024-07-24 10:00:00');

-- 19. 王十一的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER019', 9, '抖音', 'wangshiyi_user', '已发货', 9, '王十一', 'SSN01928', 2700.00, 270.00, 1009, '银行卡', '2024-07-28 14:00:00');

-- 20. 冯十二的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER020', 10, 'Facebook', 'fengshier_user', '已退款', 10, '冯十二', 'SSN56473', -300.00, -30.00, 1010, '现金', '2024-07-29 17:00:00');-- 订单表数据插入

-- 1. 正常订单，关联张三
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER001', 1, '亚马逊', 'zhangsan_user', '已付款', 1, '张三', 'SSN12345', 1500.00, 150.00, 1001, '微信支付', '2024-07-10 12:00:00');

-- 2. 缺少部分认证的卖家李四的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER002', 2, 'eBay', 'lisi_user', '待处理', 2, '李四', 'SSN67890', 2000.00, 200.00, 1002, '支付宝', '2024-07-11 15:00:00');

-- 3. 没有收入记录的卖家王五的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER003', 3, '阿里巴巴', 'wangwu_user', '已完成', 3, '王五', 'SSN13579', 500.00, 50.00, 1003, '银行卡', '2024-07-12 10:30:00');

-- 4. 联系方式缺失的卖家赵六的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER004', 4, '京东', 'zhaoliu_user', '已取消', 4, '赵六', 'SSN24680', 800.00, 80.00, 1004, '信用卡', '2024-07-13 14:00:00');
-- 5. 近期注册的卖家孙七的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER005', 5, '拼多多', 'sunqi_user', '挂起', 5, '孙七', 'SSN54321', 1000.00, 100.00, 1005, '现金', '2024-07-14 09:00:00');

-- 6. 总收入较高的卖家周八的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER006', 6, '淘宝', 'zhouba_user', '已发货', 6, '周八', 'SSN98765', 3000.00, 300.00, 1006, '微信支付', '2024-07-15 13:00:00');

-- 7. 没有电子邮件的卖家吴九的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER007', 7, '沃尔玛', 'wujiu_user', '已退款', 7, '吴九', 'SSN19283', 1200.00, 120.00, 1007, '支付宝', '2024-07-16 11:30:00');
-- 8. 没有身份证件照片的卖家郑十的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER008', 8, '抖音', 'zhengshi_user', '已完成', 8, '郑十', 'SSN87654', 700.00, 70.00, 1008, '信用卡', '2024-07-17 10:00:00');

-- 9. 已完成全部验证的卖家王十一的订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER009', 9, '抖音', 'wangshiyi_user', '已完成', 9, '王十一', 'SSN01928', 2500.00, 250.00, 1009, '银行卡', '2024-07-18 15:00:00');

-- 10. 总收入为负值的卖家冯十二的订单（特殊情况）
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER010', 10, 'Facebook', 'fengshier_user', '已完成', 10, '冯十二', 'SSN56473', -500.00, -50.00, 1010, '现金', '2024-07-19 17:00:00');
-- 11. 张三的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER011', 1, '亚马逊', 'zhangsan_user', '已付款', 1, '张三', 'SSN12345', 1800.00, 180.00, 1001, '支付宝', '2024-07-20 12:00:00');

-- 12. 李四的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER012', 2, 'eBay', 'lisi_user', '已发货', 2, '李四', 'SSN67890', 2200.00, 220.00, 1002, '微信支付', '2024-07-21 15:30:00');
-- 13. 王五的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER013', 3, '阿里巴巴', 'wangwu_user', '待处理', 3, '王五', 'SSN13579', 600.00, 60.00, 1003, '银行卡', '2024-07-22 14:00:00');

-- 14. 赵六的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER014', 4, '京东', 'zhaoliu_user', '已完成', 4, '赵六', 'SSN24680', 900.00, 90.00, 1004, '信用卡', '2024-07-23 11:00:00');
-- 15. 孙七的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER015', 5, '拼多多', 'sunqi_user', '已付款', 5, '孙七', 'SSN54321', 1100.00, 110.00, 1005, '现金', '2024-07-24 10:00:00');

-- 16. 周八的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER016', 6, '淘宝', 'zhouba_user', '挂起', 6, '周八', 'SSN98765', 3500.00, 350.00, 1006, '支付宝', '2024-07-25 16:00:00');

-- 17. 吴九的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER017', 7, '沃尔玛', 'wujiu_user', '已发货', 7, '吴九', 'SSN19283', 1300.00, 130.00, 1007, '微信支付', '2024-07-26 13:00:00');

-- 18. 郑十的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER018', 8, 'T k', 'zhengshi_user', '已取消', 8, '郑十', 'SSN87654', 800.00, 80.00, 1008, '信用卡', '2024-07-27 09:00:00');-- 15. 孙七的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER015', 5, '拼多多', 'sunqi_user', '已付款', 5, '孙七', 'SSN54321', 1100.00, 110.00, 1005, '现金', '2024-07-24 10:00:00');

-- 19. 王十一的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER019', 9, '抖音', 'wangshiyi_user', '已发货', 9, '王十一', 'SSN01928', 2700.00, 270.00, 1009, '银行卡', '2024-07-28 14:00:00');

-- 20. 冯十二的第二个订单
INSERT INTO eladmin.bus_order (order_number, order_app_id, order_app_name, order_account_username, order_status, order_seller_id, order_seller_name, order_seller_ssn, order_amount, order_commission, order_employee_id, order_payment_method, order_created_at)
VALUES ('ORDER020', 10, 'Facebook', 'fengshier_user', '已退款', 10, '冯十二', 'SSN56473', -300.00, -30.00, 1010, '现金', '2024-07-29 17:00:00');