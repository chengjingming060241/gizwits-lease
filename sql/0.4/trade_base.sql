ALTER TABLE `gizwits_lease`.`trade_base`
ADD COLUMN `trade_type` INT(1) NOT NULL COMMENT '交易类型：1:微信公众号，2app微信，3支付宝，4充值卡，5余额，6微信支付' AFTER `nofify_time`

