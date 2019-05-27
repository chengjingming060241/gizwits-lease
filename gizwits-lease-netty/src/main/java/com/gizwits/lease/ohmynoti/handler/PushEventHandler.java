package com.gizwits.lease.ohmynoti.handler;

import com.alibaba.fastjson.JSONObject;

/**
 * The interface Push event handler.
 *
 * @author Jcxcc
 * @date 3 /29/18
 * @email jcliu @gizwits.com
 * @since 1.0
 */
@FunctionalInterface
public interface PushEventHandler {

    /**
     * Process message.
     *
     * @param json the json
     */
    void processPushEventMessage(JSONObject json);
}