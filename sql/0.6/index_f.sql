ALTER TABLE sys_user ADD INDEX idx_sys_user_id(sys_user_id);

ALTER TABLE device ADD INDEX idx_owner_id_work_status(owner_id, work_status);
ALTER TABLE device_alarm ADD INDEX idx_ctime(ctime);
ALTER TABLE device_group ADD INDEX idx_sys_user_id(sys_user_id);
ALTER TABLE device_group_to_device ADD INDEX idx_device_group_id(device_group_id);
ALTER TABLE device_launch_area ADD INDEX idx_sys_user_id(sys_user_id);
ALTER TABLE operator ADD INDEX idx_sys_account_id(sys_account_id);
ALTER TABLE order_base ADD INDEX idx_sys_user_id_ctime(sys_user_id, ctime);
ALTER TABLE order_base ADD INDEX idx_ctime(ctime);
ALTER TABLE order_base ADD INDEX idx_sno(sno);
ALTER TABLE order_base ADD INDEX idx_user_id(user_id);
ALTER TABLE order_status_flow ADD INDEX idx_order_no(order_no);
ALTER TABLE product ADD INDEX idx_sys_user_id(sys_user_id);

ALTER TABLE `gizwits_lease`.`product_command_config`
ADD INDEX `index_product_id` (`product_id` ASC);

ALTER TABLE `gizwits_lease`.`product_data_point`
ADD INDEX `index_product_id` (`product_id` ASC);

ALTER TABLE `gizwits_lease`.`user`

ADD INDEX `index_id` (`openid` ASC, `mobile` ASC, `wx_id` ASC) ;


ALTER TABLE `gizwits_lease`.`user_charge_card`
DROP INDEX `user_charge_card_id_uindex` ,
ADD UNIQUE INDEX `index_card_num` (`card_num` ASC),
ADD INDEX `index_user_id` (`user_id` ASC);

ALTER TABLE `gizwits_lease`.`user_charge_card_order`
DROP INDEX `user_charge_card_order_order_no_uindex` ,
ADD INDEX `index_card_num` (`card_num` ASC),
ADD INDEX `index_user_id` (`user_id` ASC);

ALTER TABLE `gizwits_lease`.`user_charge_card_operation_record`
DROP INDEX `user_charge_card_operation_record_id_uindex` ,
ADD INDEX `index_card_num` (`card_num` ASC),
ADD INDEX `index_sys_user_id` (`sys_user_id` ASC);

ALTER TABLE `gizwits_lease`.`user_device`
ADD INDEX `index_sno` (`sno` ASC),
ADD INDEX `index_openId` (`openid` ASC);

ALTER TABLE `gizwits_lease`.`user_move_record`
ADD INDEX `index_user_id` (`user_id` ASC),
ADD INDEX `index_sys_user_id` (`sys_user_id` ASC);

ALTER TABLE `gizwits_lease`.`user_wallet`
ADD INDEX `index_wallet_type` (`wallet_type` ASC);

ALTER TABLE `gizwits_lease`.`user_wallet_use_record`
ADD INDEX `index_trade_no` (`trade_no` ASC);

ALTER TABLE `gizwits_lease`.`user_wx_ext`
ADD INDEX `index_user_openId` (`user_openid` ASC),
ADD INDEX `index3_openId` (`openid` ASC),
ADD INDEX `index_wxId` (`wx_id` ASC);

ALTER TABLE `gizwits_lease`.`trade_base`
ADD INDEX `index_order_no` (`order_no` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_rule`
ADD INDEX `index_sys_user_id` (`sys_user_id` ASC, `sys_account_id` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_rule_detail`
ADD INDEX `index_ruleId` (`rule_id` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_rule_detail_device`
ADD INDEX `index_rule_detail_id` (`rule_detail_id` ASC)
ADD INDEX `index_sno` (`sno` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_sheet`
ADD INDEX `index_id` (`sheet_no` ASC, `operator_id` ASC, `trade_no` ASC, `rule_id` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_sheet_action_record`
ADD INDEX `index_id` (`sheet_id` ASC, `user_id` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_sheet_order`
ADD INDEX `index_id` (`sheet_no` ASC, `operator_id` ASC, `device_sno` ASC, `order_no` ASC);

ALTER TABLE `gizwits_lease`.`share_benefit_sheet_pay_record`
ADD INDEX `index_id` (`sheet_id` ASC, `trade_no` ASC, `user_id` ASC);

ALTER TABLE `gizwits_lease`.`product_category`
ADD INDEX `index_id` (`parent_category_id` ASC, `sys_user_id` ASC);

ALTER TABLE `gizwits_lease`.`product_service_detail`
DROP INDEX `index2` ,
ADD INDEX `index2` (`service_mode_id` ASC, `service_type_id` ASC);

ALTER TABLE `gizwits_lease`.`product_service_mode`
ADD INDEX `index_id` (`product_id` ASC);

ALTER TABLE `gizwits_lease`.`product_to_properties`
ADD INDEX `index_id` (`product_id` ASC, `property_id` ASC);

ALTER TABLE `gizwits_lease`.`sys_message`
ADD INDEX `index_ctime` (`ctime` ASC);

