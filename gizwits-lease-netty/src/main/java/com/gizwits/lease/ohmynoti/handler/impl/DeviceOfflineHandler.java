package com.gizwits.lease.ohmynoti.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceRunningRecord;
import com.gizwits.lease.device.service.DeviceRunningRecordService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.noti.noticlient.bean.resp.body.OffLineEventBody;
import com.gizwits.noti.noticlient.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Jcxcc
 * @date 3/29/18
 * @email jcliu@gizwits.com
 * @since 1.0
 */
@Slf4j
@Component
public class DeviceOfflineHandler implements PushEventHandler {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private DeviceRunningRecordService deviceRunningRecordService;

    @Override
    public void processPushEventMessage(JSONObject json) {
        OffLineEventBody data = CommandUtils.parsePushEvent(json, OffLineEventBody.class);
        String
                did = data.getDid(),
                mac = data.getMac(),
                productKey = data.getProductKey();
        Long createdAt = data.getCreatedAt();

        log.info("设备下线===>  create_at:{} pk:{} did:{} mac:{}", createdAt, productKey, did, mac);
        Device offLineDevice = deviceService.getDeviceByMacAndPk(mac, productKey);
        if (offLineDevice == null) {
            log.error("====> 设备在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]");
            return;
        }

        deviceService.updateDeviceOnOffLineStatus(mac, productKey, did, false, null);
        redisService.cacheDeviceOnlineStatus(productKey, mac, false);

        //运行日志
        DeviceRunningRecord runningRecord = new DeviceRunningRecord();
        runningRecord.setCtime(new Date());
        runningRecord.setContent(json.toJSONString());
        runningRecord.setMac(mac);
        runningRecord.setWorkStatus(DeviceStatus.OFFLINE.getCode());
        runningRecord.setSno(offLineDevice.getSno());
        deviceRunningRecordService.insert(runningRecord);
    }
}
