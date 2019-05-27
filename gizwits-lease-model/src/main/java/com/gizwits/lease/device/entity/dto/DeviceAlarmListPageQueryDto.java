package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;

import java.io.Serializable;

/**
 * 故障信息详情查询dto
 * Created by yinhui on 2017/7/17.
 */
public class DeviceAlarmListPageQueryDto implements Serializable{


    @Query(field = "sno")
    private String deviceSno;

    private Integer deviceAlarmId;

    public String getDeviceSno() {
        return deviceSno;
    }

    public void setDeviceSno(String deviceSno) {
        this.deviceSno = deviceSno;
    }

    public Integer getDeviceAlarmId() {return deviceAlarmId;}

    public void setDeviceAlarmId(Integer deviceAlarmId) {this.deviceAlarmId = deviceAlarmId;}
}
