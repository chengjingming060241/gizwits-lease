package com.gizwits.lease.stat.job;


import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.config.CronConfig;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.stat.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by GaGi on 2017/7/12.
 */

@Component
public class StatScheduler {
    @Autowired
    private StatDeviceOrderWidgetService statDeviceOrderWidgetService;
    @Autowired
    private StatUserWidgetService statUserWidgetService;
    @Autowired
    private StatOrderService statOrderService;
    @Autowired
    private StatDeviceTrendService statDeviceTrendService;
    @Autowired
    private StatUserTrendService statUserTrendService;
    @Autowired
    private StatUserLocationService statUserLocationService;
    @Autowired
    private StatDeviceLocationService statDeviceLocationService;

    @Autowired
    private StatFaultAlertTypeService statFaultAlertTypeService;
    @Autowired
    private RedisService redisService;

    @Autowired
    private CronConfig cronConfig;

    protected static Logger logger = LoggerFactory.getLogger(StatScheduler.class);

    @Scheduled(cron = "#{cronConfig.getOrderAnalysis()}")
    public void startOrderAnalysis() {
        //开始时间
        logger.info("开始orderAnalysis时间为：" + DateKit.getTimestampString(new Date()));
        //完成订单统计表的入库
        statOrderService.setDataForStatOrder();
        //开始时间
        logger.info("结束orderAnalysis时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getDeviceTrend()}")
    public void startDeviceTrend() {
        logger.info("开始DeviceTrend时间为：" + DateKit.getTimestampString(new Date()));
        //完成设备趋势统计入库
        statDeviceTrendService.setDataForStatDeviceTrend();

        logger.info("结束DeviceTrend时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getUserTrend()}")
    public void startUserTrend() {
        logger.info("开始UserTrend时间为：" + DateKit.getTimestampString(new Date()));
        //完成用户趋势统计入库
        statUserTrendService.setDataForStatUserTrend();

        logger.info("结束UserTrend时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getDeviceLocation()}")
    public void startDeviceLocation() {
        logger.info("开始DeviceLocation时间为：" + DateKit.getTimestampString(new Date()));
        //完成设备分布入库
        statDeviceLocationService.setDataForLocation();

        logger.info("结束DeviceLocation时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getUserLocation()}")
    public void startUserLocation() {
        logger.info("开始UserLocation时间为：" + DateKit.getTimestampString(new Date()));
        //用户分布入库
        statUserLocationService.setDataForLocation();

        logger.info("结束UserLocation时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getDeviceOrderWidget()}")
    public void startDeviceOrderWidget() {
        logger.info("开始DeviceOrderWidget时间为：" + DateKit.getTimestampString(new Date()));

        statDeviceOrderWidgetService.setDataForWidget();

        logger.info("结束DeviceOrderWidget时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getUserWidget()}")
    public void startUserWidget() {
        logger.info("开始UserWidget时间为：" + DateKit.getTimestampString(new Date()));

        statUserWidgetService.setDataForWidget();

        logger.info("结束UserWidget时间为：" + DateKit.getTimestampString(new Date()));
    }

    @Scheduled(cron = "#{cronConfig.getDaily()}")
    public void startFaultAlertType() {
        logger.info("开始FaultAlertType时间为：" + DateKit.getTimestampString(new Date()));

//        statFaultAlertTypeService.setDataForType();

        logger.info("结束FaultAlertType时间为：" + DateKit.getTimestampString(new Date()));
    }
}
