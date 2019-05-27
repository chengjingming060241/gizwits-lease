package com.gizwits.lease.order.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;

/**
 * Created by yinhui on 2017/7/20.
 */
public class OrderBaseListDto implements Serializable{

    /** 订单号 */
    private String orderNo;

    /** 用户昵称 */
    private String userName;

    /** 投放点名称 */
    private String deviceLaunchArea;

    /** 对应设备序列号 **/
    private String sno;

    /** 设备mac */
    private String deviceMac;

    /** 支付费用 */
    private Double pay;

    /** 支付时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date payTime;

    /** 服务方式id */
    private String payType;

    /** 收费模式 **/
    private Integer service_mode_id;

    /** 收费名称 */
    private String serviceMode;

    /** 状态 */
    private Integer status;

    /** 异常原因 */
    private Integer abnormalReason;

    /** 用户手机 */
    private String mobile;

    private String operator;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getAbnormalReason() {
        return abnormalReason;
    }

    public void setAbnormalReason(Integer abnormalReason) {
        this.abnormalReason = abnormalReason;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceLaunchArea() {
        return deviceLaunchArea;
    }

    public void setDeviceLaunchArea(String deviceLaunchArea) {
        this.deviceLaunchArea = deviceLaunchArea;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public Double getPay() {
        return pay;
    }

    public void setPay(Double pay) {
        this.pay = pay;
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

    public Integer getService_mode_id() {
        return service_mode_id;
    }

    public void setService_mode_id(Integer service_mode_id) {
        this.service_mode_id = service_mode_id;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
