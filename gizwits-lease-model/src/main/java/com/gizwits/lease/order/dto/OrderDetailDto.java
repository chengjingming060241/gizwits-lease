package com.gizwits.lease.order.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单详情dto
 * Created by yinhui on 2017/7/20.
 */
public class OrderDetailDto implements Serializable{
    private String orderNo;
    private String userName;
    private String deviceLaunchArea;
    private String deviceMac;
    private Double pay;
    private Date payTime;
    private Integer payTypeCode;
    private String payType;
    private String serviceMode;
    private Integer status;
    private String orderStatus;
    private Date orderTime;
    private String servicetype;
    private Long deviceUseTime;
    private String abnormalReasonDesc;
    private Double quantity;
    private String unit;
    private String operator;
    private Double intakeWater;

    public String getAbnormalReasonDesc() {
        return abnormalReasonDesc;
    }

    public void setAbnormalReasonDesc(String abnormalReasonDesc) {
        this.abnormalReasonDesc = abnormalReasonDesc;
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

    public Integer getPayTypeCode() {
        return payTypeCode;
    }

    public void setPayTypeCode(Integer payTypeCode) {
        this.payTypeCode = payTypeCode;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Long getDeviceUseTime() {return deviceUseTime;}

    public void setDeviceUseTime(Long deviceUseTime) {this.deviceUseTime = deviceUseTime;}

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Double getIntakeWater() {
        return intakeWater;
    }

    public void setIntakeWater(Double intakeWater) {
        this.intakeWater = intakeWater;
    }
}
