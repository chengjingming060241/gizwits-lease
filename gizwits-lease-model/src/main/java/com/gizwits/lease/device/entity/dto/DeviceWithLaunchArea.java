package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 设备分配投放点
 * Created by yinhui on 2017/9/1.
 */
public class DeviceWithLaunchArea implements Serializable {
    /**投放点id*/
    private Integer launchAreaId;
    /** 设备sno*/
    private List<String> sno;

    public Integer getLaunchAreaId() {
        return launchAreaId;
    }

    public void setLaunchAreaId(Integer launchAreaId) {
        this.launchAreaId = launchAreaId;
    }

    public List<String> getSno() {return sno;}

    public void setSno(List<String> sno) {this.sno = sno;}
}
