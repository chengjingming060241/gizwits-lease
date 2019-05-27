package com.gizwits.lease.order.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by GaGi on 2017/7/31.
 */
public class ChargeOrderDto {

    private String openid;

    @NotNull
    private String mobile;

    private Double fee;

    /***
     * 充值金额id
     */
    private Integer rechargeId;

    /**
     * 项目id 1艾芙芮 2卡励
     */
    private Integer projectId;

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getRechargeId() {return rechargeId;}

    public Integer getProjectId() {
        return projectId;
    }

    public void setRechargeId(Integer rechargeId) {
        this.rechargeId = rechargeId;
    }

    public void setProjectId(Integer projectId) {this.projectId = projectId;}

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}

