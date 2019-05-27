ALTER TABLE `device`
ADD COLUMN `expiration_time` DATETIME NULL COMMENT '设备到期时间' AFTER `is_out_of_range`;
