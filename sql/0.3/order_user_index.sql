ALTER TABLE `user`
ADD INDEX `user_openid_index` (`openid` ASC),
ADD INDEX `user_username_index` (`username` ASC);

ALTER TABLE `order_base`
ADD INDEX `order_base_sno_index` (`sno` ASC),
ADD INDEX `order_base_user_id_index` (`user_id` ASC);