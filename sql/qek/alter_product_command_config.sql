ALTER TABLE `product_command_config`
ADD COLUMN `is_clock_correct` INT(1) NOT NULL DEFAULT 0 COMMENT '是否需要时钟校准,0 否,1 是' ,
ADD COLUMN `calculate_value` INT(11) NULL COMMENT '换算数据点单位' ,
ADD COLUMN `error_range` INT(11) NULL COMMENT '误差范围',
ADD COLUMN `clock_correct_datapoint` VARCHAR(50) NULL COMMENT '时钟校准的数据点';


ALTER TABLE `product_command_config`
ADD COLUMN `identity_name` VARCHAR(60) NULL COMMENT '数据点标识名' AFTER `clock_correct_datapoint`;

alter table product_command_config add column `ref_dp` varchar(60) DEFAULT null comment '续费时参考的数据点';

ALTER TABLE `product_command_config`
ADD COLUMN `show_type` INT(1) NULL DEFAULT 1 COMMENT '展示形式：1 文本 2饼状图 3进度条' AFTER `ref_dp`;

ALTER TABLE `product_command_config`
ADD COLUMN `is_free` INT(1) NOT NULL DEFAULT 0 COMMENT '是否免费，0，收费，1，免费',
ADD COLUMN `working_mode` VARCHAR(50) NULL COMMENT '工作模式';