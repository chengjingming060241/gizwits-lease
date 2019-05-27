alter table device add column giz_ws_port varchar(128);
alter table device add column giz_wss_port varchar(128);
alter table device add column giz_host varchar(128);
alter table device add column content_url varchar(128);

alter table order_base add column is_deleted int(1);

alter table order_status_flow MODIFY order_no VARCHAR(20) ;


ALTER TABLE product_service_detail MODIFY command VARCHAR(500) NOT NULL;



CREATE TABLE `trade_base` (
  `trade_no` varchar(31) NOT NULL COMMENT '交易号，主键，按照一定规则生成',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '更改时间',
  `status` int(1) NOT NULL COMMENT '交易状态，1交易创建，2交易成功，3交易失败',
  `total_fee` double(12,2) NOT NULL COMMENT '交易金额，单位为分',
  `order_no` varchar(20) NOT NULL COMMENT '业务订单号，',
  `order_type` int(2) NOT NULL COMMENT '1用户消费订单，2分润订单，3充值订单',
  `notify_url` varchar(300) DEFAULT NULL COMMENT '发起交易时传递的回调URL',
  `nofify_time` datetime DEFAULT NULL COMMENT '交易回调时间',
  PRIMARY KEY (`trade_no`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `trade_weixin` (
  `trade_no` varchar(31) NOT NULL COMMENT '主键，与trade_base表关联',
  `transaction_id` varchar(64) DEFAULT NULL COMMENT '微信单号',
  `appid` varchar(64) DEFAULT NULL COMMENT '微信开放平台审核通过的应用APPID',
  `mch_id` varchar(64) DEFAULT NULL COMMENT '微信支付分配的商户号',
  `body` varchar(256) DEFAULT NULL COMMENT '商品描述',
  `time_end` varchar(16) DEFAULT NULL COMMENT '微信端,支付完成时间',
  PRIMARY KEY (`trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `user_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '对应设备序列号',
  `mac` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '对应设备MAC',
  `wechat_device_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '微信设备ID',
  `openid` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'openId',
  `user_id` int(11) NOT NULL COMMENT '所属用户id',
  `is_bind` int(1) DEFAULT NULL COMMENT '是否绑定',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户绑定设备表';

CREATE TABLE `order_ext_by_time` (
  `order_no` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键,与订单保持一致',
  `ctime` datetime NOT NULL COMMENT '添加时间',
  `utime` datetime DEFAULT NULL COMMENT '更新时间',
  `start_time` datetime NOT NULL COMMENT '服务开始时间',
  `end_time` datetime NOT NULL COMMENT '服务结束时间',
  `duration` double(5,2) NOT NULL COMMENT '购买时长',
  `price` double(6,2) NOT NULL COMMENT '单价,元',
  `unit` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '单位,分钟/小时/天',
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单扩展表(按时)';