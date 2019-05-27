package com.gizwits.lease.event;

import com.gizwits.lease.order.entity.OrderBase;
import org.springframework.context.ApplicationEvent;

/**
 * Created by zhl on 2017/7/19.
 */
public class WxPayCallbackEvent extends ApplicationEvent{

    public WxPayCallbackEvent(Object source, OrderBase orderBase) {
        super(source);
        this.orderBase = orderBase;
    }

    private OrderBase orderBase;

    public OrderBase getOrderBase() {
        return orderBase;
    }

    public void setOrderBase(OrderBase orderBase) {
        this.orderBase = orderBase;
    }
}
