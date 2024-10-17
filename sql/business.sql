
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
create table eladmin.bus_order
(
    order_id             bigint auto_increment comment '订单ID' primary key,
    order_number         varchar(50) not null comment '订单编号',
    order_app_id     bigint null comment 'App项目编号',
    order_app_name       varchar(255) null comment 'App名称',
    order_account_username varchar(255) null comment '账号名',
    order_account_password varchar(255) null comment '账号密码',
    order_status         enum ('待处理', '已付款', '已发货', '已完成', '已取消', '已退款', '挂起') default '待处理' null comment '订单状态',
    order_seller_id      bigint null comment '卖家ID',
    order_seller_name    varchar(255) null comment '卖家名称',
    order_seller_ssn     varchar(50) null comment '卖家SSN',
    order_contact_info   varchar(255) null comment '卖家联系方式',
    order_amount         decimal(10, 2) default 0.00 null comment '订单金额',
    order_commission     decimal(10, 2) default 0.00 null comment '佣金',
    order_employee_id    bigint not null comment '下单员工ID',
    order_payment_method varchar(50) null comment '支付方式',
    order_referrer_id    bigint null comment '推荐人ID',
    order_referrer_name  varchar(255) null comment '推荐人名称',
    order_referrer_ssn   varchar(50) null comment '推荐人SSN',
    order_referrer_info  varchar(255) null comment '推荐人联系方式',
    order_referral_fee   decimal(10, 2) default 0.00 null comment '推荐费',
    order_created_at     datetime default current_timestamp() null comment '创建时间',
    order_updated_at     timestamp default current_timestamp() null on update current_timestamp() comment '更新时间',
    order_remark         text null comment '备注',
    constraint order_number unique (order_number),
    constraint fk_employee foreign key (order_employee_id) references eladmin.sys_user (user_id),
    constraint fk_account foreign key (order_app_id) references eladmin.bus_app_info (account_id) on delete set null,
    constraint fk_referrer foreign key (order_referrer_id) references eladmin.bus_seller_info (seller_id) on delete set null,
    constraint fk_seller foreign key (order_seller_id) references eladmin.bus_seller_info (seller_id) on delete set null
)
    comment = '订单表';



/*
 app注册信息表
 */
CREATE TABLE bus_app_info (
                              account_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '账号记录ID',
                              app_name VARCHAR(255) NOT NULL COMMENT '具体的App名称',
                              account_username VARCHAR(255) NOT NULL COMMENT '平台账号名',
                              account_password VARCHAR(255) NOT NULL COMMENT '平台账号密码',
                              account_status ENUM('激活', '未激活', '密码找回', '停用') DEFAULT '未激活' COMMENT '账号状态',
                              created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
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
                              government_id_type ENUM('护照', '驾驶执照', '绿卡') COMMENT '证件类型',
                              government_id_number VARCHAR(100) COMMENT '证件号',
                              security_question VARCHAR(255) COMMENT '安全问题',
                              security_answer VARCHAR(255) COMMENT '答案',
                              remark TEXT COMMENT '备注'
);


/**
  卖家和项目映射关系表
 */
CREATE TABLE eladmin.bus_seller_platform_mapping (
                                                 mapping_id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '映射ID',
                                                 seller_id BIGINT NOT NULL COMMENT '卖家ID（关联bus_seller_info表）',
                                                 account_id BIGINT NOT NULL COMMENT '账号记录ID（关联bus_app_info表）',

    -- 外键约束
                                                 CONSTRAINT fk_seller FOREIGN KEY (seller_id) REFERENCES eladmin.bus_seller_info (seller_id),
                                                 CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES eladmin.bus_app_info (account_id),

    -- 索引
                                                 INDEX idx_seller_id (seller_id),
                                                 INDEX idx_account_id (account_id)
) COMMENT '卖家-平台账号映射表';
