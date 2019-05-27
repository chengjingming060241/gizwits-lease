package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 添加设备dto
 * Created by yinhui on 2017/7/19.
 */
public class DeviceAddDto implements Serializable{

    @NotBlank
    private String name;

    @NotBlank
    private String mac;

    @NotNull
    private Integer productId;

    private String productName;

    private Integer productServiceModeId;

    private String productServiceModeName;

    private Integer deviceLaunchAreaId;
    private String deivceLaunchAreaName;

    /**
     * 出水口
     */
    private List<DeviceAddPortDto> portDtos;

    private Double totalWater;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductServiceModeId() {
        return productServiceModeId;
    }

    public void setProductServiceModeId(Integer productServiceModeId) {
        this.productServiceModeId = productServiceModeId;
    }

    public String getProductServiceModeName() {
        return productServiceModeName;
    }

    public void setProductServiceModeName(String productServiceModeName) {
        this.productServiceModeName = productServiceModeName;
    }

    public Integer getDeviceLaunchAreaId() {
        return deviceLaunchAreaId;
    }

    public void setDeviceLaunchAreaId(Integer deviceLaunchAreaId) {
        this.deviceLaunchAreaId = deviceLaunchAreaId;
    }

    public String getDeivceLaunchAreaName() {
        return deivceLaunchAreaName;
    }

    public void setDeivceLaunchAreaName(String deivceLaunchAreaName) {
        this.deivceLaunchAreaName = deivceLaunchAreaName;
    }

    public List<DeviceAddPortDto> getPortDtos() {return portDtos;}

    public void setPortDtos(List<DeviceAddPortDto> portDtos) {this.portDtos = portDtos;}

    public Double getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(Double totalWater) {
        this.totalWater = totalWater;
    }

    @Override
    public String toString() {
        return "DeviceAddDto{" +
                "name='" + name + '\'' +
                ", mac='" + mac + '\'' +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", productServiceModeId=" + productServiceModeId +
                ", productServiceModeName='" + productServiceModeName + '\'' +
                ", deviceLaunchAreaId=" + deviceLaunchAreaId +
                ", deivceLaunchAreaName='" + deivceLaunchAreaName + '\'' +
                '}';
    }
}
