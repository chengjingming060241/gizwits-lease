package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 故障信息查询条件Dto
 * Created by yinhui on 2017/7/15.
 */
public class DeviceAlarmQueryDto implements Serializable{

    private String deviceMac;
    private String deviceName;
    private String deviceLaunchArea;
    private String deviceSno;
    private String  deviceAlramName;
    private Integer status;
    private Integer productId;

    private Integer pageSize;
    private Integer currentPage;

    @JsonIgnore
    private Integer start;
    @JsonIgnore
    private Integer end;

    @JsonIgnore
    private List<String> snos;

    @JsonIgnore
    private List<Integer> ids;

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceLaunchArea() {
        return deviceLaunchArea;
    }

    public void setDeviceLaunchArea(String deviceLaunchArea) {
        this.deviceLaunchArea = deviceLaunchArea;
    }

    public String getDeviceSno() {return deviceSno;}

    public void setDeviceSno(String deviceSno) {this.deviceSno = deviceSno;}

    public String getDeviceAlramName() {
        return deviceAlramName;
    }

    public void setDeviceAlramName(String deviceAlramName) {
        this.deviceAlramName = deviceAlramName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getStart() {

        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public List<String> getSnos() {
        return snos;
    }

    public void setSnos(List<String> snos) {
        this.snos = snos;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }
}
