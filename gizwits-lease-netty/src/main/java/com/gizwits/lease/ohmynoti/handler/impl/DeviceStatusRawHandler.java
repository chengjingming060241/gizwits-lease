package com.gizwits.lease.ohmynoti.handler.impl;


import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import com.gizwits.noti.noticlient.bean.resp.body.StatusRAWEventBody;
import com.gizwits.noti.noticlient.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Jcxcc
 * @date 3/29/18
 * @email jcliu@gizwits.com
 * @since 1.0
 */
@Slf4j
@Component
public class DeviceStatusRawHandler implements PushEventHandler {
    @Override
    public void processPushEventMessage(JSONObject json) {
        StatusRAWEventBody data = CommandUtils.parsePushEvent(json, StatusRAWEventBody.class);
        String
                did = data.getDid(),
                mac = data.getMac(),
                raw = data.getData(),
                productKey = data.getProductKey();
        log.info("设备透传===>pk:{} did:{} mac:{} raw:{}", productKey, did, mac, raw);
        //todo 透传处理
    }
}