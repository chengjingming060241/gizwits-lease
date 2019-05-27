CREATE TABLE if not EXISTS `device_ext_for_majiang` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备序列号',
  `mode` int(1) DEFAULT NULL COMMENT '游戏模式：1 极速 2静音',
  `game_type` int(1) DEFAULT NULL COMMENT '游戏类型：1标准 2自定义音',
  `game_no` int(3) DEFAULT NULL COMMENT '游戏序号，如果是自定义时为空',
  `command` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下发指令',
  PRIMARY KEY (`id`),
  KEY `sno_index` (`sno`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备扩展表(麻将机)';