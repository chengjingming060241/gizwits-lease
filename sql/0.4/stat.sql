CREATE TABLE `stat_fault_alert_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `ctime` datetime DEFAULT NULL COMMENT '创建时间',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备号',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属用户id',
  `show_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '显示名称',
  `identity_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标志名称',
  `remark` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `count` int(11) DEFAULT '0' COMMENT '统计数量',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
    PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

alter table stat_device_order_widget add column share_order_count int(11) DEFAULT 0;
alter table stat_device_order_widget add column share_order_money double(12,2) DEFAULT 0.00;
