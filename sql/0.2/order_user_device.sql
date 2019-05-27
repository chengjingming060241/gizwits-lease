ALTER TABLE `product`
ADD COLUMN `qrcode_type` VARCHAR (10) NOT NULL DEFAULT 'WEB' COMMENT '生成二维码的方式:WEB,网页链接;WEIXIN,微信硬件;' ,
ADD COLUMN `location_type` VARCHAR (10) NOT NULL DEFAULT 'GIZWITS' COMMENT '获取设备坐标方式:GIZWITS,机智云接口;GD,高德接口(需要相关数据点支撑)'

ALTER TABLE `product_data_point`
ADD COLUMN `is_monit` INT(1) NOT NULL DEFAULT 1 COMMENT '监控数据点: 0,不监控;1,监控';

ALTER TABLE `product_service_mode`
ADD COLUMN `service_type_id`INT(11) NOT NULL COMMENT  '服务类型ID对应product_command_config的ID';

CREATE TABLE  IF NOT EXISTS `device`(
  `sno` VARCHAR(64) NOT NULL  COMMENT '设备序列号',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(20) NOT NULL COMMENT '设备名称',
  `mac` VARCHAR(32) NOT NULL COMMENT 'MAC地址,通讯用',
  `work_status` INT(1) NOT NULL COMMENT '设备在线状态,0:离线,1:空闲 2:使用中 3:故障中 4:禁用 ',
  `status` INT(1)NOT NULL COMMENT '设备状态,0:入库 1:出库 2:服务中 3:暂停服务 4:已返厂 5:已报废',
  `longitude` DECIMAL(19,2) COMMENT '经度',
  `latitude` DECIMAL(19,2) COMMENT '维度',
  `last_online_time` DATETIME COMMENT '最后上线时间',
  `operator_id` INT(11) COMMENT '运营商ID',
  `operator_name` VARCHAR(50) COMMENT '运营商名称',
  `launch_area_id` INT(11) COMMENT '投放点ID',
  `launch_area_name` VARCHAR(50) COMMENT '投放点名称',
  `product_id` INT(11) NOT NULL COMMENT '所属产品',
  `product_name` VARCHAR(50) COMMENT '所属产品名称',
  `is_deleted` INT(1) default '0' null comment '删除标识，0：未删除，1：已删除',
  PRIMARY KEY (`sno`),
  UNIQUE KEY idx_mac(`mac`),
  KEY idx_status(`status`),
  KEY idx_operator_id(`operator_id`),
  KEY idx_product_id(`product_id`),
  KEY idx_launch_area(`launch_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备表';
ALTER TABLE `device`
ADD COLUMN `fault_status` INT(1) NOT NULL COMMENT '设备故障状态 ',
ADD COLUMN `online_status` INT(1) NOT NULL COMMENT '设备在线状态,0:离线,1:空闲 2:使用中 3:故障中 4:禁用 ',
ADD COLUMN `service_id` INT(11) COMMENT '收费模式ID',
ADD COLUMN `service_name` VARCHAR(50) COMMENT '收费模式名称',
ADD COLUMN `giz_did` VARCHAR(50) COMMENT '机智云设备did',
ADD COLUMN `giz_pass_code` VARCHAR(50) COMMENT '机智云设备passcode',
ADD COLUMN `agent_id` INT(11) COMMENT '代理商id',
ADD COLUMN `sys_user_id` INT(11) NOT NULL COMMENT '创建者id';


CREATE TABLE  IF NOT EXISTS `user`(
  `username` VARCHAR(64) NOT NULL COMMENT '主键,用户名',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `nickname` VARCHAR (64) COMMENT '别名',
  `openid` VARCHAR(128) COMMENT '第三方开发平台ID',
  `third_party` INT(2) COMMENT '第三方平台,1:微信 2:支付宝 3:百度 4:新浪',
  `password` VARCHAR(64) NOT NULL COMMENT '密码',
  `mobile` VARCHAR(11)  COMMENT '手机,绑定时程序上控制唯一',
  `email` VARCHAR(128)  COMMENT '电子邮件,绑定时程序上控制唯一',
  `gender` VARCHAR(10) COMMENT '性别',
  `avatar` VARCHAR(300) COMMENT '头像地址',
  `birthday` DATETIME COMMENT '生日',
  `province` VARCHAR(20) COMMENT '所属省份',
  `city` VARCHAR(20) COMMENT '所属城市',
  `address` VARCHAR(50) COMMENT '详细地址',
  `remark` VARCHAR(255) COMMENT '备注',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户表,不要前缀,因为用户模块计划抽象成通用功能';

ALTER TABLE `user`
ADD COLUMN `sys_user_id` INT(11)  COMMENT '微信用户所属运营商用户ID',
ADD COLUMN `wx_id` VARCHAR(20) COMMENT '微信ID',
ADD COLUMN `status` INT(2) NOT NULL DEFAULT 0 COMMENT '用户状态,0:正常 1:黑名单 ';


CREATE TABLE IF NOT EXISTS `product_command_config` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL COMMENT '产品ID',
  `command_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '指令类型：SERVICE,收费类型指令；CONTROL,控制类型指令；STATUS,状态类型指令',
  `status_command_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '状态指令类型,只有在command_type为STATUS时有值：FREE,空闲指令；USING,使用中指令；FINISH,设备使用完成指令',
  `name` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '指令名称',
  `command` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '下发指令',
  `is_show` int(1) NOT NULL DEFAULT '1' COMMENT '是否在后台展示：0,不展示；1,展示',
  `ctime` datetime NOT NULL,
  `utime` datetime DEFAULT NULL,
  `is_deleted` int(1) DEFAULT '0' COMMENT '是否删除：0,否；1,删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品指令配置表';

CREATE TABLE  IF NOT EXISTS `device_alarm`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL COMMENT '故障名称',
  `attr` VARCHAR(100) NOT NULL COMMENT '故障参数',
  `happen_time` DATETIME COMMENT '故障发生时间',
  `fixed_time` DATETIME  COMMENT '故障修复时间',
  `status` INT(1) NOT NULL COMMENT '故障状态,0:未修复 1:已修复',
  `mac` VARCHAR(32) NOT NULL COMMENT 'MAC地址',
  `longitude` DECIMAL(19,2) COMMENT '经度',
  `latitude` DECIMAL(19,2) COMMENT '维度',
  `notify_user_id` INT(11)  COMMENT '需要通知的人员ID',
  `sno` VARCHAR(64) NOT NULL  COMMENT '对应设备序列号',
  PRIMARY KEY (`id`),
  KEY idx_sno (`sno`),
  KEY idx_mac(`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备故障(警告)记录表';

CREATE TABLE  IF NOT EXISTS `device_running_record`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `sno` VARCHAR(64) NOT NULL  COMMENT '对应设备序列号',
  `mac` VARCHAR(32) NOT NULL COMMENT 'MAC地址',
  `work_status` INT(1) NOT NULL COMMENT '设备在线状态,1:在线,2:离线 3:使用中 4:空闲 5:禁用 6:故障 ',
  `content` VARCHAR(2000) NOT NULL COMMENT '报文内容',
  PRIMARY KEY (`id`),
  KEY idx_sno (`sno`),
  KEY idx_mac(`mac`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备运行记录表';

CREATE TABLE  IF NOT EXISTS `order_base`(
  `order_no` VARCHAR(20) NOT NULL   COMMENT '主键,订单号,按照一定规则生成',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `sno` VARCHAR(64) NOT NULL  COMMENT '对应设备序列号',
  `mac` VARCHAR(64) NOT NULL  COMMENT '对应设备MAC',
  `command` VARCHAR(128) NOT NULL COMMENT '发货指令',
  `service_mode_id` INT(11) NOT NULL COMMENT '服务方式ID',
  `service_mode_name` VARCHAR(50) NOT NULL COMMENT '服务方名称',
  `order_status` INT(2) NOT NULL COMMENT '订单状态,0:创建 1:支付中 2:支付完成 3:服务中 4:订单完成 5:订单失败',
  `pay_time` DATETIME COMMENT '订单支付时间',
  `pay_type` int(2) NOT NULL DEFAULT 1 COMMENT '支付类型:1,公众号支付;2,微信APP支付;3,支付宝支付;4,充值卡支付',
  `trade_no` VARCHAR (32) COMMENT '支付订单号',
  `transaction_id` VARCHAR (32) COMMENT '支付系统生成的交易号,用于退款处理',
  `amount` DOUBLE(12,2) NOT NULL DEFAULT 0.00 COMMENT '订单总价',
  `user_id` VARCHAR (32) NOT NULL  COMMENT '用户ID',
  `user_name` VARCHAR(50) NOT NULL COMMENT '用户姓名',
  `sys_user_id` INT(11) NOT NULL COMMENT '订单所属的微信运营配置中的sys_user_id',
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '订单表';

CREATE TABLE `order_pay_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(20)  NOT NULL COMMENT '订单号',
  `ctime` datetime NOT NULL,
  `utime` DATETIME COMMENT '更新时间',
  `pay_type` int(2) NOT NULL COMMENT '支付类型:1,公众号支付;2,微信APP支付;3,支付宝支付;4,充值卡支付',
  `params` varchar(1000) DEFAULT NULL COMMENT '支付提交参数',
  `status` int(2) DEFAULT '1' COMMENT '订单状态,0:创建 1:支付中 2:支付完成 3:服务中 4:订单完成 5:订单失败',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '订单支付记录表';

CREATE TABLE `order_status_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增',
  `order_no` INT(11) NOT NULL COMMENT '订单号',
  `pre_status` INT(2) DEFAULT NULL COMMENT '订单前置状态',
  `now_status` INT(2) NOT NULL COMMENT '当前状态',
  `ctime` datetime NOT NULL,
  `creatorId` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作者',
  `remark` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '订单状态流转表';
