CREATE TABLE `order_timer` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` VARCHAR(20) NOT NULL COMMENT '关联订单号',
  `sno` VARCHAR(64) NOT NULL COMMENT '设备SNO',
  `week_day` VARCHAR(45) NOT NULL COMMENT '定时周几执行，多个日期用逗号隔开：1周一；2周二；3周三；4周四；5周五；6周六；7周日',
  `time` VARCHAR(45) NOT NULL COMMENT '执行时间，24小时制，如: 14:30:00',
  `is_enable` INT(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0，否；1，是',
  `is_expire` INT(1) NOT NULL DEFAULT 0 COMMENT '是否过期，订单过期后所有定时无效',
  `is_deleted` INT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0，否；1，是',
  `command` VARCHAR(200) NOT NULL COMMENT '控制指令内容',
  `ctime` DATETIME NOT NULL COMMENT '创建时间',
  `utime` DATETIME NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单定时任务';

ALTER TABLE `order_timer`
ADD INDEX `order_timer_order_no_index` (`order_no` ASC),
ADD INDEX `order_timer_device_sno_index` (`sno` ASC);

