package com.gizwits.lease.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by GaGi on 2017/8/5.
 */
public class BindGizwitsDeviceEvent extends ApplicationEvent {
    private Integer productId;
    private String token;
    private String sno;
    private String username;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source   the object on which the event initially occurred (never {@code null})
     * @param username
     */
    public BindGizwitsDeviceEvent(Object source, Integer productId, String token, String sno, String username) {
        super(source);
        this.productId = productId;
        this.token = token;
        this.sno = sno;
        this.username = username;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
