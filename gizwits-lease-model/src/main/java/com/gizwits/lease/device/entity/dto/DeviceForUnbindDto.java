package com.gizwits.lease.device.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Dto - 设备解绑
 *
 * @author lilh
 * @date 2017/8/25 14:33
 */
public class DeviceForUnbindDto {

    /** 设备sno */
    private List<String> deviceSnos;

    /** 设备组id */
    private List<Integer> deviceGroupIds;

    /** 投放点id */
    @JsonIgnore
    private List<Integer> launchAreaIds;

    public List<String> getDeviceSnos() {
        return deviceSnos;
    }

    public void setDeviceSnos(List<String> deviceSnos) {
        this.deviceSnos = deviceSnos;
    }

    public List<Integer> getDeviceGroupIds() {
        return deviceGroupIds;
    }

    public void setDeviceGroupIds(List<Integer> deviceGroupIds) {
        this.deviceGroupIds = deviceGroupIds;
    }

    public List<Integer> getLaunchAreaIds() {
        return launchAreaIds;
    }

    public void setLaunchAreaIds(List<Integer> launchAreaIds) {
        this.launchAreaIds = launchAreaIds;
    }
}
