--分润单增加不同类型订单的合计
ALTER TABLE `share_benefit_sheet`
ADD COLUMN `wx_total_money` DOUBLE(11,2) NULL COMMENT '微信订单合计' ,
ADD COLUMN `card_total_money` DOUBLE(11,2) NULL COMMENT '刷卡订单合计' ,
ADD COLUMN `alipay_total_money` DOUBLE(11,2) NULL COMMENT '支付宝订单合计' ,
ADD COLUMN `other_total_money` DOUBLE(11,2) NULL COMMENT '其他订单合计' ;