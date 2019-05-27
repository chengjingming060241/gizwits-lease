package com.gizwits.lease.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.gizwits.lease.device.entity.DeviceAssignRecord;
import com.gizwits.lease.device.entity.dto.DeviceAssignRecordDto;
import com.gizwits.lease.device.service.DeviceAssignRecordService;
import com.gizwits.lease.event.DeviceAssignEvent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 设备分配记录
 *
 * @author lilh
 * @date 2017/8/3 10:15
 */
@Component
public class DeviceAssignRecordListener implements ApplicationListener<DeviceAssignEvent> {

    @Autowired
    private DeviceAssignRecordService deviceAssignRecordService;

    @Async
    @Override
    public void onApplicationEvent(DeviceAssignEvent event) {
        List<DeviceAssignRecordDto> records = event.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<DeviceAssignRecord> assignRecords = records.stream().map(item -> resolveRecord(item, event)).collect(Collectors.toList());
            deviceAssignRecordService.insertBatch(assignRecords);
        }
    }

    private DeviceAssignRecord resolveRecord(DeviceAssignRecordDto item, DeviceAssignEvent event) {
        DeviceAssignRecord record = new DeviceAssignRecord();
        BeanUtils.copyProperties(item, record);
        record.setCtime(new Date());
        record.setSysUserId(item.getCurrent().getId());
        record.setSysUserName(item.getCurrent().getUsername());
        record.setOperateType(event.getType().getDesc());
        return record;
    }
}
