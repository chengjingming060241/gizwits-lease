ALTER TABLE `feedback_user`
CHANGE COLUMN `picture_url` `picture_url` TEXT CHARACTER SET 'utf8mb4' NULL COMMENT '图片地址' ,
CHANGE COLUMN `origin` `origin` INT(1) NULL COMMENT '消息来源：1 移动用户端,2 移动管理端 ' ,
CHANGE COLUMN `sno` `sno` VARCHAR(64) CHARACTER SET 'utf8mb4' NULL COMMENT '设备序列号' ,
CHANGE COLUMN `mac` `mac` VARCHAR(32) CHARACTER SET 'utf8mb4' NULL COMMENT 'MAC地址' ,
CHANGE COLUMN `recipient_id` `recipient_id` INT(11) NULL COMMENT '收件人id' ,
CHANGE COLUMN `recipient_name` `recipient_name` VARCHAR(45) CHARACTER SET 'utf8mb4' NULL COMMENT '收件人姓名' ,
CHANGE COLUMN `is_read` `is_read` INT(1) NULL DEFAULT '0' COMMENT '是否已读：0 未读，1已读' ;
