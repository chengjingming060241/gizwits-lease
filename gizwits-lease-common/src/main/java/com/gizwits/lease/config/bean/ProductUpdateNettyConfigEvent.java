package com.gizwits.lease.config.bean;

import org.springframework.context.ApplicationEvent;

/**
 * Created by zhl on 2017/9/15.
 */
public class ProductUpdateNettyConfigEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3460640802442445606L;

    public ProductUpdateNettyConfigEvent(Object source) {
        super(source);
    }

    public String getProductKey() {
        return (String) getSource();
    }

}
