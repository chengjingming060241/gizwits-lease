ALTER TABLE product ADD wx_product_id VARCHAR(10) NULL COMMENT '微信硬件productId';
ALTER TABLE product MODIFY manufacturer_id INT(11) NOT NULL COMMENT '所属厂商的关联的系统账号';