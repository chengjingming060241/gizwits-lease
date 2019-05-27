package com.gizwits.lease.device.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 沁尔康设备页面
 * Created by yinhui on 2017/8/24.
 */
public class DevicePortVo implements Serializable {
    private String sno;
    private List<DevicePortDto> devicePortDtoList;

    public String getSno() {return sno;}

    public void setSno(String sno) {this.sno = sno;}

    public List<DevicePortDto> getDevicePortDtoList() {return devicePortDtoList;}

    public void setDevicePortDtoList(List<DevicePortDto> devicePortDtoList) {this.devicePortDtoList = devicePortDtoList;}
}
