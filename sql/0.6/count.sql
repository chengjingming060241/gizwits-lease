ALTER TABLE `gizwits_lease`.`stat_device_trend`
ADD COLUMN `previous_deivce_total` INT(11) NULL COMMENT '之前的设备总数' AFTER `active_count`;
