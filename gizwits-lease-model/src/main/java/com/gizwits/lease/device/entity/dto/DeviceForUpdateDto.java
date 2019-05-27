package com.gizwits.lease.device.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.lease.device.vo.DevicePortDto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Dto - 设备更新
 *
 * @author lilh
 * @date 2017/7/21 16:29
 */
public class DeviceForUpdateDto {

    /** id */
    @NotBlank
    private String sno;

    /** mac */
    private String mac;

    /** 设备名称 */
    private String name;

    /** 收费模式id */
    private Integer serviceId;

    private String serviceName;

    /** 投放点id */
    private Integer launchAreaId;

    private String launchAreaName;

    /**
     * 出水口
     */
    private List<DeviceAddPortDto> portDtos;

    /**
     * 设备到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expirationTime;

    /**
     * 设备用水总量 1-100 1代表100L
     */
    @Min(1)
    @Max(100)
    private Double totalWater;

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Integer getLaunchAreaId() {
        return launchAreaId;
    }

    public void setLaunchAreaId(Integer launchAreaId) {
        this.launchAreaId = launchAreaId;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
    }

    public List<DeviceAddPortDto> getPortDtos() {return portDtos;}

    public void setPortDtos(List<DeviceAddPortDto> portDtos) {this.portDtos = portDtos;}

    public Date getExpirationTime() { return expirationTime; }

    public void setExpirationTime(Date expirationTime) { this.expirationTime = expirationTime; }

    public Double getTotalWater() {
        return totalWater;
    }

    public void setTotalWater(Double totalWater) {
        this.totalWater = totalWater;
    }
}
