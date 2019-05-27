------收费模式相关SQL----------
ALTER TABLE `product_command_config`
ADD COLUMN `is_free` INT(1) NOT NULL DEFAULT 0 COMMENT '是否免费，0，收费，1，免费',
ADD COLUMN `working_mode` VARCHAR(50) NULL COMMENT '工作模式';

ALTER TABLE `product_service_mode`
ADD COLUMN `is_free` INT(1) NOT NULL DEFAULT 0 COMMENT '是否收费：0，收费，1，免费，，数据从收费模式指令处获得',
ADD COLUMN `working_mode` VARCHAR(50) NULL COMMENT '工作模式';

CREATE TABLE `device_service_mode_setting` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sno` VARCHAR(64) NOT NULL COMMENT '设备序列号',
  `sys_account_id` INT(11) NOT NULL COMMENT '为设备设置收费模式的系统用户',
  `is_free` INT(1) NOT NULL DEFAULT 0 COMMENT '是否免费：0，收费，1，免费',
  `ctime` DATETIME NOT NULL,
  `utime` DATETIME NULL,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '设备收费模式设定(麻将机系统特有需求)';

ALTER TABLE `device_service_mode_setting`
ADD COLUMN `assign_account_id` INT(11) NULL COMMENT '分配对象的accountId，即设备的owner_id',
ADD COLUMN `is_deleted` INT(1) NOT NULL DEFAULT 0 ;


-----防串货相关sql--------
ALTER TABLE `agent`
ADD COLUMN `cover_level` INT(1) NOT NULL DEFAULT 1 COMMENT '代理商级别：1，国家级；2，省级；3，市级；4，区县级' ;
ALTER TABLE `operator`
ADD COLUMN `cover_level` INT(1) NOT NULL DEFAULT 1 COMMENT '代理商级别：1，国家级；2，省级；3，市级；4，区县级';