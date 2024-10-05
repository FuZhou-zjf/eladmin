
/*
  员工表
  作废！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
*/
create table business_user
(
    employee_id    bigint auto_increment comment 'ID - 员工唯一标识'
        primary key,
    dept_id        bigint           null comment '部门ID - 关联部门表的唯一标识',
    job            bigint           null comment '职位 - 关联职位表的唯一标识',
    username       varchar(180)     null comment '用户名 - 系统登录使用的唯一用户名',
    nick_name      varchar(255)     null comment '昵称 - 用户显示名称',
    gender         varchar(2)       null comment '性别 - 用户性别，1表示男，0表示女',
    phone          varchar(255)     null comment '手机号码 - 用户的联系手机号',
    email          varchar(180)     null comment '邮箱 - 用户联系邮箱',
    avatar_name    varchar(255)     null comment '头像地址 - 用户头像的文件名或链接',
    avatar_path    varchar(255)     null comment '头像真实路径 - 用户头像的存储路径',
    password       varchar(255)     null comment '密码 - 系统登录密码',
    is_admin       bit default b'0' null comment '是否为admin账号 - 1为admin账号，0为普通用户',
    enabled        bit              null comment '状态 - 1启用、0禁用',
    create_by      varchar(255)     null comment '创建者 - 记录创建该用户信息的管理员',
    update_by      varchar(255)     null comment '更新者 - 记录最后更新该用户信息的管理员',
    pwd_reset_time datetime         null comment '修改密码的时间 - 最近一次密码修改的时间',
    create_time    datetime         null comment '创建日期 - 记录用户信息创建的时间',
    update_time    datetime         null comment '更新时间 - 记录用户信息最后更新的时间',
    constraint UK_kpubos9gc2cvtkb0thktkbkes
        unique (email),
    constraint uniq_email
        unique (email),
    constraint uniq_username
        unique (username),
    constraint username
        unique (username)
)
    comment '系统用户 - 管理系统的用户表，存储所有用户的基本信息' row_format = COMPACT;

create index FK5rwmryny6jthaaxkogownknqp
    on bussnise_user (dept_id);

create index FK_job_index
    on bussnise_user (job);

create index inx_enabled
    on bussnise_user (enabled);

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
                                   order_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
                                   order_number VARCHAR(50) NOT NULL UNIQUE COMMENT '订单编号',
                                   order_status ENUM('PENDING', 'PAID', 'SHIPPED', 'COMPLETED', 'CANCELED', 'REFUNDED', 'ON_HOLD') DEFAULT 'PENDING' COMMENT '订单状态',
                                   order_created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   order_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 卖家信息
                                   order_seller_id INT NULL COMMENT '卖家ID',
                                   order_seller_name VARCHAR(255) COMMENT '卖家名称',
                                   order_seller_ssn VARCHAR(50) COMMENT '卖家SSN',
                                   order_contact_info VARCHAR(255) COMMENT '卖家联系方式',

    -- 推荐人信息
                                   order_referrer_id INT NULL COMMENT '推荐人ID',
                                   order_referral_fee DECIMAL(10, 2) DEFAULT 0.00 COMMENT '介绍费',

    -- 金额信息
                                   order_amount DECIMAL(10, 2) DEFAULT 0.00 COMMENT '订单金额',
                                   order_commission DECIMAL(10, 2) DEFAULT 0.00 COMMENT '佣金',

    -- 其他信息
                                   order_employee_id INT NOT NULL COMMENT '下单员工ID',
                                   order_project_id INT COMMENT '项目ID',
                                   order_payment_method VARCHAR(50) COMMENT '支付方式',
                                   order_remark TEXT COMMENT '备注',

    -- 外键约束
                                   CONSTRAINT fk_seller FOREIGN KEY (order_seller_id) REFERENCES eladmin.bus_seller_info (seller_id) ON DELETE SET NULL,
                                   CONSTRAINT fk_referrer FOREIGN KEY (order_referrer_id) REFERENCES eladmin.bus_seller_info (seller_id) ON DELETE SET NULL,
                                   CONSTRAINT fk_employee FOREIGN KEY (order_employee_id) REFERENCES eladmin.sys_user (user_id),

    -- 索引
                                   INDEX idx_order_number (order_number),
                                   INDEX idx_seller_id (order_seller_id),
                                   INDEX idx_referrer_id (order_referrer_id),
                                   INDEX idx_employee_id (order_employee_id)
) COMMENT '订单表';
