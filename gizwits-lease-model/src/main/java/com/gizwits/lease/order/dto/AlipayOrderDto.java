package com.gizwits.lease.order.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/8/16.
 */
public class AlipayOrderDto implements Serializable {
    /**支付宝用户手机号码*/
    private String mobile;
    /**订单号*/
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
