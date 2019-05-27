CREATE TABLE IF NOT EXISTS device_group_to_device
(
  id INT PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  ctime DATETIME NOT NULL COMMENT '创建时间',
  device_group_id INT NOT NULL COMMENT '设备组id',
  device_sno VARCHAR(32) NOT NULL COMMENT '设备sno'
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT '设备组与设备的关系';
CREATE UNIQUE INDEX device_group_to_device_id_uindex ON device_group_to_device (id);