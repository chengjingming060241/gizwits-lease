package com.gizwits.lease.order.dto;


import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/4/25.
 */
public class WxOrderDto {
    @NotNull
    private String openid;
    @NotNull
    private String deviceId;
    private Double quantity;
    private String remark;
    private String param;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }
}
