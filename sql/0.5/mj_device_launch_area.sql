ALTER TABLE device_launch_area ADD owner_id INT NULL COMMENT '投放点当前归属';

-- 投放点分配记录
CREATE TABLE IF NOT EXISTS device_launch_area_assign_record
(
  id INT PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  ctime DATETIME NOT NULL COMMENT '创建时间',
  device_launch_area_id INT NOT NULL COMMENT '投放点id',
  device_launch_area_name VARCHAR(32) NOT NULL COMMENT '投放点名称',
  source_owner_id INT NOT NULL COMMENT '原来的拥有者',
  target_owner_id INT NOT NULL COMMENT '分配之后的拥有者',
  operate_type VARCHAR(32) DEFAULT 'ASSIGN' NOT NULL COMMENT '操作类型，ASSIGN和UNBIND',
  sys_user_id INT NOT NULL COMMENT '创建人',
  sys_user_name VARCHAR(32) NOT NULL COMMENT '创建人名称'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT = '投放点分配记录';
