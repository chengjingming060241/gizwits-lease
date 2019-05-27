ALTER TABLE operator DROP manufacturer_id;
ALTER TABLE operator DROP parent_operator_id;
ALTER TABLE operator MODIFY share_benefit_rule_id INT(11) COMMENT '分润规则';
ALTER TABLE operator ADD status INT(1) DEFAULT 1 NOT NULL;
ALTER TABLE operator
  MODIFY COLUMN status INT(1) NOT NULL DEFAULT 1 COMMENT '运营商状态，1.待分配，2.运营中，3.暂停运营' AFTER address;
ALTER TABLE operator ADD sys_account_id INT(11) NOT NULL COMMENT '运营商绑定的系统账号';
ALTER TABLE operator ADD sys_user_name VARCHAR(32) NULL;
ALTER TABLE operator MODIFY sys_user_id INT(11) NOT NULL COMMENT '创建人';
ALTER TABLE device_launch_area MODIFY operator_id INT(13) COMMENT '运营商对应的系统账号';
ALTER TABLE device_launch_area MODIFY operator_name VARCHAR(50) COMMENT '运营商名称';
ALTER TABLE device MODIFY operator_id INT(11) COMMENT '运营商对应的系统账号';
ALTER TABLE device MODIFY operator_name VARCHAR(50) COMMENT '运营商名称';

CREATE TABLE IF NOT EXISTS device_assign_record
(
  id INT PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  ctime DATETIME NOT NULL COMMENT '创建时间',
  sno VARCHAR(30) NOT NULL COMMENT '设备sno',
  mac VARCHAR(30) COMMENT '设备mac',
  source_operator INT COMMENT '原运营商',
  destination_operator INT NOT NULL COMMENT '现运营商',
  sys_user_id INT NOT NULL COMMENT '创建人',
  sys_user_name VARCHAR(30) COMMENT '创建人名称'
);
ALTER TABLE device_assign_record COMMENT = '设备分配记录';
ALTER TABLE device_launch_area MODIFY operator_id INT(13) COMMENT '运营商对应的系统账号';
ALTER TABLE device_launch_area MODIFY operator_name VARCHAR(50) COMMENT '运营商对应的系统账号名称';
ALTER TABLE device MODIFY operator_id INT(11) COMMENT '运营商对应的系统账号';
ALTER TABLE device MODIFY operator_name VARCHAR(50) COMMENT '运营商对应的系统账号名称';
