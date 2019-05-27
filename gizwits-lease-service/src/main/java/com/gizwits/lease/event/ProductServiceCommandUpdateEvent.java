package com.gizwits.lease.event;

import com.gizwits.lease.product.entity.ProductCommandConfig;
import org.springframework.context.ApplicationEvent;

/**
 * Created by zhl on 2017/9/15.
 */
public class ProductServiceCommandUpdateEvent extends ApplicationEvent {
    public ProductServiceCommandUpdateEvent(Object source) {
        super(source);
    }

    public ProductCommandConfig getCommandConfig(){
        return (ProductCommandConfig) getSource();
    }
}
