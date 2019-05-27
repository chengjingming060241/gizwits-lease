CREATE TABLE `order_ext_port` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `order_no` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备sno',
  `port` int(1) DEFAULT NULL COMMENT '出水口号',
  PRIMARY KEY (`id`),
  KEY `sno_index` (`sno`),
  KEY `order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单扩展表';