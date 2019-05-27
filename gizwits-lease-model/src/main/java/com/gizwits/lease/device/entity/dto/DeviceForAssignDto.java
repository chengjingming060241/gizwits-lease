package com.gizwits.lease.device.entity.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceGroup;
import com.gizwits.lease.device.entity.DeviceLaunchArea;

/**
 * Dto - 设备分配
 *
 * @author lilh
 * @date 2017/8/2 15:20
 */
public class DeviceForAssignDto {

    /** 分配的设备ids */
    private List<String> deviceSnos;

    /** 1,投放点 2,运营商 3,代理商 */
    private Integer assignDestinationType;

    /** 运营商或代理商或投放点id */
    private Integer assignedId;

    /**
     * 强制进行分润操作
     */
    private Boolean forceAssign;

    /** 设备组id */
    private List<Integer> deviceGroupIds;

    /**
     * 设备
     */
    @JsonIgnore
    private List<Device> devices;

    /** assignedId对应的系统账号 */
    @JsonIgnore
    private Integer resolvedAccountId;

    /** 运营商或代理商的名称 */
    @JsonIgnore
    private String assignedName;

    /** 设备组 */
    @JsonIgnore
    private List<DeviceGroup> deviceGroups;

    @JsonIgnore
    private Boolean isOperator;

    @JsonIgnore
    private Boolean isAgent;

    /** 投放点id */
    @JsonIgnore
    private List<Integer> launchAreaIds;

    /** 投放点 */
    @JsonIgnore
    private List<DeviceLaunchArea> deviceLaunchAreas;

    /** 设备到投放点的映射 */
    @JsonIgnore
    private Map<String, DeviceLaunchArea> snoToDeviceLaunchArea;

    public Boolean getForceAssign() {
        return forceAssign;
    }

    public void setForceAssign(Boolean forceAssign) {
        this.forceAssign = forceAssign;
    }

    public List<String> getDeviceSnos() {
        return deviceSnos;
    }

    public void setDeviceSnos(List<String> deviceSnos) {
        this.deviceSnos = deviceSnos;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public Integer getAssignDestinationType() {
        return assignDestinationType;
    }

    public void setAssignDestinationType(Integer assignDestinationType) {
        this.assignDestinationType = assignDestinationType;
    }

    public Integer getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(Integer assignedId) {
        this.assignedId = assignedId;
    }

    public Integer getResolvedAccountId() {
        return resolvedAccountId;
    }

    public void setResolvedAccountId(Integer resolvedAccountId) {
        this.resolvedAccountId = resolvedAccountId;
    }

    public List<Integer> getDeviceGroupIds() {
        return deviceGroupIds;
    }

    public void setDeviceGroupIds(List<Integer> deviceGroupIds) {
        this.deviceGroupIds = deviceGroupIds;
    }

    public List<DeviceGroup> getDeviceGroups() {
        return deviceGroups;
    }

    public void setDeviceGroups(List<DeviceGroup> deviceGroups) {
        this.deviceGroups = deviceGroups;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }

    public Boolean getIsOperator() {
        return isOperator;
    }

    public void setIsOperator(Boolean isOperator) {
        this.isOperator = isOperator;
    }

    public Boolean getIsAgent() {
        return isAgent;
    }

    public void setIsAgent(Boolean isAgent) {
        this.isAgent = isAgent;
    }

    public List<Integer> getLaunchAreaIds() {
        return launchAreaIds;
    }

    public void setLaunchAreaIds(List<Integer> launchAreaIds) {
        this.launchAreaIds = launchAreaIds;
    }

    public List<DeviceLaunchArea> getDeviceLaunchAreas() {
        return deviceLaunchAreas;
    }

    public void setDeviceLaunchAreas(List<DeviceLaunchArea> deviceLaunchAreas) {
        this.deviceLaunchAreas = deviceLaunchAreas;
    }

    public Map<String, DeviceLaunchArea> getSnoToDeviceLaunchArea() {
        return snoToDeviceLaunchArea;
    }

    public void setSnoToDeviceLaunchArea(Map<String, DeviceLaunchArea> snoToDeviceLaunchArea) {
        this.snoToDeviceLaunchArea = snoToDeviceLaunchArea;
    }
}
