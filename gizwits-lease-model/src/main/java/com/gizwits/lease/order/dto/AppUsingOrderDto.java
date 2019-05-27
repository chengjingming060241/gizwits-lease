package com.gizwits.lease.order.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/9/12.
 */
public class AppUsingOrderDto implements Serializable{
    @NotNull
    private String sno;
    @NotNull
    private String openid;

    public AppUsingOrderDto() {}

    public AppUsingOrderDto(String sno, String openid) {
        this.sno = sno;
        this.openid = openid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
