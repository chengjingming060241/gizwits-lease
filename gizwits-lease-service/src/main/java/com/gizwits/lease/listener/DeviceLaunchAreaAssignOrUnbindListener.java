package com.gizwits.lease.listener;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.gizwits.lease.device.entity.DeviceLaunchAreaAssignRecord;
import com.gizwits.lease.device.service.DeviceLaunchAreaAssignRecordService;
import com.gizwits.lease.event.DeviceLaunchAreaAssignEvent;
import com.gizwits.lease.event.source.DeviceLaunchAreaAssignSource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author lilh
 * @date 2017/9/2 17:11
 */
@Component
public class DeviceLaunchAreaAssignOrUnbindListener implements ApplicationListener<DeviceLaunchAreaAssignEvent> {

    @Autowired
    private DeviceLaunchAreaAssignRecordService deviceLaunchAreaAssignRecordService;

    @Async
    @Override
    public void onApplicationEvent(DeviceLaunchAreaAssignEvent event) {
        List<DeviceLaunchAreaAssignSource> sources = event.getDevicelaunchAreaSource();
        if (CollectionUtils.isNotEmpty(sources)) {
            List<DeviceLaunchAreaAssignRecord> records = sources.stream().map(item -> {
                DeviceLaunchAreaAssignRecord record = new DeviceLaunchAreaAssignRecord();
                record.setCtime(new Date());
                record.setDeviceLaunchAreaId(item.getLaunchAreaId());
                record.setDeviceLaunchAreaName(item.getLaunchAreaName());
                record.setSourceOwnerId(item.getSourceOwnerId());
                record.setTargetOwnerId(item.getTargetOwnerId());
                record.setOperateType(event.getType().getDesc());
                record.setSysUserId(item.getCurrent().getId());
                record.setSysUserName(item.getCurrent().getUsername());
                return record;
            }).collect(Collectors.toList());
            deviceLaunchAreaAssignRecordService.insertBatch(records);
        }
    }
}
