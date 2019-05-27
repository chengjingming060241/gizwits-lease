package com.gizwits.lease.listener;

import java.util.Objects;

import com.gizwits.lease.event.StatusCommandUpdatedEvent;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 清除状态指令的缓存数据
 *
 * @author lilh
 * @date 2017/8/16 19:17
 */
@Component
public class RemoveStatusCommandCacheListener implements ApplicationListener<StatusCommandUpdatedEvent> {

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    @Async
    @Override
    public void onApplicationEvent(StatusCommandUpdatedEvent event) {
        ProductCommandConfig source = event.getCommandConfig();
        Product product = productService.selectById(source.getProductId());
        if (Objects.nonNull(product)) {
            redisService.deleteProductStatusCommand(product.getGizwitsProductKey(), source.getStatusCommandType());
        }
    }
}
