package com.gizwits.lease.device.entity.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.device.entity.DeviceGroup;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 设备组详情
 *
 * @author lilh
 * @date 2017/8/15 16:37
 */
public class DeviceGroupForDetailDto {

    private Integer id;

    /** 组名称 */
    private String name;

    /** 创建时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    /** 更新时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date utime;

    /** 组中的设备 */
    private List<DeviceShowDto> devicesInGroup;

    public DeviceGroupForDetailDto(DeviceGroup deviceGroup) {
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

    public void setCtime(Date ctime) {this.ctime = ctime;}

    public Date getCtime() {return ctime;}

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public List<DeviceShowDto> getDevicesInGroup() {
        return devicesInGroup;
    }

    public void setDevicesInGroup(List<DeviceShowDto> devicesInGroup) {
        this.devicesInGroup = devicesInGroup;
    }
}
