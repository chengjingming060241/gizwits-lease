package com.gizwits.lease.ohmynoti.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.constant.AlarmType;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import com.gizwits.noti.noticlient.bean.resp.body.FaultEventBody;
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
public class DeviceFaultHandler implements PushEventHandler {

    @Autowired
    private DeviceAlertFaultHandler deviceAlertFaultHandler;

    @Override
    public void processPushEventMessage(JSONObject json) {
        FaultEventBody data = CommandUtils.parsePushEvent(json, FaultEventBody.class);
        String
                did = data.getDid(),
                mac = data.getMac(),
                productKey = data.getProductKey();
        FaultEventBody.Data detail = data.getData();
        Long createdAt = data.getCreatedAt();

        log.info("设备故障===>create_at:{} pk:{} did:{} mac:{} detail:{}", createdAt, productKey, did, mac, JSONObject.toJSONString(detail));

        String faultCode = detail.getAttrName();
        String displayName = detail.getAttrDisplayName();
        Integer handleResult = detail.getValue();
        deviceAlertFaultHandler.handle(AlarmType.FLAUT, mac, productKey, faultCode, displayName, handleResult);
    }
}