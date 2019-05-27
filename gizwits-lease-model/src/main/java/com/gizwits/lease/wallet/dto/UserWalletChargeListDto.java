package com.gizwits.lease.wallet.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 充值单列表dto
 * Created by yinhui on 2017/8/9.
 */
public class UserWalletChargeListDto implements Serializable {
    /**
     * 充值单号
     */
    private String chargeNo;
    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 充值金额
     */
    private Double fee;
    /**
     * 充值时间
     */
    private Date payTime;
    /**
     * 充值方式
     */
    private String payType;
    /**
     * 充值状态
     */
    private String status;

    public String getChargeNo() {
        return chargeNo;
    }

    public void setChargeNo(String chargeNo) {
        this.chargeNo = chargeNo;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
