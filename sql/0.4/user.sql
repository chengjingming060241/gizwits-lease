CREATE TABLE `user_wx_ext` (
  `id` INT(11) NOT NULL,
  `user_openid` VARCHAR(60) NOT NULL COMMENT '对应user表的openid',
  `openid` VARCHAR(60) NOT NULL COMMENT '微信用户ID',
  `wx_id` VARCHAR(45) NOT NULL COMMENT '微信公众号ID',
  `sys_user_id` INT(11) NOT NULL COMMENT '微信用户所属运营用户ID',
  `ctime` DATETIME NOT NULL,
  `utime` DATETIME NULL,
  `status` INT(1) NOT NULL DEFAULT 1 COMMENT '用户状态: 1,正常; 2,黑名单',
  `move_in_black_time` DATETIME NULL COMMENT '移入黑名单时间',
  `move_out_black_time` DATETIME NULL COMMENT '移出黑名单时间',
  `authorization_time` DATETIME NULL COMMENT '授权时间',
  PRIMARY KEY (`id`)
  )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='微信用户信息';

ALTER TABLE `user`
ADD COLUMN `alipay_unionid` VARCHAR(128) NULL COMMENT '支付宝用户ID' AFTER `authorization_time`,
ADD COLUMN `sina_unionid` VARCHAR(128) NULL COMMENT '微博用户ID' AFTER `alipay_unionid`,
ADD COLUMN `baidu_unionid` VARCHAR(128) NULL COMMENT '百度用户ID' AFTER `sina_unionid`,
ADD COLUMN `code` VARCHAR(45) NULL COMMENT '验证码' AFTER `baidu_unionid`,
CHANGE COLUMN `password` `password` VARCHAR(64) CHARACTER SET 'utf8mb4' NULL COMMENT '密码' ;


ALTER TABLE `user_wallet`
ADD COLUMN `discount_money` DOUBLE(11,2) NULL COMMENT '用户的充值赠送金额,此部分的金额说明money中有多少金额是优惠金额' ;