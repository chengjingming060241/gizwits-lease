package com.gizwits.lease.order.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/8/15.
 */
public class DepositOrderDto {
    @NotNull
    private String sno;
    @NotNull
    private String mobile;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
