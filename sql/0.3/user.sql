ALTER TABLE user ADD status INT(1) DEFAULT 1 NOT NULL COMMENT '用户状态: 1,正常; 2,黑名单';
ALTER TABLE user ADD move_in_black_time DATETIME NULL COMMENT '移入黑名单时间';
ALTER TABLE user ADD move_out_black_time DATETIME NULL COMMENT '移出黑名单时间';
ALTER TABLE user ADD authorization_time DATETIME NULL COMMENT '授权时间';

CREATE TABLE IF NOT EXISTS user_move_record
(
  id INT PRIMARY KEY NOT NULL COMMENT '主键' AUTO_INCREMENT,
  ctime DATETIME NOT NULL COMMENT '创建时间',
  user_id INT NOT NULL COMMENT '移入/出的用户',
  move_type INT NOT NULL COMMENT '移动类型，1:移入黑名单 2:移出黑名单',
  sys_user_id INT NOT NULL COMMENT '操作人员',
  sys_user_name VARCHAR(30) COMMENT '操作人员名称'
);
ALTER TABLE user_move_record COMMENT = '用户移动记录（移入/出黑名单）';