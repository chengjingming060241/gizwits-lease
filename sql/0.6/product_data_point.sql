ALTER TABLE `gizwits_lease`.`product_data_point`
CHANGE COLUMN `show_name` `show_name` VARCHAR(60) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL COMMENT '显示名称' ;

ALTER TABLE `gizwits_lease`.`order_base`
CHANGE COLUMN `command` `command` VARCHAR(400) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL COMMENT '发货指令' ;

ALTER TABLE `gizwits_lease`.`product_data_point`
CHANGE COLUMN `identity_name` `identity_name` VARCHAR(60) CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_unicode_ci' NOT NULL COMMENT '标志名称' ;
