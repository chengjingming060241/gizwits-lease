package com.gizwits.lease.ohmynoti.handler.impl;

import com.gizwits.lease.constant.AlarmStatus;
import com.gizwits.lease.constant.AlarmType;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Joke
 */
@Slf4j
@Component
public class DeviceAlertFaultHandler {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceAlarmService deviceAlarmService;

    public void handle(AlarmType alarmType, String mac, String productKey, String faultCode, String displayName, Integer handleResult) {
        if (StringUtils.isNotBlank(faultCode)) {
            Device device = deviceService.getDeviceByMacAndPk(mac, productKey);
            if (device == null) {
                log.error("====> 设备在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]");
                return;
            }

            //故障解决(当handleResult=0时, 该故障已经处理了)
            if (handleResult == 0) {
                //获取之前的未解决故障记录
                DeviceAlarm oldOne = deviceAlarmService.getUnresolveAlarm(mac, productKey, faultCode);
                if (null == oldOne) {//不存在
                    saveOneAlarm(device, productKey, faultCode, displayName, handleResult, alarmType.getCode());
                } else {
                    oldOne.setStatus(AlarmStatus.RESOLVE.getCode());
                    oldOne.setFixedTime(new Date());
                    oldOne.setUtime(new Date());
                    deviceAlarmService.updateById(oldOne);
                }
                device.setFaultStatus(DeviceStatus.NORMAL.getCode());
            } else {//新增故障
                device.setFaultStatus(DeviceStatus.FAULT.getCode());
                saveOneAlarm(device, productKey, faultCode, displayName, handleResult, alarmType.getCode());
            }
            device.setUtime(new Date());
            deviceService.updateById(device);
        }
    }

    private void saveOneAlarm(Device deviceEntity, String productKey, String faultCode, String displayName, int handleResult, Integer alarmType) {
        DeviceAlarm alarm = new DeviceAlarm();
        alarm.setCtime(new Date());
        alarm.setName(displayName);
        alarm.setAttr(faultCode);
        alarm.setHappenTime(new Date());
        alarm.setMac(deviceEntity.getMac());
        alarm.setSno(deviceEntity.getSno());
        alarm.setLongitude(deviceEntity.getLongitude());
        alarm.setLatitude(deviceEntity.getLatitude());
        alarm.setProductKey(productKey);

        if (handleResult == 0) {
            alarm.setStatus(AlarmStatus.RESOLVE.getCode());
        } else {
            alarm.setStatus(AlarmStatus.UNRESOLVE.getCode());
        }
        alarm.setAlarmType(alarmType);
        deviceAlarmService.saveOne(alarm);
    }
}