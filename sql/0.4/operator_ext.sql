CREATE TABLE `operator_ext` (
  `id` INT(11) NOT NULL,
  `operator_id` INT(11) NOT NULL COMMENT '对应的运营商ID字段',
  `cash_pledge` DOUBLE(11,2) NULL COMMENT '押金(若配置此项，则用户必须交过押金才可使用设备)',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营商信息扩展';


ALTER TABLE `operator`
ADD COLUMN `description` VARCHAR(2000) NULL COMMENT '公司内容简介' ;
