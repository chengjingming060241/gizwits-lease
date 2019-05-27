CREATE TABLE IF NOT EXISTS charge_setting
(
  id INT PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  ctime DATETIME NOT NULL COMMENT '创建时间',
  utime DATETIME COMMENT '更新时间',
  money DOUBLE(12,2) NOT NULL COMMENT '值',
  product_id INT COMMENT '产品id',
  sys_user_id INT NOT NULL COMMENT '创建人',
  sys_user_name VARCHAR(32) NOT NULL COMMENT '创建人名称'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='充值设定';