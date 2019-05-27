package com.gizwits.lease.device.vo;

import java.io.Serializable;

/**
 * 产品管理端投放点设备统计vo
 * Created by yinhui on 2017/9/6.
 */
public class DeviceLaunchAreaCountVo implements Serializable {
    /**设备投放点id*/
    private Integer deviceLaunchAreaId;
    /**投放点名称*/
    private String areaName;
    /**设备数*/
    private Integer deviceCount;

    public Integer getDeviceLaunchAreaId() {
        return deviceLaunchAreaId;
    }

    public void setDeviceLaunchAreaId(Integer deviceLaunchAreaId) {
        this.deviceLaunchAreaId = deviceLaunchAreaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }
}
