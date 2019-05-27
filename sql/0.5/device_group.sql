ALTER TABLE device_group ADD assigned_account_id INT(11) NULL COMMENT '被分配的运营商或代理商的系统帐号';
ALTER TABLE device_group ADD assigned_name VARCHAR(64) NULL COMMENT '运营商或代理商名称';