ALTER TABLE `user_wallet`
ADD COLUMN `user_id` INT(11) NULL COMMENT ' 用户id' ,
ADD COLUMN `sys_user_id` INT(11) NULL COMMENT '用户所属的运营商，暂时只针对“押金”类型' ;


ALTER TABLE `user_wallet_charge_order`
ADD COLUMN `user_id` INT(11) NULL COMMENT '用户id' ;

ALTER TABLE `user_wallet_use_record`
ADD COLUMN `user_id` INT(11) NULL COMMENT '用户id' ;

update user_wallet  w set w.user_id = (select id from user where username = w.username);
update user_wallet_charge_order  w set w.user_id = (select id from user where username = w.username);
update user_wallet_use_record  w set w.user_id = (select id from user where username = w.username);