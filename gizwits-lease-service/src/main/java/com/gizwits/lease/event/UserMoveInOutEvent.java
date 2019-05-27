package com.gizwits.lease.event;

import org.springframework.context.ApplicationEvent;

/**
 * Event - 用户移入/出黑名单
 *
 * @author lilh
 * @date 2017/8/3 12:32
 */
public class UserMoveInOutEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1423737084805616486L;

    public UserMoveInOutEvent(Object source) {
        super(source);
    }
}
