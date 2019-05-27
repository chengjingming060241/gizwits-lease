CREATE TABLE `device_ext_port` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备sno',
  `mac` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'MAC地址,通讯用',
  `port` int(1) DEFAULT NULL COMMENT '出水口号',
  `port_type` int(1) DEFAULT NULL COMMENT '出水类型：1常温，2热水，3冰水',
  `status` int(1) DEFAULT NULL COMMENT '状态：3使用中  4空闲',
  `sort` int(1) DEFAULT NULL COMMENT '排序字段',
  PRIMARY KEY (`id`),
  KEY `sno_index` (`sno`),
  KEY `port_index` (`port`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备扩展表';