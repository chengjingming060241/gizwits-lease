ALTER TABLE `gizwits_lease`.`operator`
ADD COLUMN `is_allot` INT(1) NOT NULL DEFAULT 0 COMMENT '是否分配投放点：0未分配 1已分配' AFTER `sys_user_name`;

