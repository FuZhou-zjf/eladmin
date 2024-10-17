
/**
  卖家表
 */
CREATE TABLE bus_seller_info (
                                 seller_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '卖家主键',
                                 name VARCHAR(100) NOT NULL COMMENT '卖家姓名',
                                 contact_info VARCHAR(150) COMMENT '联系方式',
                                 email VARCHAR(150) COMMENT '电子邮件',
                                 phone_number VARCHAR(20) COMMENT '电话号码',
                                 identity_number VARCHAR(50) COMMENT '身份证号码',
                                 ssn VARCHAR(50) COMMENT '社会安全号',
                                 id_front VARCHAR(150) COMMENT '身份证正面照片',
                                 id_back VARCHAR(150) COMMENT '身份证背面照片',
                                 id_handheld VARCHAR(150) COMMENT '手持身份证照片',
                                 ssn_front VARCHAR(150) COMMENT 'SSN正面照片',
                                 ssn_back VARCHAR(150) COMMENT 'SSN背面照片',
                                 utility_bill VARCHAR(150) COMMENT '水电账单照片',
                                 bank_statement VARCHAR(150) COMMENT '银行对账单照片',
                                 video_url VARCHAR(255) COMMENT '卖家视频链接',
                                 total_income DECIMAL(10, 2) COMMENT '总收入',
                                 created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
                                 remarks TEXT COMMENT '备注'


) COMMENT='卖家信息表';



/**
  订单表
 */
CREATE TABLE eladmin.bus_order (
                                   order_id               BIGINT AUTO_INCREMENT COMMENT '订单ID' PRIMARY KEY,
                                   order_number           VARCHAR(20) NOT NULL COMMENT '订单编号',
                                   order_status           VARCHAR(50) NOT NULL DEFAULT '0' COMMENT '订单状态',

    -- 基础账号信息
                                   order_account_username VARCHAR(255) NULL COMMENT '账号名',
                                   order_account_password VARCHAR(255) NULL COMMENT '密码',

    -- App 信息
                                   order_app_id           BIGINT NULL COMMENT 'App项目编号',
                                   order_app_name         VARCHAR(255) NULL COMMENT 'App名称',

    -- 卖家信息
                                   order_seller_id        BIGINT NULL COMMENT '卖家ID',
                                   order_seller_name      VARCHAR(255) NULL COMMENT '卖家名称',
                                   order_seller_ssn       VARCHAR(50) NULL COMMENT '卖家SSN',
                                   order_contact_info     VARCHAR(255) NULL COMMENT '卖家联系方式',
                                   order_payment_method   VARCHAR(50) NULL COMMENT '支付方式',
                                   order_contact_other    VARCHAR(255) NULL COMMENT '紧急联系方式',

    -- 推荐人信息
                                   order_referrer_id      BIGINT NULL COMMENT '推荐人ID',
                                   order_referrer_name    VARCHAR(255) NULL COMMENT '推荐人名称',
                                   order_referrer_ssn     VARCHAR(50) NULL COMMENT '推荐人SSN',
                                   order_referrer_info    VARCHAR(255) NULL COMMENT '推荐人联系方式',
                                   order_referrer_other    VARCHAR(255) NULL COMMENT '推荐人紧急联系方式',
                                   order_referrer_method  VARCHAR(50) NULL COMMENT '推荐人支付方式',
                                   order_referral_fee     DECIMAL(5, 2) DEFAULT 0.00 NOT NULL COMMENT '推荐费',

    -- 金额信息
                                   order_amount           DECIMAL(5, 2) DEFAULT 0.00 NOT NULL COMMENT '订单金额',


    -- 订单员工信息
                                   order_employee_id      BIGINT NOT NULL COMMENT '下单员工ID',
                                   order_commission       DECIMAL(5, 2) DEFAULT 0.00 NOT NULL COMMENT '佣金',


    -- 时间戳和备注
                                   order_created_at       DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   order_updated_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   order_remark           TEXT NULL COMMENT '备注',

                                   CONSTRAINT order_number UNIQUE (order_number)
)
    COMMENT '订单表';


/*
 app注册信息表
 */
CREATE TABLE bus_app_info (
                              account_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '账号ID',
                              app_name VARCHAR(255) NOT NULL COMMENT 'App名称',
                              account_username VARCHAR(255) NOT NULL COMMENT '账号名',
                              account_password VARCHAR(255) NOT NULL COMMENT '账号密码',
                              account_status varchar(50) default '0' not null comment '账号状态',
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 个人信息
                              full_name VARCHAR(255) NOT NULL COMMENT '全名',
                              ssn VARCHAR(20) COMMENT 'SSN或EIN',
                              birth_date DATE COMMENT '出生日期',
                              address_line1 VARCHAR(255) COMMENT '地址1',
                              address_line2 VARCHAR(255) COMMENT '地址2',
                              city VARCHAR(100) COMMENT '城市',
                              state VARCHAR(100) COMMENT '州/省',
                              postal_code VARCHAR(20) COMMENT '邮编',


    -- 联系方式
                              phone_number VARCHAR(20) COMMENT '联系电话',
                              email VARCHAR(255) COMMENT '电子邮件',

    -- 银行信息
                              bank_account_number VARCHAR(50) COMMENT '银行账号',
                              bank_routing_number VARCHAR(9) COMMENT '路由号',

    -- 身份验证
                              government_id_number VARCHAR(100) COMMENT '证件号',
                              security_question VARCHAR(255) COMMENT '安全问题',
                              security_answer VARCHAR(255) COMMENT '答案',
                              remark TEXT COMMENT '备注'
);


