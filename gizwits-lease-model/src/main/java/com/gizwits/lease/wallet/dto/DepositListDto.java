package com.gizwits.lease.wallet.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * 押金列表dto
 * Created by yinhui on 2017/8/25.
 */
public class DepositListDto implements Serializable {
    /**
     * 押金单号
     */
    private String chargeOrderNo;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 支付时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 押金金额
     */
    private Double money;
    /**
     * 充值状态
     */
    private Integer status;

    private String launchAreaName;

    private String deviceName;


    public String getChargeOrderNo() {
        return chargeOrderNo;
    }

    public void setChargeOrderNo(String chargeOrderNo) {
        this.chargeOrderNo = chargeOrderNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}

