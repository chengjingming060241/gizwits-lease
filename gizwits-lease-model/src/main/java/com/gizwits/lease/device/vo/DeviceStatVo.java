package com.gizwits.lease.device.vo;

/**
 * Created by GaGi on 2017/7/12.
 */
public class DeviceStatVo {
    private Integer sysUserId;
    private Integer deviceTotal;
    private Integer deviceNewToday;
    private Integer deviceNewYesterday;
    private Double deviceNewPercent;

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getDeviceTotal() {
        return deviceTotal;
    }

    public void setDeviceTotal(Integer deviceTotal) {
        this.deviceTotal = deviceTotal;
    }

    public Integer getDeviceNewToday() {
        return deviceNewToday;
    }

    public void setDeviceNewToday(Integer deviceNewToday) {
        this.deviceNewToday = deviceNewToday;
    }

    public Integer getDeviceNewYesterday() {
        return deviceNewYesterday;
    }

    public void setDeviceNewYesterday(Integer deviceNewYesterday) {
        this.deviceNewYesterday = deviceNewYesterday;
    }

    public Double getDeviceNewPercent() {
        return deviceNewPercent;
    }

    public void setDeviceNewPercent(Double deviceNewPercent) {
        this.deviceNewPercent = deviceNewPercent;
    }
}
