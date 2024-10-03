
/*
  员工表
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
