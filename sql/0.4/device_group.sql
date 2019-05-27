CREATE TABLE IF NOT EXISTS device_group
(
  id INT PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  name VARCHAR(64) NOT NULL COMMENT '组名称',
  ctime DATETIME NOT NULL COMMENT '创建时间',
  utime DATETIME COMMENT '更新时间',
  is_deleted INT DEFAULT 0 NOT NULL COMMENT '是否删除，0:未删除，1:已删除',
  sys_user_id INT NOT NULL COMMENT '创建人',
  sys_user_name VARCHAR(64) NOT NULL COMMENT '创建人名称'
)ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备组';
CREATE UNIQUE INDEX device_group_id_uindex ON device_group (id);

ALTER TABLE device ADD group_id INT NULL COMMENT '设备组id';