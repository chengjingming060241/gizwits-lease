package com.gizwits.lease.user.dto;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

/**
 * Created by GaGi on 2017/8/31.
 */
public class ChargeCardOrderDto {
    @NotEmpty
    private String openid;
//    @Range(min = 1)
    @Range(min = 0) // TODO: 改为1
    private Double fee;
    @NotEmpty
    private String cardNum;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
