create table if not exists  `sys_message_to_user` (
 `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
   `sys_message_id` int(11) DEFAULT NULL COMMENT '消息系统id',
 `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户名称',
   `role_id` int(11) DEFAULT NULL,
  `role_name` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `is_read` int(1) NOT NULL DEFAULT '0' COMMENT '是否已读：0 未读，1已读',
  `is_send` int(1) NOT NULL DEFAULT '1' COMMENT '是否发送：0，为发送，1，已发送',
  PRIMARY KEY (`id`),
  KEY `user_id_index` (`user_id`),
  KEY `role_id_index` (`role_id`)
)ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统消息用户表';

ALTER TABLE `gizwits_lease`.`sys_message`
DROP COLUMN `recipient_name`,
DROP COLUMN `recipient_id`,
DROP COLUMN `mobile`,
DROP INDEX `recipient_id_index` ;

ALTER TABLE `gizwits_lease`.`sys_message`
ADD COLUMN `is_send` INT(1) NOT NULL DEFAULT 1 COMMENT '是否发送：0，为发送，1，已发送' AFTER `is_read`,
ADD COLUMN `is_bind_wx` INT(1) NULL COMMENT '是否绑定微信' AFTER `is_send`;


ALTER TABLE `gizwits_lease`.`user_wallet_charge_order`
ADD COLUMN `discount_money` DOUBLE(12,2) NULL COMMENT '优惠金额' AFTER `fee`,
ADD COLUMN `pay_type` INT(2) NULL COMMENT '支付类型:1,公众号支付;2,微信APP支付;3,支付宝支付;4,充值卡支付,5,余额支付' AFTER `pay_time`;


ALTER TABLE `gizwits_lease`.`user_wallet_use_record`
ADD COLUMN `discount_money` DOUBLE(12,2) NULL COMMENT '优惠金额' AFTER `fee`;


