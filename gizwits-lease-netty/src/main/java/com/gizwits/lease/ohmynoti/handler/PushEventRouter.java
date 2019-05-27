package com.gizwits.lease.ohmynoti.handler;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.ohmynoti.handler.impl.*;
import com.gizwits.noti.noticlient.bean.resp.NotiRespPushEvents;
import com.gizwits.noti.noticlient.bean.resp.body.*;
import com.gizwits.noti.noticlient.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type Push event router.
 *
 * @author Jcxcc
 * @date 3 /29/18
 * @email jcliu @gizwits.com
 * @since 1.0
 */
@Slf4j
@Service
public class PushEventRouter {

    /**
     * The Event handler map.
     */
    volatile static Map<String, PushEventHandler> eventHandlerMap;

    @Autowired
    private InvalidPushEventHandler invalidPushEventHandler;

    @Autowired
    private DeviceStatusKvHandler deviceStatusKvHandler;

    @Autowired
    private DeviceStatusRawHandler deviceStatusRawHandler;

    @Autowired
    private DeviceOnlineHandler deviceOnlineHandler;

    @Autowired
    private DeviceOfflineHandler deviceOfflineHandler;

    @Autowired
    private DeviceAlertHandler deviceAlertHandler;

    @Autowired
    private DeviceFaultHandler deviceFaultHandler;
    /**
     * 一共14个事件
     */
    private void initEventHandlerMap() {
        eventHandlerMap = new ConcurrentHashMap<>(20);
        //无效
        eventHandlerMap.put(NotiRespPushEvents.INVALID.getCode(), invalidPushEventHandler);
        //设备大文件下载
        eventHandlerMap.put(NotiRespPushEvents.DOWNLOAD.getCode(), json -> {
            DownloadEventBody data = CommandUtils.parsePushEvent(json, DownloadEventBody.class);
            log.info("设备下载大文件===>pk:{} did:{} mac:{} url:{}", data.getProductKey(), data.getDid(), data.getMac(), data.getDownloadUrl());
        });
        //设备重置
        eventHandlerMap.put(NotiRespPushEvents.RESET.getCode(), json -> {
            ResetEventBody data = CommandUtils.parsePushEvent(json, ResetEventBody.class);
            log.info("设备重置===>pk:{} did:{} mac:{}", data.getProductKey(), data.getDid(), data.getMac());
        });
        //数据点改变
        eventHandlerMap.put(NotiRespPushEvents.DATA_POINTS_CHANGED.getCode(), json -> {
            DataPointChangedEventBody data = CommandUtils.parsePushEvent(json, DataPointChangedEventBody.class);
            log.info("设备数据点改变===>pk:{} did:{} mac:{}", data.getProductKey(), data.getDid(), data.getMac());
        });
        //绑定
        eventHandlerMap.put(NotiRespPushEvents.BIND.getCode(), json -> {
            BindEventBody data = CommandUtils.parsePushEvent(json, BindEventBody.class);
            log.info("绑定设备===>pk:{} did:{} mac:{} uid:{}", data.getProductKey(), data.getDid(), data.getMac(), data.getUid());
        });
        //解绑
        eventHandlerMap.put(NotiRespPushEvents.BIND.getCode(), json -> {
            UnbindEventBody data = CommandUtils.parsePushEvent(json, UnbindEventBody.class);
            log.info("解绑设备===>pk:{} did:{} mac:{} uid:{}", data.getProductKey(), data.getDid(), data.getMac(), data.getUid());
        });
        //中控添加子设备
        eventHandlerMap.put(NotiRespPushEvents.SUB_DEVICE_DELETED.getCode(), json -> {
            SubDeviceAddedEventBody data = CommandUtils.parsePushEvent(json, SubDeviceAddedEventBody.class);
            log.info("中控添加子设备===>pk:{} did:{} mac:{}", data.getProductKey(), data.getDid(), data.getMac());
        });
        //中控删除子设备
        eventHandlerMap.put(NotiRespPushEvents.SUB_DEVICE_DELETED.getCode(), json -> {
            SubDeviceDeletedEventBody data = CommandUtils.parsePushEvent(json, SubDeviceDeletedEventBody.class);
            log.info("中控删除子设备===>pk:{} did:{} mac:{}", data.getProductKey(), data.getDid(), data.getMac());
        });
        //设备状态kv
        eventHandlerMap.put(NotiRespPushEvents.DEVICE_STATUS_KV.getCode(), deviceStatusKvHandler);
        //设备状态raw
        eventHandlerMap.put(NotiRespPushEvents.DEVICE_STATUS_RAW.getCode(), deviceStatusRawHandler);
        //上线
        eventHandlerMap.put(NotiRespPushEvents.DEVICE_ONLINE.getCode(), deviceOnlineHandler);
        //下线
        eventHandlerMap.put(NotiRespPushEvents.DEVICE_OFFLINE.getCode(), deviceOfflineHandler);
        //报警
        eventHandlerMap.put(NotiRespPushEvents.ATTR_ALERT.getCode(), deviceAlertHandler);
        //故障
        eventHandlerMap.put(NotiRespPushEvents.ATTR_FAULT.getCode(), deviceFaultHandler);
    }

    /**
     * Gets push event handler.
     *
     * @param code the code
     * @return the push event handler
     */
    private PushEventHandler getPushEventHandler(String code) {
        if (Objects.isNull(eventHandlerMap)) {
            synchronized (PushEventRouter.class) {
                if (Objects.isNull(eventHandlerMap)) {
                    initEventHandlerMap();

                }
            }
        }

        return eventHandlerMap.getOrDefault(code, invalidPushEventHandler);
    }

    /**
     * Dispatch.
     *
     * @param json the json
     */
    @Async
    public void dispatch(JSONObject json) {
        if (json.containsKey("event_type") || json.containsKey("cmd")) {
            getPushEventHandler(CommandUtils.getPushEventCode(json)).processPushEventMessage(json);
        }
    }
}