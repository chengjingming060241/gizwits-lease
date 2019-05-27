ALTER TABLE `share_benefit_rule_detail_device`
ADD COLUMN `share_percentage` DECIMAL(4,2) NULL COMMENT '上级为本运营商设置的分润比例' ,
ADD COLUMN `children_percentage` DECIMAL(4,2) NULL DEFAULT 0.00 COMMENT '此设备在本运营商下级的分润比例' ;


ALTER TABLE `share_benefit_rule`
CHANGE COLUMN `operator_id` `sys_account_id` INT(11) NOT NULL COMMENT '运营商关联系统用户' ,
CHANGE COLUMN `id` `id` VARCHAR(50) NOT NULL COMMENT '主键ID' ;

ALTER TABLE `share_benefit_rule_detail`
CHANGE COLUMN `id` `id` VARCHAR(50) NOT NULL COMMENT '主键ID' ,
CHANGE COLUMN `rule_id` `rule_id` VARCHAR(50) NOT NULL COMMENT '分润规则主表ID' ;

ALTER TABLE `share_benefit_rule_detail_device`
CHANGE COLUMN `id` `id` VARCHAR(50) NOT NULL COMMENT '主键ID' ,
CHANGE COLUMN `rule_detail_id` `rule_detail_id` VARCHAR(50) NOT NULL COMMENT '分润规则详细表ID' ;


ALTER TABLE `share_benefit_sheet`
DROP COLUMN `operator_id`,
ADD COLUMN `sys_account_id` INT(11) NULL COMMENT '运营商的账号',
ADD COLUMN `rule_id` VARCHAR(50) NULL COMMENT '分润规则对应ID' ;


ALTER TABLE `share_benefit_sheet_order`
ADD COLUMN `sys_account_id` INT(11) NULL COMMENT '运营商或代理商的账号ID' AFTER `utime`
DROP COLUMN `parent_share_percentage`,
CHANGE COLUMN `children_share_percentage` `children_share_percentage` DOUBLE(3,2) NULL DEFAULT NULL COMMENT '下一级在该订单的分润比例' ;


ALTER TABLE `operator`
CHANGE COLUMN `share_benefit_rule_id` `share_benefit_rule_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '分润规则' ;

ALTER TABLE `agent`
ADD COLUMN `share_benefit_rule_id` VARCHAR(50) NULL COMMENT '分润规则ID' AFTER `is_deleted`;



