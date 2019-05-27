CREATE TABLE `trade_alipay` (
  `trade_no` varchar(31) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键，与trade_base表关联',
  `alipay_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付宝单号',
  `appid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付宝应用APPID',
  `seller_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '收款支付宝账号对应的支付宝唯一用户号',
  `subject` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '订单标题',
  `trade_status` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '交易状态：交易完成TRADE_FINISHED  交易成功TRADE_SUCCESS',
  PRIMARY KEY (`trade_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付宝交易表'