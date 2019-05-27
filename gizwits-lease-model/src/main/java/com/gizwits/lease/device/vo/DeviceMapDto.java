package com.gizwits.lease.device.vo;

import com.gizwits.lease.device.entity.DeviceLaunchArea;
import org.springframework.beans.BeanUtils;

/**
 * @author lilh
 * @date 2017/7/27 15:44
 */
public class DeviceMapDto {

    /** 投放点id */
    private Integer id;

    /** 投放点名称 */
    private String name;

    /** 投放点经度 */
    private String longitude;

    /** 投放点纬度 */
    private String latitude;

    /** 设备数量 */
    private Long deviceCount = 0L;

    public DeviceMapDto(DeviceLaunchArea deviceLaunchArea) {
        BeanUtils.copyProperties(deviceLaunchArea, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Long getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Long deviceCount) {
        this.deviceCount = deviceCount;
    }
}
