create table if not exists `user_wallet_use_record`(
 `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `wallet_type` INT(1) NOT NULL COMMENT '钱包类型，1，余额，2，押金',
  `wallet_name` VARCHAR(20) NOT NULL COMMENT '钱包名称,枚举中获取值',
    `fee` DOUBLE(12,2) NOT NULL COMMENT '操作金额',
  `balance` DOUBLE(12,2) NOT NULL DEFAULT 0  COMMENT ' 余额',
   `operation_type` INT(1) NOT NULL COMMENT '操作类型：1,充值，2,消费',
  `username` VARCHAR(64) NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`),
  KEY idx_username(`username`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户钱包操作记录表';

CREATE TABLE  IF NOT EXISTS `user_wallet`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `wallet_type` INT(1) NOT NULL COMMENT '钱包类型，1，余额，2，押金',
  `wallet_name` VARCHAR(20) NOT NULL COMMENT '钱包名称,枚举中获取值',
  `money` DOUBLE(12,2) NOT NULL DEFAULT 0  COMMENT '钱数',
  `username` VARCHAR(64) NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`),
  KEY idx_username(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户钱包表';

ALTER TABLE `order_base`
ADD COLUMN `is_deleted` INT(1) NULL COMMENT '是否删除：0,不删除,1, 删除' ,
CHANGE COLUMN `pay_type` `pay_type` INT(2) NULL DEFAULT '1' COMMENT '支付类型:1,公众号支付;2,微信APP支付;3,支付宝支付;4,充值卡支付,5,余额' ;



create table if not exists `user_wallet_charge_order`(
 `charge_order_no` VARCHAR(20) NOT NULL   COMMENT '主键,充值单号',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `wallet_id` INT(1) NOT NULL COMMENT '钱包ID',
    `fee` DOUBLE(12,2) NOT NULL COMMENT '操作金额',
  `balance` DOUBLE(12,2) NOT NULL COMMENT ' 余额',
   `status` INT(1) NOT NULL COMMENT '充值状态：1：创建，2：支付中，3：支付完成，4：支付失败，5：订单完成，6：订单失败',
  `username` VARCHAR(64) NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`charge_order_no`),
  KEY idx_username(`username`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户钱包充值单表';

alter table user_wallet_charge_order change wallet_id wallet_type INT(1) NOT NULL COMMENT '钱包ID';

alter table user_wallet_charge_order alter column balance set default 0.00;

alter table user_wallet_charge_order add column wallet_name VARCHAR(20);

alter table user_wallet_charge_order add column pay_time DATETIME;

alter table user_wallet_charge_order add column pay_type int(2);