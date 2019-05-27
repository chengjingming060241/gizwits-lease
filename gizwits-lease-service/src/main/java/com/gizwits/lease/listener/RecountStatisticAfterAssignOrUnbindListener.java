package com.gizwits.lease.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.dto.DeviceAssignRecordDto;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.event.DeviceAssignEvent;
import com.gizwits.lease.stat.service.StatService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 分配和解绑后重新统计
 *
 * @author lilh
 * @date 2017/8/28 10:58
 */
@Component
public class RecountStatisticAfterAssignOrUnbindListener implements ApplicationListener<DeviceAssignEvent> {

    @Autowired
    private StatService statService;

    @Autowired
    private DeviceService deviceService;

    @Async
    @Override
    public void onApplicationEvent(DeviceAssignEvent event) {
        List<DeviceAssignRecordDto> records = event.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            Map<String, Integer> snoToSourceOwnerIdMap = records.stream().collect(Collectors.toMap(DeviceAssignRecordDto::getSno, DeviceAssignRecordDto::getSourceOperator));
            List<Device> devices = resolveDevices(records);
            if (CollectionUtils.isNotEmpty(devices)) {
                devices.forEach(device -> statService.setDataForDeviceAssign(device, snoToSourceOwnerIdMap.get(device.getSno())));
            }
        }

    }

    private List<Device> resolveDevices(List<DeviceAssignRecordDto> records) {
        Set<String> snos = records.stream().map(DeviceAssignRecordDto::getSno).collect(Collectors.toSet());
        List<Device> devices = deviceService.selectBatchIds(new ArrayList<>(snos));
        if (CollectionUtils.isNotEmpty(devices)) {
            return devices;
        }
        return Collections.emptyList();
    }
}
