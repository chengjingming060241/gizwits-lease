package com.gizwits.lease.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event - 产品更新事件
 *
 * @author lilh
 * @date 2017/8/16 17:28
 */
public class ProductUpdatedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3460640802442445606L;

    public ProductUpdatedEvent(Object source) {
        super(source);
    }

    public String getProductKey() {
        return (String) getSource();
    }
}
