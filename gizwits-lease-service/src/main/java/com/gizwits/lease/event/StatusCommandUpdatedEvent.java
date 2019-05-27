package com.gizwits.lease.event;

import com.gizwits.lease.product.entity.ProductCommandConfig;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 状态指令更新事件
 *
 * @author lilh
 * @date 2017/8/16 18:43
 */
public class StatusCommandUpdatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = 6968076053492606411L;

    public StatusCommandUpdatedEvent(Object source) {
        super(source);
    }

    public ProductCommandConfig getCommandConfig() {
        return (ProductCommandConfig) getSource();
    }
}
