package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.lease.device.entity.Device;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 麻将机设备详情
 * Created by yinhui on 2017/8/30.
 */
public class MJDeviceDetailDto implements Serializable {
    /**
     * 设备sno
     */
    private String sno;
    /**
     * 设备名称
     */
    private String name;
    /**
     * 收费模式id
     */
    private Integer serviceId;
    /**
     * 收费模式
     */
    private String serviceName;
    /**
     * 投放点id
     */
    private Integer launchAreaId;
    /**
     * 投放点名称
     */
    private String launchAreaName;
    /**
     * 电压
     */
    private String voltage;
    /**
     * 开关
     */
    private Integer isOpen;
    /**
     * 状态
     */
    @JsonIgnore
    private Integer workStatus;
    /**
     * 设备状态
     */
    private String workStatusDesc;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 纬度
     */
    private BigDecimal latitude;
    /**
     * 是否可修改收费模式
     */
    private Integer isModifyServiceMode;
    /**
     * 是否可修改投放点
     */
    private Integer isModifyLaunchArea;

    private String province;
    private String city;
    private String area;
    private String address;

    private Integer onlineStatus;
    private String onlineStatusDesc;

    public MJDeviceDetailDto(Device device) {
        BeanUtils.copyProperties(device, this);

    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getLaunchAreaId() {
        return launchAreaId;
    }

    public void setLaunchAreaId(Integer launchAreaId) {
        this.launchAreaId = launchAreaId;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
    }

    public String getVoltage() {return voltage;}

    public void setVoltage(String voltage) {this.voltage = voltage;}

    public Integer getIsOpen() {return isOpen;}

    public void setIsOpen(Integer isOpen) {this.isOpen = isOpen;}

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public String getWorkStatusDesc() {
        return workStatusDesc;
    }

    public void setWorkStatusDesc(String workStatusDesc) {
        this.workStatusDesc = workStatusDesc;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public Integer getIsModifyServiceMode() {
        return isModifyServiceMode;
    }

    public void setIsModifyServiceMode(Integer isModifyServiceMode) {
        this.isModifyServiceMode = isModifyServiceMode;
    }

    public Integer getIsModifyLaunchArea() {
        return isModifyLaunchArea;
    }

    public void setIsModifyLaunchArea(Integer isModifyLaunchArea) {
        this.isModifyLaunchArea = isModifyLaunchArea;
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

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getOnlineStatusDesc() {
        return onlineStatusDesc;
    }

    public void setOnlineStatusDesc(String onlineStatusDesc) {
        this.onlineStatusDesc = onlineStatusDesc;
    }
}