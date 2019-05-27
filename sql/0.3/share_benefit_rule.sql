CREATE TABLE IF NOT EXISTS `share_benefit_rule` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `share_benefit_rule_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分润规则名称',
  `operator_id` int(11) NOT NULL COMMENT '运营商ID',
  `operator_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '运营商名称',
  `start_time` datetime NOT NULL COMMENT '账单首次生成时间',
  `frequency` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '对账单生成频率: DAY,WEEK,MONTH,YEAR',
  `last_execute_time` datetime DEFAULT NULL COMMENT '上一次分润执行的时间',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_user_id` int(11) NOT NULL COMMENT '分润规则的所有者',
  `is_deleted` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `sh_rule_operator_id` (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分润规则表'

CREATE TABLE IF NOT EXISTS `share_benefit_rule_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_id` int(11) NOT NULL COMMENT '分润规则主表ID',
  `name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '详细分润规则名称',
  `share_percentage` decimal(4,2) NOT NULL DEFAULT '0.00' COMMENT '分润比例，显示的是百分数',
  `share_type` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SINGLE' COMMENT '分润类型，使用设备：ALL,所有设备；SINGLE，个别设备；',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime DEFAULT NULL COMMENT '修改时间',
  `is_deleted` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分润规则详细表'

CREATE TABLE IF NOT EXISTS `share_benefit_rule_detail_device` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `rule_detail_id` INT(11) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分润规则详细表ID',
  `sno` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '设备SNO',
  `ctime` datetime NOT NULL COMMENT '创建时间',
  `utime` datetime DEFAULT NULL,
  `is_deleted` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='分润规则详细设备表'
