package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 *  故障信息Dto
 * Created by yinhui on 2017/7/16.
 */
public class DeviceAlramInfoDto implements Serializable {
    private  Integer id;
    private String  sno;
    private String deviceMac;
    private String deviceName;
    private String deviceAlarmName;
    private String attr;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date happenTime;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date fixTime;
    @ApiModelProperty("故障发生时长，单位毫秒")
    private Long deviceTime;
    private Integer alarmType;
    private String deviceAlarmType;
    private Integer status;
    private String deviceAlarmStatus;
    private Integer deviceLaunchAreaId;
    private String deviceLaunchArea;
    private String mobile;
    private String peopleInchargeName;
    private Integer maintainerId;
    private String maintainer;

    /** 告警时长 */
    private String alarmDuringTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAlarmName() {
        return deviceAlarmName;
    }

    public void setDeviceAlarmName(String deviceAlarmName) {
        this.deviceAlarmName = deviceAlarmName;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public Date getHappenTime() {return happenTime;}

    public void setHappenTime(Date happenTime) {this.happenTime = happenTime;}

    public Date getFixTime() {
        return fixTime;
    }

    public void setFixTime(Date fixTime) {
        this.fixTime = fixTime;
    }

    public Long getDeviceTime() {return deviceTime;}

    public void setDeviceTime(Long deviceTime) {this.deviceTime = deviceTime;}

    public Integer getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(Integer alarmType) {
        this.alarmType = alarmType;
    }

    public String getDeviceAlarmType() {return deviceAlarmType;}

    public void setDeviceAlarmType(String deviceAlarmType) {this.deviceAlarmType = deviceAlarmType;}

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDeviceAlarmStatus() {return deviceAlarmStatus;}

    public void setDeviceAlarmStatus(String deviceAlarmStatus) {this.deviceAlarmStatus = deviceAlarmStatus;}

    public Integer getDeviceLaunchAreaId() {return deviceLaunchAreaId;}

    public void setDeviceLaunchAreaId(Integer deviceLaunchAreaId) {this.deviceLaunchAreaId = deviceLaunchAreaId;}

    public String getDeviceLaunchArea() {
        return deviceLaunchArea;
    }

    public void setDeviceLaunchArea(String deviceLaunchArea) {
        this.deviceLaunchArea = deviceLaunchArea;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPeopleInchargeName() {
        return peopleInchargeName;
    }

    public void setPeopleInchargeName(String peopleInchargeName) {
        this.peopleInchargeName = peopleInchargeName;
    }

    public Integer getMaintainerId() {
        return maintainerId;
    }

    public void setMaintainerId(Integer maintainerId) {
        this.maintainerId = maintainerId;
    }

    public String getMaintainer() {
        return maintainer;
    }

    public void setMaintainer(String maintainer) {
        this.maintainer = maintainer;
    }

    public String getAlarmDuringTime() {
        return alarmDuringTime;
    }

    public void setAlarmDuringTime(String alarmDuringTime) {
        this.alarmDuringTime = alarmDuringTime;
    }
}
