ALTER TABLE `gizwits_lease`.`product`
ADD COLUMN `network_type` INT(2) NOT NULL DEFAULT 0 COMMENT '通信类型，0.移动；1.联通' AFTER `wx_product_id`;
