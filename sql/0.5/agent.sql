ALTER TABLE agent ADD sys_account_id INT NOT NULL COMMENT '代理商对应的系统用户id';
CREATE UNIQUE INDEX agent_sys_account_id_uindex ON agent (sys_account_id);
ALTER TABLE agent ADD sys_user_name VARCHAR(32) NULL COMMENT '创建人名称';
DROP INDEX idx_sys_user ON agent;
ALTER TABLE agent MODIFY sys_user_id INT(11) NOT NULL COMMENT '创建人';
ALTER TABLE agent ADD status INT(1) DEFAULT 1 NOT NULL COMMENT '状态：1,待分配 2,正常 3,暂停';
ALTER TABLE agent
  MODIFY COLUMN sys_user_id INT(11) NOT NULL COMMENT '创建人' AFTER status;
ALTER TABLE `agent`
ADD COLUMN `share_benefit_rule_id` VARCHAR(50) NULL COMMENT '分润规则ID';

ALTER TABLE agent ADD is_deleted INT(1) DEFAULT 0 NOT NULL COMMENT '是否删除，0,未删除 1,已删除';

