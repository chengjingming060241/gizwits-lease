package com.gizwits.lease.product.dto;

import com.gizwits.lease.product.entity.ProductServiceMode;

import java.util.List;

/**
 * @author lilh
 * @date 2017/7/21 14:42
 */
public class ProductServiceDetailForDeviceDto {

    private Integer serviceId;

    private String serviceName;

    private String serviceType;

    private String unit;

    private Integer isFree;

    private String workingMode;


    private List<PriceAndNumDto> priceAndNumDtoList;

    public ProductServiceDetailForDeviceDto(ProductServiceMode productServiceMode) {
        this.serviceId = productServiceMode.getId();
        this.serviceName = productServiceMode.getName();
        this.serviceType = productServiceMode.getServiceType();
        this.unit = productServiceMode.getUnit();
        this.isFree = productServiceMode.getIsFree();
        this.workingMode = productServiceMode.getWorkingMode();
    }

    public Integer getIsFree() {
        return isFree;
    }

    public void setIsFree(Integer isFree) {
        this.isFree = isFree;
    }

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public List<PriceAndNumDto> getPriceAndNumDtoList() {
        return priceAndNumDtoList;
    }

    public void setPriceAndNumDtoList(List<PriceAndNumDto> priceAndNumDtoList) {
        this.priceAndNumDtoList = priceAndNumDtoList;
    }
}
