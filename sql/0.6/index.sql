ALTER TABLE `gizwits_lease`.`product_command_config`
ADD INDEX `index_product_id` (`product_id` ASC);

ALTER TABLE `gizwits_lease`.`product_data_point`
ADD INDEX `index_product_id` (`product_id` ASC);

ALTER TABLE `gizwits_lease`.`user`
ADD INDEX `index_sys_user_id` (`sys_user_id` ASC),
ADD INDEX `index_openId` (`openid` ASC),
ADD INDEX `index_mobile` (`mobile` ASC);

ALTER TABLE `gizwits_lease`.`user_charge_card_operation_record`
DROP INDEX `user_charge_card_operation_record_id_uindex` ,
ADD INDEX `card_num_index` (`card_num` ASC);
