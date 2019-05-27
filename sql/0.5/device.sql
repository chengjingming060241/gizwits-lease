ALTER TABLE device ADD entry_time DATETIME NULL COMMENT '入库时间';
ALTER TABLE device ADD shift_out_time DATETIME NULL COMMENT '出库时间';
ALTER TABLE device MODIFY status INT(1) NOT NULL DEFAULT '0' COMMENT '设备状态,0:入库 1:出库 2:服务中 3:暂停服务 4:已返厂 5:已报废 6:待入库(信息不完整)';