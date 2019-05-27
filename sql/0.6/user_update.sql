ALTER TABLE `user`
CHANGE COLUMN `openid` `openid` VARCHAR(128) CHARACTER SET 'utf8mb4' NULL DEFAULT NULL COMMENT '微信用户的unionId',
DROP COLUMN `wx_id`;

ALTER TABLE `user_wx_ext`
CHANGE COLUMN `user_openid` `user_openid` VARCHAR(60) CHARACTER SET 'utf8mb4' NULL DEFAULT NULL COMMENT '微信用户的unionid，对应user表的openid' ,
CHANGE COLUMN `openid` `openid` VARCHAR(60) CHARACTER SET 'utf8mb4' NOT NULL COMMENT '微信用户openid，每个用户对一个公众号生成一个openid' ,
CHANGE COLUMN `wx_id` `wx_id` VARCHAR(45) CHARACTER SET 'utf8mb4' NOT NULL COMMENT '微信公众号ID，是微信公众号的唯一标识' ;
