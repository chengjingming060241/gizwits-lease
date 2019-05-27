ALTER TABLE `qinerkang`.`device` 
ADD COLUMN `total_water` double(10, 2) NOT NULL COMMENT '用水总量' AFTER `lock`,
ADD COLUMN `used_water` double(10, 2) NULL DEFAULT 0 COMMENT '已使用量' AFTER `total_water`;
ALTER TABLE `qinerkang`.`device` 
DROP COLUMN `used_water`,
ADD COLUMN `remain_water` double(50, 2) NULL COMMENT '剩余水量' AFTER `operate_status`;



ALTER TABLE `qinerkang`.`product_service_detail` 
ADD COLUMN `normal_price` double(8, 2) NULL COMMENT '常温单价' AFTER `is_deleted`,
ADD COLUMN `normal_num` double(8, 2) NULL COMMENT '常温数量' AFTER `normal_price`;



ALTER TABLE `qinerkang`.`device` 
ADD COLUMN `operate_status` smallint(1) NULL DEFAULT 0 COMMENT '设备是否投入运营 0:否 1:是' AFTER `used_water`;


INSERT INTO `sys_permission` VALUES (789, '2019-02-12 17:17:37', '2019-02-12 17:17:37', '/device/deviceRunningRecord/offlineList', '离线列表', 138, 'string', '/device/deviceRunningRecord/offlineList', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (790, '2019-02-14 10:41:04', '2019-02-14 10:41:04', '/device/device/changeOperateStatus', '控制设备是否投入运营', 120, 'string', '/device/device/changeOperateStatus', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (791, '2019-02-15 16:32:35', '2019-02-15 16:32:35', '/device/deviceLaunchArea/sumCharge', '统计投放点充值订单', 60, 'string', '/device/deviceLaunchArea/sumCharge', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (792, '2019-02-18 11:44:57', '2019-02-18 11:44:57', '/agent/resetPwd', '代理商重置密码', 263, 'string', '/agent/resetPwd', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (793, '2019-02-18 11:53:01', '2019-02-18 11:53:01', '/operator/resetPwd', '运营商重置密码', 182, 'string', '/operator/resetPwd', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (794, '2019-02-25 16:49:26', '2019-02-25 16:49:26', '/stat/statUsingWater/getHourAnalysis', '用水时段分析', 68, 'string', '/stat/statUsingWater/getHourAnalysis', 1, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (795, '2019-02-25 16:50:17', '2019-02-25 16:50:17', '/stat/statUsingWater/getIntervalAnalysis', '按区间用水分析', 794, 'string', '/stat/statUsingWater/getIntervalAnalysis', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (796, '2019-02-25 17:56:15', '2019-02-25 17:56:15', '/device/deviceRunningRecord/getUnreadOffline', '未读离线记录数', 138, 'string', '/device/deviceRunningRecord/getUnreadOffline', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (797, '2019-02-28 14:00:19', '2019-02-28 14:00:19', '/device/device/resetFilter', '重置滤芯寿命', 138, 'string', '/device/device/resetFilter', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (798, '2019-03-05 09:56:48', '2019-03-05 09:56:48', '/stat/statDeviceOrderWidget/index', '首页看板数据', 70, 'string', '/stat/statDeviceOrderWidget/index', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (799, '2019-03-05 11:56:34', '2019-03-05 11:56:34', '/wallet/userWalletChargeOrder/rechargeData', '充值列表看板数据', 260, 'string', '/wallet/userWalletChargeOrder/rechargeData', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (800, '2019-03-06 11:00:49', '2019-03-06 11:00:49', '/operator/getAllParentOperator', '获取所有父级运营商', 182, 'string', '/operator/getAllParentOperator', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (801, '2019-03-06 11:01:40', '2019-03-06 11:01:40', '/getAllChildOperatorById', '根据父运营商获取下面子运营商', 182, 'string', '/operator/getAllChildOperatorById', 2, 0, 1, 'admin', 0);
INSERT INTO `sys_permission` VALUES (802, '2019-03-08 13:56:42', '2019-03-08 13:56:42', '/stat/statDeviceOrderWidget/operatorSort', '运营商总金额排序看板', 70, 'string', '/stat/statDeviceOrderWidget/operatorSort', 2, 0, 1, 'admin', 0);



ALTER TABLE `qinerkang`.`user_wallet_charge_order` 
ADD COLUMN `launch_area_name` varchar(64) NULL COMMENT '用户最后一次消费投放点' AFTER `user_id`,
ADD COLUMN `device_name` varchar(64) NULL COMMENT '用户最后一次消费机器' AFTER `launch_area_name`;

ALTER TABLE `qinerkang`.`device_running_record` 
ADD COLUMN `is_read` int(1) NOT NULL DEFAULT 0 COMMENT '是否已读 0-未读 1-已读' AFTER `content`;

ALTER TABLE `qinerkang`.`device_ext_port` 
MODIFY COLUMN `port_type` int(1) NULL DEFAULT NULL COMMENT '出水类型：1常温，2热水，3冰水，4温水' AFTER `port`;

ALTER TABLE `qinerkang`.`device_launch_area` 
ADD COLUMN `service_mode_id` int(11) NULL COMMENT '收费模式的id' AFTER `owner_id`,
ADD COLUMN `service_mode_name` varchar(20) NULL COMMENT '收费模式名称' AFTER `service_mode_id`;

ALTER TABLE `qinerkang`.`order_base` 
ADD COLUMN `real_recharge` double(11, 2) NULL COMMENT '用户此时实际充值金额' AFTER `refund_result`,
ADD COLUMN `real_discount` double(11, 2) NULL COMMENT '用户此时实际赠送金额' AFTER `real_recharge`,
ADD COLUMN `intake_water` double(11, 2) NULL COMMENT '取水量' AFTER `real_discount`;



INSERT INTO `qinerkang`.`panel`(`id`, `utime`, `ctime`, `type`, `module`, `module_item`, `sort`) VALUES (33, '2019-03-05 09:45:36', '2019-03-05 09:45:40', 1, 3, 33, 33)
INSERT INTO `qinerkang`.`panel`(`id`, `utime`, `ctime`, `type`, `module`, `module_item`, `sort`) VALUES (34, '2019-03-05 09:59:18', '2019-03-05 09:59:22', 1, 1, 34, 34)
INSERT INTO `qinerkang`.`panel`(`id`, `utime`, `ctime`, `type`, `module`, `module_item`, `sort`) VALUES (35, '2019-03-05 10:04:03', '2019-03-05 10:04:07', 1, 1, 35, 35)
INSERT INTO `qinerkang`.`panel`(`id`, `utime`, `ctime`, `type`, `module`, `module_item`, `sort`) VALUES (36, '2019-03-05 10:04:41', '2019-03-05 10:04:45', 1, 1, 36, 36)




ALTER TABLE `qinerkang`.`stat_order` 
ADD COLUMN `order_amount_wx` double(12, 2) NULL DEFAULT 0.00 COMMENT '订单总金额微信' AFTER `ordered_percent`,
ADD COLUMN `order_count_wx` int(11) NULL DEFAULT 0 COMMENT '订单数量微信' AFTER `order_amount_wx`,
ADD COLUMN `order_amount_wallet` double(12, 2) NULL DEFAULT 0.00 COMMENT '订单总金额钱包' AFTER `order_count_wx`,
ADD COLUMN `order_count_wallet` int(11) NULL DEFAULT 0 COMMENT '订单数量钱包' AFTER `order_amount_wallet`;




