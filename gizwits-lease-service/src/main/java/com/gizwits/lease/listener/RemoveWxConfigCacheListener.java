package com.gizwits.lease.listener;

import com.gizwits.boot.event.SysUserExtUpdatedEvent;
import com.gizwits.lease.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 清除微信配置缓存数据
 *
 * @author lilh
 * @date 2017/8/16 16:59
 */
@Component
public class RemoveWxConfigCacheListener implements ApplicationListener<SysUserExtUpdatedEvent> {

    @Autowired
    private RedisService redisService;

    @Async
    @Override
    public void onApplicationEvent(SysUserExtUpdatedEvent event) {
        redisService.deleteWxConfig(event.getExt().getWxId());
        redisService.deleteWxConfig(String.valueOf(event.getExt().getSysUserId()));
    }
}
