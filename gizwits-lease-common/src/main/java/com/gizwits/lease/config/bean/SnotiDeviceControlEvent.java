package com.gizwits.lease.config.bean;

import org.springframework.context.ApplicationEvent;

/**
 * @author Jcxcc
 * @date 2018/9/28
 * @since 1.0
 */
public class SnotiDeviceControlEvent extends ApplicationEvent {

    private SnotiDeviceControlDTO snotiDeviceControlDTO;

    public SnotiDeviceControlEvent(SnotiDeviceControlDTO source) {
        super(source);
        this.snotiDeviceControlDTO = source;
    }

    public SnotiDeviceControlDTO getSnotiDeviceControlDTO() {
        return snotiDeviceControlDTO;
    }
}
