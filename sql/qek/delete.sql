ALTER TABLE `device_group_to_device`
ADD COLUMN `is_deleted` INT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除' AFTER `device_sno`,
ADD COLUMN `utime` DATETIME NULL AFTER `ctime`;


ALTER TABLE `operator`
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除';

ALTER TABLE `product_category`
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除';

ALTER TABLE `product_data_point`
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除';

ALTER TABLE `product_service_detail`
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 COMMENT '是否删除：0未删除 1已删除';



ALTER TABLE `product_data_point`
DROP INDEX `idx_identity` ,
ADD INDEX `idx_identity` (`identity_name` ASC, `product_id` ASC);


ALTER TABLE `user`
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 AFTER `wx_nickname`;

ALTER TABLE `user_wx_ext`
ADD COLUMN `is_deleted` INT(1) NULL DEFAULT 0 AFTER `authorization_time`;



