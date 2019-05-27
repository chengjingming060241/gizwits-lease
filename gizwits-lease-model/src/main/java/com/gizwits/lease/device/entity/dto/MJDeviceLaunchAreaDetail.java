package com.gizwits.lease.device.entity.dto;

import com.baomidou.mybatisplus.annotations.TableField;
import com.gizwits.lease.device.entity.DeviceLaunchArea;

import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 麻将机设备投放点详情
 * Created by yinhui on 2017/8/31.
 */
public class MJDeviceLaunchAreaDetail implements Serializable {
    /**
     * 投放点名称
     */
    private String name;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区/县
     */
    private String area;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 投放点经度
     */
    private String longitude;
    /**
     * 投放点纬度
     */
    private String latitude;
    /**
     * 负责人姓名
     */
    private String personInCharge;
    /**
     * 维护人员姓名
     */
    private String maintainerName;
    /**
     * 维护人id
     */
    private Integer maintainerId;
    /**
     * 负责人电话
     */
    private String  mobile;
    /**
     * 运营商
     */
    private Integer operatorId;
    /**
     * 运营商名称
     */
    private String operatorName;
    /**
     * 是否可以修改 0否 1是
     */
    private Integer IsModify;

    public MJDeviceLaunchAreaDetail(DeviceLaunchArea deviceLaunchArea) {
        BeanUtils.copyProperties(deviceLaunchArea,this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public String getMaintainerName() {
        return maintainerName;
    }

    public void setMaintainerName(String maintainerName) {
        this.maintainerName = maintainerName;
    }

    public Integer getMaintainerId() {
        return maintainerId;
    }

    public void setMaintainerId(Integer maintainerId) {
        this.maintainerId = maintainerId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getIsModify() {
        return IsModify;
    }

    public void setIsModify(Integer isModify) {
        IsModify = isModify;
    }
}
