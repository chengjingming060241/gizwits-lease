package com.gizwits.lease.order.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by GaGi on 2017/7/31.
 */
public class PrePayDto {
    @NotNull
    private String orderNo;
    private String openid;
    private String sno;
    private String mobile;

    @NotNull
    private Integer payType;

    public Integer getPayType() {
        return payType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }
}
