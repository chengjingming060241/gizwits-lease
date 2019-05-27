package com.gizwits.lease.wallet.dto;

/**
 * Created by GaGi on 2017/8/4.
 */

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 用户交易dto
 */
public class WalletPayDto implements Serializable{
    @NotNull
    private String mobile;
    @NotNull
    private String orderNo;

    public String getMobile() {return mobile;}

    public void setMobile(String mobile) {this.mobile = mobile;}

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
