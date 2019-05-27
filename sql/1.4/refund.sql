alter table order_base add COLUMN `launch_area_id` int(11) DEFAULT null comment '投放点id';
alter table order_base add COLUMN `launch_area_name` VARCHAR (50) DEFAULT null comment '投放点名称';
alter table order_ext_by_quantity add COLUMN `service_type` VARCHAR (20) DEFAULT null comment '服务类型';

create table order_data_flow(
id int(11) auto_increment,
order_no VARCHAR(20) not null comment '对应的订单id',
sno VARCHAR(64) not null comment '对应的设备sno',
mac VARCHAR(64) not null comment '对应的设备mac',
route int(1) not null comment '数据方向：1业务云到设备，2设备到业务云',
type int(2) not null comment '类型：1设备原状态，2下发的指令，3设备使用中，4设备异常，5设备结束使用，6设备其他上报',
`data` text DEFAULT null comment '数据内容',
remark varchar(50) DEFAULT null comment '备注',
sys_user_id int(11) not null comment '设备拥有者',
ctime datetime DEFAULT now() comment '创建时间',
PRIMARY KEY (id),
KEY idx_order_no(order_no)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单指令跟踪表';

alter table device add COLUMN abnormal_times int(11) DEFAULT '0' comment '连续下单异常的次数';
alter table device add COLUMN `lock` tinyint(1) DEFAULT '0' comment '多次异常后锁定设备';

alter table order_base add COLUMN `abnormal_reason` tinyint(2) DEFAULT null comment '异常原因';

create table refund_apply
(
`refund_no` varchar(20) not null comment '退款单id',
`status` int(2) not null comment '状态：1待审核，2审核通过，3审核不通过，4已退款',
`order_no` varchar(20) not null comment '对应的订单id',
`amount` double(12,2) not null comment '退款金额',
`path` int(2) not null comment '退款路径，枚举值和支付类型一样',
`user_id` int(11) not null comment '用户id',
`user_mobile` varchar(50) DEFAULT null comment '用户手机号',
`user_alipay_account` varchar(50) DEFAULT null comment '用户支付宝帐号',
`user_alipay_real_name` varchar(50) DEFAULT null comment '用户支付宝真实姓名',
`refund_reason` varchar(200) DEFAULT null comment '退款原因',
`audit_reason` varchar(50) DEFAULT null comment '审核原因',
`auditor_id` int(11) DEFAULT null comment '审核人id',
`audit_time` datetime DEFAULT null comment '审核时间',
`refunder_id` int(11) DEFAULT null comment '退款人id',
`refund_time` datetime DEFAULT null comment '退款时间',
`sys_user_id` int(11) not null comment '和订单的sys_user_id一致',
`ctime` datetime DEFAULT now() comment '创建时间',
`utime` datetime DEFAULT now() comment '更新时间',
primary key (refund_no),
key idx_order_no (order_no),
key idx_user_id (user_id),
key idx_auditor_id (auditor_id),
key idx_refunder_id (refunder_id)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='退款申请表';

