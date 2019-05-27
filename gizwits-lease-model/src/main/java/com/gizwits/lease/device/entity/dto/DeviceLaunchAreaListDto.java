package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;
import java.util.Objects;

import com.gizwits.lease.device.entity.DeviceLaunchArea;
import org.apache.commons.lang.StringUtils;

/**
 * 设备投放点列表dto
 * Created by yinhui on 2017/7/12.
 */
public class DeviceLaunchAreaListDto implements Serializable {
    private static final long serialVersionUID = -6530827576798169827L;

    private DeviceLaunchArea deviceLaunchArea;
    private DeviceLaunchAreaAndServiceDto deviceLaunchAreaAndServiceDto;
    private Integer deviceCount;

    /** province + city + area + address */
    private String region;

    /** 当前归属 */
    private String currentOwner;

    private boolean canModify = false;

    public DeviceLaunchArea getDeviceLaunchArea() {
        return deviceLaunchArea;
    }

    public void setDeviceLaunchArea(DeviceLaunchArea deviceLaunchArea) {
        this.deviceLaunchArea = deviceLaunchArea;
        if (Objects.nonNull(this.deviceLaunchArea)) {
            append(this.deviceLaunchArea.getProvince());
            append(this.deviceLaunchArea.getCity());
            append(this.deviceLaunchArea.getArea());
            append(this.deviceLaunchArea.getAddress());
        }
    }

    public DeviceLaunchAreaAndServiceDto getDeviceLaunchAreaAndServiceDto() {
        return deviceLaunchAreaAndServiceDto;
    }

    public void setDeviceLaunchAreaAndServiceDto(DeviceLaunchAreaAndServiceDto deviceLaunchAreaAndServiceDto) {
        this.deviceLaunchAreaAndServiceDto = deviceLaunchAreaAndServiceDto;
        if (Objects.nonNull(this.deviceLaunchAreaAndServiceDto)) {
            append(this.deviceLaunchAreaAndServiceDto.getProvince());
            append(this.deviceLaunchAreaAndServiceDto.getCity());
            append(this.deviceLaunchAreaAndServiceDto.getArea());
            append(this.deviceLaunchAreaAndServiceDto.getAddress());
        }
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public boolean isCanModify() {
        return canModify;
    }

    public void setCanModify(boolean canModify) {
        this.canModify = canModify;
    }

    private void append(String province) {
        if(Objects.isNull(this.region)) {
            this.region = "";
        }
        if(StringUtils.isNotBlank(province)) {
            this.region += province;
        }
    }
}
