package com.gizwits.lease.wallet.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 退款
 * Created by yinhui on 2017/9/1.
 */
public class RefundDto implements Serializable {
    /**订单号*/
    private String orderNo;
    /**用户名*/
    private String username;
    /**退款金额*/
    private BigDecimal money;
    /**支付账号*/
    private String payAccount;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }
}
