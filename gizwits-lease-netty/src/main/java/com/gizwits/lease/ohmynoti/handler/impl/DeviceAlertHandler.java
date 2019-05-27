package com.gizwits.lease.ohmynoti.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.constant.AlarmType;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import com.gizwits.noti.noticlient.bean.resp.body.AlertEventBody;
import com.gizwits.noti.noticlient.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Jcxcc
 * @date 3/29/18
 * @email jcliu@gizwits.com
 * @since 1.0
 */
@Slf4j
@Component
public class DeviceAlertHandler implements PushEventHandler {

    @Autowired
    private DeviceAlertFaultHandler deviceAlertFaultHandler;

    @Override
    public void processPushEventMessage(JSONObject json) {
        AlertEventBody data = CommandUtils.parsePushEvent(json, AlertEventBody.class);
        String
                did = data.getDid(),
                mac = data.getMac(),
                productKey = data.getProductKey();
        AlertEventBody.Data detail = data.getData();
        Long createdAt = data.getCreatedAt();
        log.info("设备告警===> create_at :{} pk:{} did:{} mac:{} detail:{}", createdAt, productKey, did, mac, JSONObject.toJSONString(detail));

        String faultCode = detail.getAttrName();
        String displayName = detail.getAttrDisplayName();
        Integer handleResult = detail.getValue();
        deviceAlertFaultHandler.handle(AlarmType.ALERT, mac, productKey, faultCode, displayName, handleResult);
    }
}