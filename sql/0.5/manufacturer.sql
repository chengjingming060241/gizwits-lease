ALTER TABLE manufacturer ADD is_deleted INT(1) DEFAULT 0 NOT NULL COMMENT '是否删除，0,未删除 1,已删除';
ALTER TABLE manufacturer ADD sys_account_id INT(11) NULL COMMENT '厂商绑定的系统帐号';
ALTER TABLE manufacturer ADD sys_user_name VARCHAR(32) NULL COMMENT '创建人名称';
ALTER TABLE manufacturer MODIFY sys_user_id INT(11) NULL COMMENT '创建人';

