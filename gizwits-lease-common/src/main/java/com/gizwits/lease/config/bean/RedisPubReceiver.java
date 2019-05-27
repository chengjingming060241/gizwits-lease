package com.gizwits.lease.config.bean;

import com.gizwits.boot.utils.CommonEventPublisherUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CountDownLatch;

/**
 * Created by zhl on 2017/9/15.
 */
public class RedisPubReceiver {
    private Logger logger = LoggerFactory.getLogger("PRODUCT_LOGGER");

    private CountDownLatch latch;

    @Autowired
    public RedisPubReceiver(CountDownLatch latch) {
        this.latch = latch;
    }

    public void receiveMessage(String message){
        logger.info("====Received Message:{}===="+message);
        CommonEventPublisherUtils.publishEvent(new ProductUpdateNettyConfigEvent(message));
        latch.countDown();
    }
}
