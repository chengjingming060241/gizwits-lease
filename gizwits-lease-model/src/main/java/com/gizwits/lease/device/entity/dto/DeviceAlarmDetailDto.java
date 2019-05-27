package com.gizwits.lease.device.entity.dto;

import com.baomidou.mybatisplus.plugins.Page;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 *  设备故障详情dto
 * Created by yinhui on 2017/7/17.
 */
public class DeviceAlarmDetailDto implements Serializable {
    private DeviceAlramInfoDto deviceAlramInfoDto;
    private String deviceSno;
    private String address;
    private String province;
    private String city;
    private String area;
    private BigDecimal longtitude;
    private BigDecimal latitude;
    private String maintainerMobile; //维护人员电话
    private Page<DeviceAlramInfoDto> page;


    public DeviceAlramInfoDto getDeviceAlramInfoDto() {
        return deviceAlramInfoDto;
    }

    public void setDeviceAlramInfoDto(DeviceAlramInfoDto deviceAlramInfoDto) {
        this.deviceAlramInfoDto = deviceAlramInfoDto;
    }

    public String getDeviceSno() {
        return deviceSno;
    }

    public void setDeviceSno(String deviceSno) {
        this.deviceSno = deviceSno;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public BigDecimal getLongtitude() {return longtitude;}

    public void setLongtitude(BigDecimal longtitude) {this.longtitude = longtitude;}

    public BigDecimal getLatitude() {return latitude;}

    public void setLatitude(BigDecimal latitude) {this.latitude = latitude;}

    public String getMaintainerMobile() {
        return maintainerMobile;
    }

    public void setMaintainerMobile(String maintainerMobile) {
        this.maintainerMobile = maintainerMobile;
    }

    public Page<DeviceAlramInfoDto> getPage() {
        return page;
    }

    public void setPage(Page<DeviceAlramInfoDto> page) {
        this.page = page;
    }
}
