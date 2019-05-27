ALTER TABLE `product_service_mode`
ADD COLUMN `command` VARCHAR(500)  COMMENT '在没收费模式详情的时候需要的指令,如免费模式时需要下发的指令',
ADD COLUMN `is_deleted` INT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0,否；1,是' ;

ALTER TABLE `product_service_detail`
ADD COLUMN `is_deleted` INT(1) NOT NULL DEFAULT 0 COMMENT '是否删除：0,否；1,是',
CHANGE COLUMN `num` `num` DOUBLE(8,2) NOT NULL COMMENT '数量' ;


CREATE TABLE  IF NOT EXISTS `order_ext_by_quantity`(
  `order_no` INT(11) NOT NULL   COMMENT '主键,与订单保持一致',
  `ctime` DATETIME NOT NULL COMMENT '添加时间',
  `utime` DATETIME COMMENT '更新时间',
  `quantity` DOUBLE(7,2) NOT NULL COMMENT '购买的量',
  `price` DOUBLE(6,2) NOT NULL COMMENT '单价,元',
  `unit` VARCHAR(20) NOT NULL COMMENT '单位,立方',
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '订单扩展记录表(按量)';
ALTER TABLE `order_ext_by_quantity`
CHANGE COLUMN `order_no` `order_no` VARCHAR(20) NOT NULL COMMENT '主键,与订单保持一致' ;

ALTER TABLE `product`
ADD COLUMN `gizwits_appid` VARCHAR(50) NULL COMMENT '机智云AppId',
ADD COLUMN `gizwits_appsecret` VARCHAR(50) NULL AFTER `gizwits_appid`;
