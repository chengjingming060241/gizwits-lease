package com.gizwits.lease.benefit.entity.dto;

import java.io.Serializable;

/**
 * 分润详情设备dto
 * Created by yinhui on 2017/8/1.
 */
public class ShareBenefitRuleDetailDeviceDto implements Serializable {
    private String ruleDetailDeviceId;
    private String sno;
    private String deviceName;
    private String launchArea;
    private String mac;


    public String getRuleDetailDeviceId() {
        return ruleDetailDeviceId;
    }

    public void setRuleDetailDeviceId(String ruleDetailDeviceId) {
        this.ruleDetailDeviceId = ruleDetailDeviceId;
    }

    public String getSno() {return sno;}

    public void setSno(String sno) {this.sno = sno;}

    public String getDeviceName() {return deviceName;}

    public void setDeviceName(String deviceName) {this.deviceName = deviceName;}

    public String getLaunchArea() {return launchArea;}

    public void setLaunchArea(String launchArea) {this.launchArea = launchArea;}

    public String getMac() { return mac; }

    public void setMac(String mac) { this.mac = mac; }
}
