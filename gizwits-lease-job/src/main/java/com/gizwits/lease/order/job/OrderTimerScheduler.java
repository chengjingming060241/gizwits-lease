package com.gizwits.lease.order.job;

import com.gizwits.boot.utils.DateKit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.config.CronConfig;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderTimer;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderTimerService;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.util.DeviceControlAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 卡励项目要求移动端可以设置定时任务
 * Created by zhl on 2017/8/11.
 */
//@Component
public class OrderTimerScheduler {

    @Autowired
    private CronConfig cronConfig;

    @Autowired
    private OrderTimerService orderTimerService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    protected static Logger logger = LoggerFactory.getLogger(OrderTimerScheduler.class);

    @Scheduled(cron = "#{cronConfig.getEvery30Sec()}")//每秒执行
    public void schedulerOrderTimer() {
        List<OrderTimer> list = orderTimerService.getAllUsingOrderTimer();
        if (!ParamUtil.isNullOrEmptyOrZero(list)) {
            for (OrderTimer orderTimer : list) {
                OrderBase orderBase = orderBaseService.selectById(orderTimer.getOrderNo());
                if (orderBase.getOrderStatus().equals(OrderStatus.USING.getCode())
                        && !ParamUtil.isNullOrEmptyOrZero(orderTimer.getCommand())) {
                    if (isRightTime(orderTimer.getWeekDay(), orderTimer.getTime())) {
                        Device device = deviceService.selectById(orderTimer.getSno());
                        logger.info("设备:"+device.getSno()+"开始执行指令:"+orderTimer.getCommand());
                        Product product = productService.selectById(device.getProductId());
                        DeviceControlAPI.remoteControl(product.getGizwitsProductKey(), device.getGizDid(), orderTimer.getCommand());
                    }
                }
            }
        }
    }

    private boolean isRightTime(String weekDays, String time) {
        if (ParamUtil.isNullOrEmptyOrZero(weekDays) || ParamUtil.isNullOrEmptyOrZero(time)) {
            return false;
        }
        if (!isNowTime(time)) {
            return false;
        }
        Calendar now = Calendar.getInstance();
        boolean isFirstSunday = (now.getFirstDayOfWeek() == Calendar.SUNDAY);
        int weekDay = now.get(Calendar.DAY_OF_WEEK);
        //若一周第一天为星期天，则-1
        if (isFirstSunday) {
            weekDay = weekDay - 1;
            if (weekDay == 0) {
                weekDay = 7;
            }
        }
        return weekDays.contains(weekDay + "");
    }

    private boolean isNowTime(String time) {
        String nowDatetime = DateKit.getTimestampString(new Date());
        return nowDatetime.indexOf(time) > 0;
    }
}
