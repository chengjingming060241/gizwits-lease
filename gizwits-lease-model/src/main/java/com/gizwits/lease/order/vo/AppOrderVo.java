package com.gizwits.lease.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.vo.AppProductServiceDetailVo;
import com.gizwits.lease.product.vo.ProductServiceDetailVo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by GaGi on 2017/8/3.
 */
public class AppOrderVo {
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date ctime;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payTime;
    private String orderNo;
    private String sno;
    private String address;
    private String duration;
    private String unit;
    private BigDecimal payMoney;
    private Integer orderStatus;
    private String username;
    private String deviceName;
    private String mac;
    private String deviceLaunchName;
    private String workingMode;
    private Integer statusCode;
    private String status;
    private ProductServiceDetailVo serviceModeDetail;
    private AppProductServiceDetailVo appServiceModeDetailDto;

    public AppProductServiceDetailVo getAppServiceModeDetailDto() {
        return appServiceModeDetailDto;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAppServiceModeDetailDto(AppProductServiceDetailVo appServiceModeDetailDto) {
        this.appServiceModeDetailDto = appServiceModeDetailDto;
    }

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
    }

    public String getDeviceLaunchName() {
        return deviceLaunchName;
    }

    public void setDeviceLaunchName(String deviceLaunchName) {
        this.deviceLaunchName = deviceLaunchName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public ProductServiceDetailVo getServiceModeDetail() {
        return serviceModeDetail;
    }

    public void setServiceModeDetail(ProductServiceDetailVo serviceModeDetail) {
        this.serviceModeDetail = serviceModeDetail;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void buildOrderVo(OrderBase orderBase, ProductServiceMode serviceMode, DeviceLaunchArea area, Device device) {

    }
}
