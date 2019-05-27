CREATE TABLE `share_benefit_sheet_action_record` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `sheet_id` int(11) NOT NULL COMMENT '分润单ID',
  `action_type` int(2) NOT NULL DEFAULT '0' COMMENT '操作类型：0，创建分润单；1，审核通过；2、重新审核；3，执行分润',
  `user_id` int(11) NOT NULL COMMENT '操作者',
  `ctime` datetime NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分润单操作记录';

CREATE TABLE `share_benefit_sheet_pay_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sheet_id` int(11) NOT NULL COMMENT '分润单ID',
  `trade_no` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '交易订单号',
  `amount` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '分润金额',
  `content` varchar(1500) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分润执行消息体',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '状态：1，执行分润；2，分润成功；3，分润失败；',
  `user_id` int(11) NOT NULL COMMENT '操作者',
  `ctime` datetime NOT NULL,
  `utime` datetime DEFAULT NULL,
  `callback_content` varchar(1500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '支付反馈结果',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分润单支付记录';

CREATE TABLE `share_benefit_sheet` (
  `id` int(11) NOT NULL COMMENT '主键ID',
  `sheet_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分润账单号',
  `operator_id` int(11) NOT NULL COMMENT '运营商id',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '运营商名称',
  `status` int(2) NOT NULL COMMENT '账单状态：0，创建；1，待审核;2，待分润；3,执行分润中；4，分润成功；5，分润失败；',
  `pay_type` int(2) NOT NULL DEFAULT '1' COMMENT '分润支付类型：1，微信；2，支付宝；3，线下；',
  `order_count` int(11) NOT NULL DEFAULT '0' COMMENT '订单数',
  `total_money` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '所有订单的总合计金额',
  `share_money` double(11,2) NOT NULL DEFAULT '0.00' COMMENT '当前运营商的分润金额',
  `ctime` datetime NOT NULL,
  `utime` datetime DEFAULT NULL,
  `trade_no` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分润执行商户订单号',
  `payment_no` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分润成功时的微信订单号',
  `payment_time` datetime DEFAULT NULL COMMENT '微信付款成功时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `pay_account` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '支付账号，存储的是公众号的信息wx_id',
  `receiver_openid` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收款人openid',
  `receiver_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '收款人名称',
  `sys_user_id` int(11) DEFAULT NULL COMMENT '创建人(生成分润单时把运营商关联的系统账号写进来)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分润账单表';

CREATE TABLE `share_benefit_sheet_order` (
  `id` int(11) NOT NULL,
  `sheet_no` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分润单ID',
  `operator_id` int(11) NOT NULL COMMENT '运营商ID',
  `device_sno` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备sno',
  `order_no` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '订单号',
  `share_rule_detail_id` int(11) NOT NULL COMMENT '详细分润规则ID',
  `share_percentage` double(3,2) NOT NULL COMMENT '直属运营商分润比例',
  `parent_share_percentage` double(3,2) DEFAULT NULL COMMENT '上一级在该订单的分润比例',
  `share_money` double(11,4) DEFAULT NULL COMMENT '订单在该运营商的分润金额，四舍五入保留4位小数',
  `order_amount` double(12,5) DEFAULT NULL COMMENT '订单金额',
  `status` int(2) NOT NULL COMMENT '状态：1、待审核；2、审核通过；3、审核不通过；4、执行分润中；5、分润成功；',
  `ctime` datetime NOT NULL,
  `utime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='所有要参与分润的订单';


ALTER TABLE `share_benefit_sheet`
ADD COLUMN `is_try_again` INT(1) NOT NULL DEFAULT 0 COMMENT '是否使用trade_no重试支付，如果此字段为1，则分润单不可修改，只再次支付' ;
