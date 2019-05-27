package com.gizwits.lease.ohmynoti.handler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceRunningRecord;
import com.gizwits.lease.device.service.DeviceRunningRecordService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.util.DeviceControlAPI;
import com.gizwits.noti.noticlient.bean.resp.body.OnLineEventBody;
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
public class DeviceOnlineHandler implements PushEventHandler {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private DeviceRunningRecordService deviceRunningRecordService;

    @Override
    public void processPushEventMessage(JSONObject json) {
        OnLineEventBody data = CommandUtils.parsePushEvent(json, OnLineEventBody.class);
        String
                ip = data.getIp(),
                city = data.getCity(),
                region = data.getRegion(),
                country = data.getCountry(),
                did = data.getDid(),
                mac = data.getMac(),
                productKey = data.getProductKey();
        Long createdAt = data.getCreatedAt();
        log.info("设备上线===> create_at:{} pk:{} did:{} mac:{} ip:{} city:{}", createdAt, productKey, did, mac, ip, city);
        Device onlineDevice = deviceService.getDeviceByMacAndPk(mac, productKey);
        if (onlineDevice == null) {
            log.error("====> 设备在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]");
            return;
        }

        //检查设备是否有要下发的指令
        if (redisService.containsDeviceControlCommand(productKey, mac)) {
            if (DeviceControlAPI.remoteControl(productKey, did, redisService.getDeviceControlCommand(productKey, mac))) {
                redisService.removeDeviceControlCommand(productKey, mac);
            }
        }


        redisService.cacheDeviceCurrentStatus(productKey, mac, JSON.parseObject("{}"));//缓存上报数据点
        //设备时钟校准
//        orderBaseService.checkNeedClockCorrect(onlineDevice, productKey);
        deviceService.updateDeviceOnOffLineStatus(mac, productKey, did, true, null);
        redisService.cacheDeviceOnlineStatus(productKey, mac, true);

        //运行日志
        DeviceRunningRecord runningRecord = new DeviceRunningRecord();
        runningRecord.setCtime(new Date());
        runningRecord.setContent(json.toJSONString());
        runningRecord.setMac(mac);
        runningRecord.setWorkStatus(DeviceStatus.ONLINE.getCode());
        runningRecord.setSno(onlineDevice.getSno());
        deviceRunningRecordService.insert(runningRecord);
    }
}
