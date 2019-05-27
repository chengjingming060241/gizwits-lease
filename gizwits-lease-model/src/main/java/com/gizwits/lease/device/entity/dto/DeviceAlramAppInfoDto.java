package com.gizwits.lease.device.entity.dto;

/**
 * Created by xian on 30/8/2017.
 */
public class DeviceAlramAppInfoDto extends DeviceAlramInfoDto {
    private String maintainerMobile; //维护人员电话
    private Integer work_status; //设备工作状态,2离线,3:使用中,4:空闲 5:禁用 6:故障 7待机
    private String deviceWorkStatus;

    public void setMaintainerMobile(String maintainerMobile) {
        this.maintainerMobile = maintainerMobile;
    }

    public void setWork_status(Integer work_status) {
        this.work_status = work_status;
    }

    public void setDeviceWorkStatus(String deviceWorkStatus) {
        this.deviceWorkStatus = deviceWorkStatus;
    }

    public Integer getWork_status() {
        return work_status;
    }

    public String getDeviceWorkStatus() {
        return deviceWorkStatus;
    }

    public String getMaintainerMobile() {
        return maintainerMobile;
    }

    public static String convertWorkStatusToString(Integer status) {
        if (status == 2) {
            return "离线";
        }else if (status == 3) {
            return "使用中";
        }else if (status == 4) {
            return "空闲";
        }else if (status == 5) {
            return "禁用";
        }else if (status == 6) {
            return "故障";
        }else if (status == 7) {
            return "待机";
        }else {
            return "";
        }
    }
}


