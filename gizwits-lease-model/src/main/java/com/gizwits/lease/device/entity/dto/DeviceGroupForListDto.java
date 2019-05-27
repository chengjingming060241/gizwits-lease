package com.gizwits.lease.device.entity.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.device.entity.DeviceGroup;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 设备分组列表
 *
 * @author lilh
 * @date 2017/8/15 15:26
 */
public class DeviceGroupForListDto {

    private Integer id;

    /** 组名称 */
    private String name;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date utime;

    /** 组中的设备数 */
    private Integer deviceCount = 0;

    /** 组中的设备 */
    private List<String> deviceIds;

    /**当前归属人*/
    private String assignedName;

    public DeviceGroupForListDto(DeviceGroup deviceGroup) {
        BeanUtils.copyProperties(deviceGroup, this);
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

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public List<String> getDeviceIds() {
        return deviceIds;
    }

    public void setDeviceIds(List<String> deviceIds) {
        this.deviceIds = deviceIds;
    }

    public String getAssignedName() {return assignedName;}

    public void setAssignedName(String assignedName) {this.assignedName = assignedName;}
}
