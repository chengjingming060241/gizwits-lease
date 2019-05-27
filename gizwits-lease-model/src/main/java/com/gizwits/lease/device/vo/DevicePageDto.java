package com.gizwits.lease.device.vo;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by GaGi on 2017/8/5.
 */
public class DevicePageDto {
    @NotEmpty
    private String sno;
    @NotEmpty
    private String orderNo;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
