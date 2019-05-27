CREATE TABLE `stat_device_hour` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `zero` int(11) DEFAULT '0' COMMENT '0点设备数量',
  `ONE` int(11) DEFAULT '0' COMMENT '1点设备数量',
  `two` int(11) DEFAULT '0' COMMENT '2点设备数量',
  `three` int(11) DEFAULT '0' COMMENT '3点设备数量',
  `four` int(11) DEFAULT '0' COMMENT '4点设备数量',
  `five` int(11) DEFAULT '0' COMMENT '5点设备数量',
  `six` int(11) DEFAULT '0' COMMENT '6点设备数量',
  `seven` int(11) DEFAULT '0' COMMENT '7点设备数量',
  `eight` int(11) DEFAULT '0' COMMENT '8点设备数量',
  `nine` int(11) DEFAULT '0' COMMENT '9点设备数量',
  `ten` int(11) DEFAULT '0' COMMENT '10点设备数量',
  `eleven` int(11) DEFAULT '0' COMMENT '11点设备数量',
  `twelve` int(11) DEFAULT '0' COMMENT '12点设备数量',
  `thriteen` int(11) DEFAULT '0' COMMENT '13点设备数量',
  `fourteen` int(11) DEFAULT '0' COMMENT '14点设备数量',
  `fifteen` int(11) DEFAULT '0' COMMENT '15点设备数量',
  `siteen` int(11) DEFAULT '0' COMMENT '16点设备数量',
  `seventeen` int(11) DEFAULT '0' COMMENT '17点设备数量',
  `eighteen` int(11) DEFAULT '0' COMMENT '18点设备数量',
  `nineteen` int(11) DEFAULT '0' COMMENT '19点设备数量',
  `twenty` int(11) DEFAULT '0' COMMENT '20点设备数量',
  `twenty_one` int(11) DEFAULT '0' COMMENT '21点设备数量',
  `twenty_two` int(11) DEFAULT '0' COMMENT '22点设备数量',
  `twenty_three` int(11) DEFAULT '0' COMMENT '23点设备数量',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备在线时段分析统计表';


CREATE TABLE `stat_device_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `province_id` int(11) DEFAULT NULL COMMENT '省id',
  `province` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省名称',
  `device_count` int(11) DEFAULT '0' COMMENT '省对应的用户数量',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `proportion` double(12,2) DEFAULT '0.00' COMMENT '设备占比',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备地图分布统计表';

CREATE TABLE `stat_device_order_widget` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `product_id` int(11) DEFAULT NULL COMMENT '产品id',
  `total_count` int(11) DEFAULT '0' COMMENT '当前设备总数',
  `new_count` int(11) DEFAULT '0' COMMENT '今日设备新增数',
  `ordered_percent` double(12,2) DEFAULT '0.00' COMMENT '设备订单率（昨天下单设备数-今天下单设备数）/今天总',
  `alarm_count` int(11) DEFAULT '0' COMMENT '当前设备故障数',
  `warn_count` int(11) DEFAULT '0' COMMENT '当前设备警告数',
  `warn_record` int(11) DEFAULT '0' COMMENT '今日告警记录',
  `alarm_percent` double(12,2) DEFAULT '0.00' COMMENT '当前设备故障率',
  `order_count_today` int(11) DEFAULT '0' COMMENT '今日订单数量',
  `order_count_yesterday` int(11) DEFAULT '0' COMMENT '昨日订单数量',
  `order_new_percent_yesterday` double(12,2) DEFAULT '0.00' COMMENT '昨天订单新增率',
  `order_count_month` int(11) DEFAULT '0' COMMENT '本月订单数',
  `order_count_before_yesterday` int(11) DEFAULT NULL COMMENT '前天订单数',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_utime` (`utime`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备订单看板数据统计表';

CREATE TABLE `stat_device_trend` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `sys_user_id` int(11) DEFAULT '0' COMMENT '归属系统用户id',
  `new_count` int(11) DEFAULT '0' COMMENT '新增上线数量',
  `ordered_percent` double(12,2) DEFAULT '0.00' COMMENT '设备订单率',
  `ordered_count` int(11) DEFAULT '0' COMMENT '含有订单的设备数',
  `product_id` int(11) DEFAULT NULL COMMENT '对应的产品id',
  `active_count` int(11) DEFAULT '0' COMMENT '设备活跃数',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备趋势统计表';

CREATE TABLE `stat_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备序列号',
  `operator_id` int(11) DEFAULT NULL COMMENT '运营商id',
  `order_amount` double(12,2) DEFAULT NULL COMMENT '订单总金额',
  `order_count` int(11) DEFAULT NULL COMMENT '订单数量',
  `ordered_percent` double(12,2) DEFAULT NULL COMMENT '订单增长率',
  PRIMARY KEY (`id`),
  KEY `idx_operator_id` (`operator_id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_sno` (`sno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单分析统计表';

CREATE TABLE `stat_user_hour` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `zero` int(11) DEFAULT '0' COMMENT '0点用户控制设备数量',
  `ONE` int(11) DEFAULT '0' COMMENT '1点用户控制设备数量',
  `two` int(11) DEFAULT '0' COMMENT '2点用户控制设备数量',
  `three` int(11) DEFAULT '0' COMMENT '3点用户控制设备数量',
  `four` int(11) DEFAULT '0' COMMENT '4点用户控制设备数量',
  `five` int(11) DEFAULT '0' COMMENT '5点用户控制设备数量',
  `six` int(11) DEFAULT '0' COMMENT '6点用户控制设备数量',
  `seven` int(11) DEFAULT '0' COMMENT '7点用户控制设备数量',
  `eight` int(11) DEFAULT '0' COMMENT '8点用户控制设备数量',
  `nine` int(11) DEFAULT '0' COMMENT '9点用户控制设备数量',
  `ten` int(11) DEFAULT '0' COMMENT '10点用户控制设备数量',
  `eleven` int(11) DEFAULT '0' COMMENT '11点用户控制设备数量',
  `twelve` int(11) DEFAULT '0' COMMENT '12点用户控制设备数量',
  `thriteen` int(11) DEFAULT '0' COMMENT '13点用户控制设备数量',
  `fourteen` int(11) DEFAULT '0' COMMENT '14点用户控制设备数量',
  `fifteen` int(11) DEFAULT '0' COMMENT '15点用户控制设备数量',
  `siteen` int(11) DEFAULT '0' COMMENT '16点用户控制设备数量',
  `seventeen` int(11) DEFAULT '0' COMMENT '17点用户控制设备数量',
  `eighteen` int(11) DEFAULT '0' COMMENT '18点用户控制设备数量',
  `nineteen` int(11) DEFAULT '0' COMMENT '19点用户控制设备数量',
  `twenty` int(11) DEFAULT '0' COMMENT '20点用户控制设备数量',
  `twenty_one` int(11) DEFAULT '0' COMMENT '21点用户控制设备数量',
  `twenty_two` int(11) DEFAULT '0' COMMENT '22点用户控制设备数量',
  `twenty_three` int(11) DEFAULT '0' COMMENT '23点用户控制设备数量',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='控制设备时段用户统计表';

CREATE TABLE `stat_user_location` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `province_id` int(11) DEFAULT NULL COMMENT '省id',
  `province` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省名称',
  `user_count` int(11) DEFAULT '0' COMMENT '省对应的用户数量',
  `proportion` double(12,2) DEFAULT '0.00' COMMENT '用户百分比',
  PRIMARY KEY (`id`),
  KEY `idx_sys_user_id` (`sys_user_id`),
  KEY `idx_ctime` (`ctime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户地图分布统计表';

CREATE TABLE `stat_user_trend` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `new_count` int(11) DEFAULT '0' COMMENT '新增用户数量',
  `active_count` int(11) DEFAULT '0' COMMENT '活跃用户数量',
  `total_count` int(11) DEFAULT '0' COMMENT '用户总数',
  `male` int(11) DEFAULT '0' COMMENT '男性用户数量',
  `female` int(11) DEFAULT '0' COMMENT '女性用户数量',
  `zero` int(11) DEFAULT '0' COMMENT '使用该产品0次用户数量',
  `one_two` int(11) DEFAULT '0' COMMENT '使用该产品1~2次用户数量',
  `three_four` int(11) DEFAULT '0' COMMENT '使用该产品3~4次用户数量',
  `five_six` int(11) DEFAULT '0' COMMENT '使用该产品5~6次用户数量',
  `seven_eight` int(11) DEFAULT '0' COMMENT '使用该产品7~8次用户数量',
  `nine_ten` int(11) DEFAULT '0' COMMENT '使用该产品9~10次用户数量',
  `ten_more` int(11) DEFAULT '0' COMMENT '使用该产品10次及其以上用户数量',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_sys_user_id` (`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户趋势及性别，使用次数统计表';

CREATE TABLE `stat_user_widget` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime NOT NULL COMMENT '修改时间',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '归属系统用户id',
  `total_count` int(11) DEFAULT '0' COMMENT '当前用户总数',
  `new_percent` double(12,2) DEFAULT '0.00' COMMENT '昨天用户增长率',
  `active_count` int(11) DEFAULT '0' COMMENT '今日活跃用户数',
  `active_percent` double(12,2) DEFAULT '0.00' COMMENT '昨天用户活跃率',
  PRIMARY KEY (`id`),
  KEY `idx_ctime` (`ctime`),
  KEY `idx_utime` (`utime`),
  KEY `idx_sys_user_id` (`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备订单看板数据统计表';

