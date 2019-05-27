package com.gizwits.lease.message.job;

import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.config.CronConfig;
import com.gizwits.lease.message.service.SysMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时发送消息
 * Created by yinhui on 2017/8/8.
 */
//@Component
public class MessageSendScheduler {
    protected static Logger logger = LoggerFactory.getLogger(MessageSendScheduler.class);

    @Autowired
    private SysMessageService sysMessageService;

    @Autowired
    private CronConfig cronConfig;

    @Scheduled(cron = "#{cronConfig.getEveryFiveMin()}")
    public void startSendMessage(){
        //开始时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(date);
        logger.info("开始发送消息时间为：" + DateKit.getTimestampString(date));
        sysMessageService.sendByTime(time);
        //结束时间
        logger.info("结束发送消息时间为：" + DateKit.getTimestampString(new Date()));
    }


}
