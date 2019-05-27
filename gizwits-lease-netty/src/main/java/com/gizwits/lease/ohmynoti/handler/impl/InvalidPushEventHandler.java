package com.gizwits.lease.ohmynoti.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 无效推送事件处理
 *
 * @author Jcxcc
 * @date 3/29/18
 * @email jcliu@gizwits.com
 * @since 1.0
 */
@Slf4j
@Component
public class InvalidPushEventHandler implements PushEventHandler {
    @Override
    public void processPushEventMessage(JSONObject json) {
        log.info("推送事件无效===>{}", json.toJSONString());
    }
}