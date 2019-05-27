create table if not exists user_charge_card
(
  id int auto_increment comment '主键'
    primary key,
  ctime datetime not null comment '创建时间',
  utime datetime null comment '更新时间',
  card_num varchar(32) not null comment '充值卡号',
  user_id int not null comment '用户id',
  user_name varchar(32) null comment '用户名称',
  mobile varchar(32) null comment '手机号',
  money double(12,2) default '0.00' not null comment '卡内余额',
  status int default '1' not null comment '充值卡状态，1,使用中 2,暂停',
  bind_card_time datetime null comment '绑定时间',
  is_bind_wx int default '0' not null comment '是否绑定微信，0,未绑定 1,已绑定',
  constraint user_charge_card_id_uindex
  unique (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值卡';

-- 订单表中增加支付卡号字段，以便追踪卡支付信息
ALTER TABLE order_base ADD pay_card_num VARCHAR(32) NULL COMMENT '支付方式为充值卡支付时的卡号';

-- 充值卡充值订单
create table if not exists user_charge_card_order
(
  order_no varchar(32) not null comment '充值单号'
    primary key,
  ctime datetime not null comment '创建时间',
  utime datetime null comment '更新时间',
  pay_time datetime null comment '支付时间',
  card_num varchar(32) not null comment '充值卡号',
  money double(12,2) default '0.00' not null comment '充值金额',
  pay_type int not null comment '支付方式',
  status int not null comment '充值订单状态',
  username varchar(32) null comment '充值人姓名',
  user_id int null comment '充值人id',
  constraint user_charge_card_order_order_no_uindex
  unique (order_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci comment '充值卡充值订单';

-- 充值卡操作记录
create table if not exists user_charge_card_operation_record
(
  id int auto_increment comment '主键'
    primary key,
  ctime datetime not null comment '创建时间',
  card_num varchar(32) not null comment '充值卡号',
  operate_type varchar(32) not null comment '操作类型,ENABLE和DISABLE',
  remark varchar(256) null comment '操作说明',
  ip varchar(32) null comment '访问ip',
  sys_user_id int not null comment '创建人',
  sys_user_name varchar(32) not null comment '创建人名称',
  constraint user_charge_card_operation_record_id_uindex
  unique (id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci comment '充值卡操作记录(启用/禁用)';
