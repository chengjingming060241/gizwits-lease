ALTER TABLE `user_wallet`
ADD COLUMN `discount_money` DOUBLE(11,2) NULL DEFAULT 0.00 COMMENT '用户的充值赠送金额,此部分的金额说明money中有多少金额是优惠金额' ;

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


insert  into `recharge_money`(`id`,`ctime`,`utime`,`type`,`charge_money`,`discount_money`,`rate`,`sys_user_id`,`project_id`,`sort`)
values (1,'2017-08-16 20:40:23',NULL,1,10.00,0.21,NULL,NULL,2,1),(2,'2017-08-16 20:40:23',NULL,1,40.00,0.21,NULL,NULL,2,2),(3,'2017-08-16 20:40:23',NULL,1,80.00,0.21,NULL,NULL,2,3),(4,'2017-08-16 20:40:23',NULL,1,120.00,0.21,NULL,NULL,2,4),(5,'2017-08-16 20:40:23',NULL,2,NULL,NULL,'0.05',NULL,2,NULL),(6,'2017-08-16 20:40:23',NULL,1,10.00,15.00,NULL,NULL,1,1),(7,'2017-08-16 20:40:23',NULL,1,20.00,30.00,NULL,NULL,1,2),(8,'2017-08-16 20:40:23',NULL,1,30.00,45.00,NULL,NULL,1,3),(9,'2017-08-16 20:40:23',NULL,1,40.00,60.00,NULL,NULL,1,4),(10,'2017-08-16 20:40:23',NULL,1,50.00,75.00,NULL,NULL,1,5),(11,'2017-08-16 20:40:23',NULL,1,100.00,130.00,NULL,NULL,1,6);
