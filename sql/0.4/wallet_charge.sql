CREATE TABLE `recharge_money` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `type` int(1) NOT NULL COMMENT '类型 1，固定充值额，2，自定义充值额',
  `charge_money` double(12,2) DEFAULT NULL COMMENT '充值金额',
  `discount_money` double(12,2) DEFAULT NULL COMMENT '赠送金额',
  `rate` decimal(3,2) DEFAULT '0.00' COMMENT '优惠比例',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '创建者id',
  `project_id` int(11) DEFAULT NULL COMMENT '项目id：1艾芙芮  2卡励',
  `sort` int(1) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='充值优惠表'

ALTER TABLE `gizwits_lease`.`user_wallet_use_record`
ADD COLUMN `trade_no` VARCHAR(31) NULL COMMENT '交易号' AFTER `id`;

