CREATE TABLE  IF NOT EXISTS `brand`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL  COMMENT '品牌名称',
  `brand_serial_number` VARCHAR(50) COMMENT '品牌序列号',
  `logo_url` VARCHAR(150) COMMENT '品牌logo地址',
  `introduce` VARCHAR(256) COMMENT '品牌介绍',
  `create_time` DATETIME COMMENT '品牌创建时间',
  `manufacturer_id` INT(11) COMMENT '所属厂商(企业)',
  `manufacturer_name` VARCHAR(50)  COMMENT '企业名称',
  PRIMARY KEY (`id`),
  KEY idx_sn(`brand_serial_number`),
  KEY idx_name(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '品牌表';


CREATE TABLE  IF NOT EXISTS `manufacturer`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL COMMENT '企业名称',
  `industry` VARCHAR(50) COMMENT '所属行业',
  `web_site` VARCHAR(100) COMMENT '公司官网',
  `sub_domain` VARCHAR(50) COMMENT '子域名',
  `logo_url` VARCHAR(150) COMMENT '公司logo url',
  `phone` VARCHAR(20) COMMENT '企业电话',
  `mobile` VARCHAR(11) COMMENT '手机号码',
  `contact` VARCHAR(20) COMMENT '联系人',
  `department` VARCHAR(50) COMMENT '部门',
  `email` VARCHAR(100) COMMENT '电子邮件',
  `qq` VARCHAR(20) COMMENT 'QQ号码',
  `province` VARCHAR(50) COMMENT '省',
  `city` VARCHAR(20) COMMENT '市',
  `area` VARCHAR(20) COMMENT '区/县',
  `address` VARCHAR(200) COMMENT '详细地址',
  `parent_manufacturer_id` INT(11) COMMENT '父级企业ID',
  `sys_user_id` INT(11) NOT NULL COMMENT '对应系统用户ID',
  `enterprise_id` VARCHAR(32) NOT NULL COMMENT '机智云里产品对应的企业id',
  `enterprise_secret` VARCHAR(32) NOT NULL COMMENT '机智云平台产品对应的企业secret',
  PRIMARY KEY (`id`),
  KEY  idx_name(`name`),
  KEY  idx_contact(`contact`),
  UNIQUE KEY idx_sys_user(`sys_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '厂商(或企业)表';


CREATE TABLE  IF NOT EXISTS `agent`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL COMMENT '代理商名称',
  `industry` VARCHAR(50) COMMENT '所属行业',
  `web_site` VARCHAR(100) COMMENT '公司官网',
  `logo_url` VARCHAR(150) COMMENT '代理商logo url',
  `phone` VARCHAR(20) COMMENT '企业电话',
  `mobile` VARCHAR(11) COMMENT '手机号码',
  `contact` VARCHAR(20) COMMENT '联系人',
  `department` VARCHAR(50) COMMENT '部门',
  `email` VARCHAR(100) COMMENT '电子邮件',
  `qq` VARCHAR(20) COMMENT 'QQ号码',
  `province` VARCHAR(50) COMMENT '省',
  `city` VARCHAR(20) COMMENT '市',
  `area` VARCHAR(20) COMMENT '区/县',
  `address` VARCHAR(200) COMMENT '详细地址',
  `parent_agent_id` INT(11) COMMENT '父级代理商ID',
  `sys_user_id` INT(11) NOT NULL COMMENT '对应系统用户ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY idx_sys_user(`sys_user_id`)
  -- 索引按照 查询情况进行添加
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '代理商表';


CREATE TABLE  IF NOT EXISTS `operator`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL COMMENT '运营商名称',
  `industry` VARCHAR(50) COMMENT '所属行业',
  `web_site` VARCHAR(100) COMMENT '公司官网',
  `logo_url` VARCHAR(150) COMMENT '代理商logo url',
  `phone` VARCHAR(20) COMMENT '企业电话',
  `mobile` VARCHAR(11) COMMENT '手机号码',
  `contact` VARCHAR(20) COMMENT '联系人',
  `department` VARCHAR(50) COMMENT '部门',
  `email` VARCHAR(100) COMMENT '电子邮件',
  `qq` VARCHAR(20) COMMENT 'QQ号码',
  `province` VARCHAR(50) COMMENT '省',
  `city` VARCHAR(20) COMMENT '市',
  `area` VARCHAR(20) COMMENT '区/县',
  `address` VARCHAR(200) COMMENT '详细地址',
  `manufacturer_id` INT(11) NOT NULL COMMENT  '所属厂商,厂商可以直接创建运营商',
  `parent_operator_id` INT(11) COMMENT '父级运营商ID',
  `sys_user_id` INT(11) NOT NULL COMMENT '对应系统用户ID,运营商可以登录租赁平台',
  `share_benefit_rule_id` INT(11) NOT NULL COMMENT '分润规则',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '运营商表';


CREATE TABLE  IF NOT EXISTS `product`(
    `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
    `ctime` DATETIME NOT NULL COMMENT '添加时间',
    `utime` DATETIME COMMENT '更新时间',
    `name` VARCHAR(50) NOT NULL  COMMENT '产品名称',
    `img_url` VARCHAR(500) COMMENT '产品图片地址',
    `gizwits_product_key` VARCHAR(32) NOT NULL COMMENT '机智云产品key',
    `gizwits_product_secret` VARCHAR(32) NOT NULL COMMENT '机智云产品secret',
    `status` INT(2) NOT NULL DEFAULT 1 COMMENT '产品状态,1:启用, 0:禁用',
    `category_id` INT(5) NOT NULL COMMENT '产品类型',
    `category_name` VARCHAR(30) NOT NULL COMMENT '产品类型名称',
    `manufacturer_id` INT(11) NOT NULL COMMENT  '所属厂商',
    `brand_id` INT(11) NOT NULL COMMENT '所属品牌',
    `communicate_type` VARCHAR(32) COMMENT '通讯方式',
    `sys_user_id` INT(11) NOT NULL COMMENT '创建人',
    `is_deleted` INT(1) NOT NULL DEFAULT 0 COMMENT '删除状态，0:未删除,1:已删除',
    `qrcode_type` VARCHAR (10) NOT NULL DEFAULT 'WEB' COMMENT '生成二维码的方式:WEB,网页链接;WEIXIN,微信硬件;' ,
    `location_type` VARCHAR (10) NOT NULL DEFAULT 'GIZWITS' COMMENT '获取设备坐标方式:GIZWITS,机智云接口;GD,高德接口(需要相关数据点支撑)'
    PRIMARY KEY (`id`),
    KEY idx_category(`category_id`),
    KEY idx_product_key(`gizwits_product_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品表';


CREATE TABLE  IF NOT EXISTS `product_properties`(
    `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
    `ctime` DATETIME NOT NULL COMMENT '添加时间',
    `utime` DATETIME COMMENT '更新时间',
    `property_key` VARCHAR(50) NOT NULL  COMMENT '属性key',
    `property_name` VARCHAR(50) NOT NULL COMMENT '属性名称',
    `tips` VARCHAR(50) COMMENT '提示语',
    `category_id` INT(5) NOT NULL COMMENT '产品类型',
    `category_name` VARCHAR(30) NOT NULL COMMENT '产品类型名称',
    `is_not_null` INT(1) NOT NULL COMMENT '是否必填,1:是 0:否',
    `is_select_value` INT(1) NOT NULL COMMENT '是否选择值,1:是,选择 0:否,填写',
    PRIMARY KEY (`id`),
    KEY idx_category(`category_id`),
    UNIQUE KEY idx_property_key(`property_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品属性定义表';

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

CREATE TABLE  IF NOT EXISTS `product_properties_option_value`(
    `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
    `ctime` DATETIME NOT NULL COMMENT '添加时间',
    `utime` DATETIME COMMENT '更新时间',
    `property_key` VARCHAR(50) NOT NULL  COMMENT '属性key',
    `property_name` VARCHAR(50) NOT NULL COMMENT '属性名称',
    `property_value` VARCHAR(50) NOT NULL COMMENT '属性值',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品属性值表,提供选择';


CREATE TABLE  IF NOT EXISTS `product_to_properties`(
    `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
    `ctime` DATETIME NOT NULL COMMENT '添加时间',
    `utime` DATETIME COMMENT '更新时间',
    `product_id` INT(11) NOT NULL  COMMENT '产品ID',
    `product_name` VARCHAR(50) NOT NULL  COMMENT '产品名称',
    `property_id` INT(11) NOT NULL  COMMENT '属性id',
    `property_key` VARCHAR(50) NOT NULL  COMMENT '属性key',
    `property_name` VARCHAR(50) NOT NULL COMMENT '属性名称',
    `property_value` VARCHAR(256) COMMENT '属性值',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品属性关系表';


CREATE TABLE  IF NOT EXISTS `product_category`(
  `id` INT(5) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(30) NOT NULL COMMENT '类别名称',
  `remark` VARCHAR(100) COMMENT '描述',
  `parent_category_id` INT(5) COMMENT '父级类别ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品类型';



-- ==========================================



CREATE TABLE  IF NOT EXISTS `product_data_point`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `show_name` VARCHAR(32) NOT NULL COMMENT '显示名称',
  `identity_name` VARCHAR(32) NOT NULL COMMENT '标志名称',
  `read_write_type` VARCHAR(32) NOT NULL COMMENT '读写类型',
  `data_type` VARCHAR(32) NOT NULL COMMENT '数据类型',
  `remark` VARCHAR(100) COMMENT '备注',
  `is_monit` INT(1) NOT NULL DEFAULT 1 COMMENT '监控数据点: 0,不监控;1,监控',
  PRIMARY KEY (`id`),
  UNIQUE KEY  idx_identity(`identity_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品数据点';




CREATE TABLE  IF NOT EXISTS `product_service_mode`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(20) NOT NULL  COMMENT '服务方式名称',
  `price` DOUBLE(8,2) NOT NULL COMMENT '单价',
  `num` DOUBLE(8,2) NOT NULL COMMENT '数量',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位',
  `service_type_id`INT(11) NOT NULL COMMENT  '服务类型ID对应product_command_config的ID', 
  `service_type` VARCHAR(20) NOT NULL COMMENT  '服务类型', 
  `product_id` INT(11) NOT NULL COMMENT '所属产品ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品(或者设备)服务方式';

CREATE TABLE  IF NOT EXISTS `device`(
  `sno` VARCHAR(64) NOT NULL  COMMENT '设备序列号',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(20) NOT NULL COMMENT '设备名称',
  `mac` VARCHAR(32) NOT NULL COMMENT 'MAC地址,通讯用',
  `work_status` INT(1) NOT NULL COMMENT '设备工作状态,2离线,3:使用中,4:空闲 5:禁用 6:故障 7待机',
  `online_status` INT(1) NOT NULL COMMENT '在线状态，1在线，2离线 ',
  `status` INT(1)NOT NULL COMMENT '设备状态,0:入库 1:出库 2:服务中 3:暂停服务 4:已返厂 5:已报废',
  `fault_status` INT(2) NOT NULL DEFAULT 8 COMMENT '设备故障状态，6故障，8正常',
  `longitude` DECIMAL(19,2) COMMENT '经度',
  `latitude` DECIMAL(19,2) COMMENT '维度',
  `last_online_time` DATETIME COMMENT '最后上线时间',
  `operator_id` INT(11) COMMENT '运营商ID',
  `operator_name` VARCHAR(50) COMMENT '运营商名称',
  `service_id` INT(11) COMMENT '收费模式ID',
  `service_name` VARCHAR(50) COMMENT '收费模式名称',
  `launch_area_id` INT(11) COMMENT '投放点ID',
  `launch_area_name` VARCHAR(50) COMMENT '投放点名称',
  `product_id` INT(11) NOT NULL COMMENT '所属产品',
  `product_name` VARCHAR(50) COMMENT '所属产品名称',
  `is_deleted` INT(1) default '0' null comment '删除标识，0：未删除，1：已删除',
  `giz_did` VARCHAR(50) COMMENT '机智云设备did',
  `giz_pass_code` VARCHAR(50) COMMENT '机智云设备passcode',
  PRIMARY KEY (`sno`),
  UNIQUE KEY idx_mac(`mac`),
  KEY idx_status(`status`),
  KEY idx_operator_id(`operator_id`),
  KEY idx_product_id(`product_id`),
  KEY idx_launch_area(`launch_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备表';

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

CREATE TABLE  IF NOT EXISTS `device_launch_record`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `lauch_time` DATETIME NOT NULL COMMENT '投放时间',
  `sno` VARCHAR(64) NOT NULL  COMMENT '对应设备序列号',
  `lauch_area_id` INT(11) NOT NULL COMMENT '投放点ID',
  `lauch_area_name` VARCHAR(50) NOT NULL COMMENT '投放点名称',
  `operator_id` INT(11) NOT NULL COMMENT '运营商ID',
  `operator_name` VARCHAR(50) NOT NULL COMMENT '运营商名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备投放记录表,记录投放历史';


CREATE TABLE  IF NOT EXISTS `device_launch_area`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL COMMENT '投放点名称',
  `province` VARCHAR(20) NOT NULL COMMENT '省',
  `city` VARCHAR(20) NOT NULL COMMENT '市',
  `area` VARCHAR(30) NOT NULL COMMENT '区/县',
  `address` VARCHAR(256) NOT NULL COMMENT '详细地址',
  `sys_user_id` INT(11)  COMMENT '投放点负责人员',
  `sys_user_name` VARCHAR(20) COMMENT '负责人姓名',
  `operator_id` INT(11) NOT NULL COMMENT '运营商',
  `operator_name` VARCHAR(50) NOT NULL COMMENT '运营商名称',
   `mobile` VARCHAR (11)  COMMENT '投放点负责人电话',
  ‘maintainer_name’ VARCHAR(20) COMMENT '维护人姓名',
   ‘maintainer_id’ INT(11) COMMENT '维护人id',
  `longitude` DECIMAL(19,2) NOT  NULL  COMMENT '经度', 
   `latitude` DECIMAL(19,2) NOT  null COMMENT '维度', 
  PRIMARY KEY (`id`),
  KEY  idx_operator(`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备投放点表';


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

CREATE TABLE  IF NOT EXISTS `order_ext_by_time`(
  `order_no` INT(11) NOT NULL   COMMENT '主键,与订单保持一致',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `start_time` DATETIME NOT NULL COMMENT '服务开始时间',
  `end_time` DATETIME NOT NULL COMMENT '服务结束时间',
  `duration` DOUBLE(5,2) NOT NULL COMMENT '购买时长',
  `price` DOUBLE(6,2) NOT NULL COMMENT '单价,元',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位,分钟/小时/天',
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '订单扩展表(按时)';

CREATE TABLE  IF NOT EXISTS `order_ext_by_quantity`(
  `order_no` INT(11) NOT NULL   COMMENT '主键,与订单保持一致',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `quantity` DOUBLE(7,2) NOT NULL COMMENT '购买的量',
  `price` DOUBLE(6,2) NOT NULL COMMENT '单价,元',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位,立方',
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '订单扩展记录表(按量)';

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

CREATE TABLE  IF NOT EXISTS `sys_wechat_pay`(
  `trade_no` INT(11) NOT NULL   COMMENT '交易号',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `transaction_id` VARCHAR(256) COMMENT '描述',
  `appid` VARCHAR(128) NOT NULL COMMENT '商户应用ID',
  `mchId` VARCHAR(128) NOT NULL COMMENT '商户ID',
  `body` VARCHAR(256) NOT NULL COMMENT '微信支付中提交的body',
  `total_fee` DOUBLE(8,2) NOT NULL COMMENT '支付总金额',
  `notify_url` VARCHAR(256) NOT NULL COMMENT '回调地址',
  `trade_type` VARCHAR(50) COMMENT '交易类型',
  `notify_time` DATETIME  COMMENT '回调时间',
  `trade_status` INT(1) NOT NULL  COMMENT '0:交易创建 1:交易成功',
  `order_no` INT(11) NOT NULL   COMMENT '订单保持一致',
  `order_type` INT(1) NOT NULL COMMENT '订单类型,1:用户消费单,2:分润单 3:服务卡充值单',
  PRIMARY KEY (`trade_no`),
  UNIQUE  KEY  idx_order_no(`order_no`,order_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '微信支付表';

CREATE TABLE  IF NOT EXISTS `user`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `username` VARCHAR(64) NOT NULL COMMENT '主键,用户名',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `nickname` VARCHAR (64) COMMENT '别名',
  `openid` VARCHAR(128) COMMENT '第三方开发平台ID',
  `third_party` INT(2) COMMENT '第三方平台,1:微信 2:支付宝 3:百度 4:新浪',
  `password` VARCHAR(64) NOT NULL COMMENT '密码',
  `mobile` VARCHAR(11)  COMMENT '手机,绑定时程序上控制唯一',
  `email` VARCHAR(128)  COMMENT '电子邮件,绑定时程序上控制唯一',
  `gender` INT(1) COMMENT '性别: 1,男; 2,女; 0,未知',
  `avatar` VARCHAR(300) COMMENT '头像地址',
  `birthday` DATETIME COMMENT '生日',
  `province` VARCHAR(20) COMMENT '所属省份',
  `city` VARCHAR(20) COMMENT '所属城市',
  `address` VARCHAR(50) COMMENT '详细地址',
  `remark` VARCHAR(255) COMMENT '备注',
  `sys_user_id` INT(11)  COMMENT '微信用户所属运营商用户ID',
  `wx_id` VARCHAR(20) COMMENT '微信ID',
  `status` INT(2) NOT NULL DEFAULT 0 COMMENT '用户状态,0:正常 1:黑名单 ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户表,不要前缀,因为用户模块计划抽象成通用功能';


CREATE TABLE  IF NOT EXISTS `user_wallet`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `wallet_id` INT(1) NOT NULL COMMENT '钱包ID,枚举中获取值',
  `wallet_name` VARCHAR(20) NOT NULL COMMENT '钱包名称,枚举中获取值',
  `money` DOUBLE(12,2) NOT NULL COMMENT '钱数',
  `username` VARCHAR(64) NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`),
  KEY idx_username(`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户钱包表';

CREATE TABLE  IF NOT EXISTS `user_device`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `sno` VARCHAR(64) NOT NULL  COMMENT '对应设备序列号',
  `mac` VARCHAR(64) NOT NULL  COMMENT '对应设备MAC',
  `wechat_device_id` VARCHAR(64) COMMENT '微信设备ID',
  `username` VARCHAR(64) NOT NULL COMMENT '所属用户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '用户绑定设备表';

CREATE TABLE  IF NOT EXISTS `share_benefit_rule`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(30) NOT NULL COMMENT '分润规则',
  `frequency_id` INT(1) NOT NULL COMMENT '频率ID',
  `frequency_name` VARCHAR(10) NOT NULL COMMENT '每天,每周,每月,每年',
  `percentage` DOUBLE(3,2) NOT NULL COMMENT '分润比例',
  PRIMARY KEY (`id`),
  KEY  idx_frequency_id(`frequency_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '分润规则表';

CREATE TABLE  IF NOT EXISTS `share_benefit_order`(
  `order_no` VARCHAR(20) NOT NULL  COMMENT '主键,分润单NO',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `operator_id` INT(11) NOT NULL COMMENT '运营商ID',
  `operator_name` VARCHAR(50) NOT NULL COMMENT '运营商名称',
  `order_count` INT(11) NOT NULL COMMENT '分润单数量',
  `amount` DOUBLE(12,2) NOT NULL COMMENT '分润总金额基数',
  `percentage` DOUBLE(3,2) NOT NULL COMMENT '分润比例',
  `tatal_fee`  DOUBLE(12,2) NOT NULL COMMENT '分润金额',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '分润单表';

CREATE TABLE  IF NOT EXISTS `sborder_to_order`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `share_benefit_order_no` VARCHAR(20) NOT NULL  COMMENT '主键,分润单NO',
  `order_no` VARCHAR(20) NOT NULL   COMMENT'消费订单号',
  `amount` DOUBLE(12,2) NOT NULL COMMENT '订单总价',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '分润单与订单记录表,关系表,sb:share benefit';


CREATE TABLE  IF NOT EXISTS `service_card`(
  `card_no` INT(11) NOT NULL   COMMENT '服务卡号',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `name` VARCHAR(50) NOT NULL COMMENT '服务卡名称',
  `amount` DOUBLE(12,2) NOT NULL COMMENT '分润总金额基数',
  `status` INT(1) NOT NULL COMMENT '服务卡状态,0:冻结 1:正常 ',
  `period_validity` DATETIME NOT NULL COMMENT '有效期至',
  `active_time` DATETIME NOT NULL COMMENT '激活日期,首次使用',
  `validity_time` INT(6) NOT NULL COMMENT '有效时长,如10天',
  `manufacturer_id` INT(11) COMMENT '所属厂商ID',
  `agnent_id` INT(11) COMMENT '代理商ID',
  `operator_id` INT(11) COMMENT '运营商ID',
  PRIMARY KEY (`card_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '服务卡表';

CREATE TABLE  IF NOT EXISTS `service_card_use_record`(
  `id` INT(11) NOT NULL  AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `card_no` INT(11) NOT NULL   COMMENT '服务卡号',
  `sno` VARCHAR(64) NOT NULL  COMMENT '对应设备序列号',
  `mac` VARCHAR(64) NOT NULL  COMMENT '对应设备MAC',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '服务卡使用记录表';


CREATE TABLE  IF NOT EXISTS `service_card_charge_order`(
  `charge_order_no` INT(11) NOT NULL   COMMENT '主键,充值订单号',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `card_no` INT(11) NOT NULL   COMMENT '服务卡号',
  `username` VARCHAR(64) COMMENT '用户在线充值的时候填写',
  `amount` DOUBLE(12,2) NOT NULL COMMENT '充值总金额',
  `status` INT(1) NOT NULL COMMENT '充值单状态,0:创建订单 1:充值成功 2:充值失败',
  PRIMARY KEY (`charge_order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '服务卡充值订单表';

CREATE TABLE `china_area` (
   `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL DEFAULT '' COMMENT '省市县的名字',
  `code` int(6) unsigned NOT NULL DEFAULT '0' COMMENT '行政区编码',
  `parent_code` int(6) unsigned NOT NULL DEFAULT '0',
  `parent_name` varchar(20) DEFAULT '' COMMENT '所属父级的中文名称',
  `is_leaf` int(6) DEFAULT b'0',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ciCOMMENT='全国省市行政编码表';

CREATE TABLE  IF NOT EXISTS 'product_price_detail'(
 `id`   INT(11) NOT NULL   AUTO_INCREMENT COMMENT '主键,自增长',
 `ctime` DATETIME NOT NULL COMMENT '添加时间', 
  `utime` DATETIME COMMENT '更新时间', 
 `service_mode_id`    INT(11) NOT NULL COMMENT '收费模式的id',
`product_id`  INT(11) NOT NULL COMMENT '产品的id',
`service_type`  VARCHAR(20) NOT NULL COMMENT '收费类型',
`price`  DOUBLE(8,2) NOT NULL COMMENT '单价',
`num`  DOUBLE(8,1) NOT NULL COMMENT '数量',
`unit`  VARCHAR(20) NOT NULL COMMENT '单位',
`status` INT(11) NOT NULL COMMENT '状态：0：启用，1:禁用',
`sys_user_id` INT(11) NOT NULL COMMENT '系统用户id，创建者',
`sys_user_name` VARCHAR(20) NOT NULL COMMENT '系统用户姓名，创建者'
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品(或者设备)收费价格详情';

-- product_category表增加创建用户
ALTER TABLE product_category ADD sys_user_id INT(11) NOT NULL COMMENT '创建人';
ALTER TABLE product_category ADD sys_user_name VARCHAR(50) NOT NULL COMMENT '创建人登录名';

-- 数据点
ALTER TABLE product_data_point ADD value_limit VARCHAR(255) COMMENT '值，比如枚举，数值，布尔等，从接口返回中解析';

-- 产品操作记录
CREATE TABLE  IF NOT EXISTS 'product_operation_history'(
  `id`   INT(11) NOT NULL   AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` DATETIME NOT NULL COMMENT '添加时间', 
  `product_id`  INT(11)  COMMENT '产品的id',
  `device_sno` VARCHAR(30) COMMENT '设备sno',
  `operate_type`  INT NOT NULL COMMENT '操作类型',
  `ip`  VARCHAR(20) NOT NULL COMMENT 'ip地址',
  `sys_user_id` INT(11) NOT NULL COMMENT '操作人',
  `sys_user_name` VARCHAR(20) NOT NULL COMMENT '操作人'
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '产品操作记录';
