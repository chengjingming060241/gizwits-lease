ALTER TABLE `device_alarm`
ADD COLUMN `alarm_type` INT(1) NULL COMMENT '告警类型:0,报警;1,故障' ;


ALTER TABLE `sys_user_ext`
ADD COLUMN `wx_template_id` VARCHAR(45) NULL COMMENT '模版id';
