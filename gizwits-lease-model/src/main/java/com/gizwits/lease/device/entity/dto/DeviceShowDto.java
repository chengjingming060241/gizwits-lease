package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备列表展示dto
 * Created by yinhui on 2017/7/26.
 */
public class DeviceShowDto implements Serializable{
    private String sno;
    private String mac;
    private String name;
    private String product;
    private String launchArea;
    private String serviceMode;
    private Date   lastOnLineTime;
    private String status;
    private String onlineStatus;
    private String workStatusDesc;
    
    private Integer workStatus;
    private String gizDid;

    private String province;
    private String city;
    private String area;
    private String address;


    /** 所属运营商 */
    private String belongOperatorName;

    /**
     * 设备到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 是否到期 true 即将到期 false未到期
     */
    private Boolean isTime = false;

    private Boolean lock;

    private Double totalWater;

    private Double remainWater;

    public Boolean getLock() {
        return lock;
    }

    public void setLock(Boolean lock) {
        this.lock = lock;
    }

    public String getGizDid() {
        return gizDid;
    }

    public void setGizDid(String gizDid) {
        this.gizDid = gizDid;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getLaunchArea() {
        return launchArea;
    }

    public void setLaunchArea(String launchArea) {
        this.launchArea = launchArea;
    }

    public String getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(String serviceMode) {
        this.serviceMode = serviceMode;
    }

    public Date getLastOnLineTime() {
        return lastOnLineTime;
    }

    public void setLastOnLineTime(Date lastOnLineTime) {
        this.lastOnLineTime = lastOnLineTime;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getBelongOperatorName() {
        return belongOperatorName;
    }

    public void setBelongOperatorName(String belongOperatorName) {
        this.belongOperatorName = belongOperatorName;
    }

    public String getWorkStatusDesc() {return workStatusDesc;}

    public void setWorkStatusDesc(String workStatusDesc) {this.workStatusDesc = workStatusDesc;}

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Boolean getTime() {
        return isTime;
    }

    public void setTime(Boolean time) {
        isTime = time;
    }

    public Double getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(Double totalWater) {
        this.totalWater = totalWater;
    }

    public Double getRemainWater() {
        return remainWater;
    }

    public void setRemainWater(Double remainWater) {
        this.remainWater = remainWater;
    }
}
