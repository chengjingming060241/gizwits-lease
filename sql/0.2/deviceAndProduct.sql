CREATE TABLE `device_launch_area` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '投放点名称',
  `province` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '省',
  `city` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '市',
  `area` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '区/县',
  `address` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '详细地址',
  `sys_user_id` int(11) NOT NULL COMMENT '创建者id',
  `sys_user_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人姓名',
  `operator_id` int(13) DEFAULT NULL COMMENT '运营商',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '运营商名称',
  `maintainer_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT ' 维护人员姓名',
  `maintainer_id` int(11) NOT NULL COMMENT '维护人员id',
  `longitude` decimal(19,2) NOT NULL COMMENT '经度',
  `latitude` decimal(19,2) NOT NULL COMMENT '纬度',
  `person_in_charge` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '负责人姓名',
  `person_in_charge_id` int(11) DEFAULT NULL COMMENT '负责人id',
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '负责人电话',
  `status` int(11) DEFAULT '1' COMMENT '状态：0:禁用，1:启用',
  PRIMARY KEY (`id`),
  KEY `idx_operator` (`operator_id`),
  KEY `idx_name` (`name`),
  KEY `idx_sys_user_id` (`sys_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='设备投放点表'


CREATE TABLE `china_area` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '省市县的名字',
  `code` int(6) unsigned NOT NULL DEFAULT '0' COMMENT '行政区编码',
  `parent_code` int(6) unsigned NOT NULL DEFAULT '0',
  `parent_name` varchar(20) DEFAULT '' COMMENT '所属父级的中文名称',
  `is_leaf` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_parent_code` (`parent_code`)
) ENGINE=InnoDB AUTO_INCREMENT=7028 DEFAULT CHARSET=utf8 COMMENT='全国省市行政编码表'


CREATE TABLE `product_service_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ctime` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '修改时间',
  `service_mode_id` int(11) NOT NULL COMMENT '收费模式的id',
  `product_id` int(11) NOT NULL COMMENT '产品的id',
  `service_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收费类型',
  `service_type_id` int(11) DEFAULT NULL COMMENT '收费类型的id',
  `price` double(8,2) NOT NULL COMMENT '单价',
  `num` int(11) NOT NULL COMMENT '数量',
  `unit` varchar(420) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位',
  `status` int(11) DEFAULT '1' COMMENT '状态：0：启用，1:禁用',
  `sys_user_id` int(11) NOT NULL COMMENT '系统用户的id，创建者',
  `sys_user_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人姓名',
  `command` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模式相应的指令',
  PRIMARY KEY (`id`),
  KEY `index2` (`service_mode_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品服务详情表'


CREATE TABLE `product_service_mode` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务方式名称',
  `unit` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单位',
  `product_id` int(11) NOT NULL COMMENT '所属产品ID',
  `service_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '服务类型',
  `sys_user_id` int(11) NOT NULL COMMENT '创建人id',
  `sys_user_name` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人姓名',
  `status` int(11) DEFAULT '1' COMMENT '状态：0:禁用，1:启用',
  `service_type_id` int(11) DEFAULT NULL COMMENT '服务类型ID对应product_command_config的ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品(或者设备)服务方式'


CREATE TABLE `product_data_point` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `show_name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '显示名称',
  `identity_name` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '标志名称',
  `read_write_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '读写类型',
  `data_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '数据类型',
  `remark` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `is_monit` int(1) NOT NULL DEFAULT '1' COMMENT '监控数据点: 0,不监控;1,监控',
  `device_alarm_rank` int(1) DEFAULT NULL COMMENT '1:级别1；2:级别2；3:级别3...',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_identity` (`identity_name`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品数据点'

ALTER TABLE `device_launch_area`
CHANGE COLUMN `maintainer_name` `maintainer_name` VARCHAR(20) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NULL COMMENT ' 维护人员姓名' ,
CHANGE COLUMN `maintainer_id` `maintainer_id` INT(11) NULL COMMENT '维护人员id',
ADD COLUMN `is_deleted` INT(11) NULL DEFAULT 0 COMMENT '是否删除：0 否，1 是' AFTER `status`;

