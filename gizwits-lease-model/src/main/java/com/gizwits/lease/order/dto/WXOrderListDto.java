package com.gizwits.lease.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.product.vo.AppProductServiceDetailVo;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信订单列表dto
 * Created by yinhui on 2017/7/28.
 */

public class WXOrderListDto implements Serializable {
    private String orderNo;
    /**
     * 设备序列号
     */
    private String deviceSno;
    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备地点
     */
    private String deviceArea;
    /**
     * 收费模式名称
     */
    private String serviceModeName;
    /**
     * 租赁时长
     */
    private String duration;

    /**
     * 应付租金
     */
    private Double shouldPayMoney;
    /**
     * 实付租金
     */
    private Double payMoney;
    /**
     * 支付方式
     */
    private String payType;
    /*
    *订单状态
     */
    private String status;
    private Integer statusCode;
    private String workingMode;

    private Double price;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date payTime;
    private AppProductServiceDetailVo appProductServiceDetailVo;

    /**
     * 下单时间
     */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;
    /**
     * 订单量
     */
    private Double quality;
    /**
     * 订单完成时间
     */
    private Date finishTime;


    public AppProductServiceDetailVo getAppProductServiceDetailVo() {
        return appProductServiceDetailVo;
    }

    public void setAppProductServiceDetailVo(AppProductServiceDetailVo appProductServiceDetailVo) {
        this.appProductServiceDetailVo = appProductServiceDetailVo;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getDeviceSno() {
        return deviceSno;
    }

    public void setDeviceSno(String deviceSno) {
        this.deviceSno = deviceSno;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceArea() {
        return deviceArea;
    }

    public void setDeviceArea(String deviceArea) {
        this.deviceArea = deviceArea;
    }

    public String getServiceModeName() {
        return serviceModeName;
    }

    public void setServiceModeName(String serviceModeName) {
        this.serviceModeName = serviceModeName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Double getShouldPayMoney() {
        return shouldPayMoney;
    }

    public void setShouldPayMoney(Double shouldPayMoney) {
        this.shouldPayMoney = shouldPayMoney;
    }

    public Double getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Double payMoney) {
        this.payMoney = payMoney;
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

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Date getCtime() {
        return ctime;
    }


    public void setCtime(Date ctime) {this.ctime = ctime;}

    public Double getQuality() {return quality;}

    public void setQuality(Double quality) {this.quality = quality;}

    public Date getFinishTime() {return finishTime;}

    public void setFinishTime(Date finishTime) {this.finishTime = finishTime;}


}
