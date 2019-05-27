package com.gizwits.lease.product.dto;


import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 收费模式列表和添加收费模式Dto
 * Created by yinhui on 2017/7/11.
 */
public class ProductServicecModeListDto implements Serializable{
    /**
     * id:收费模式id
     * serviceMode：收费模式名称
     * serviceType: 收费类型
     * serviceTypeId: 收费类型ID,对应product_command_config表的ID主键
     * product：产品名称
     * productId:产品id
     * deviceCount:设备总数
     * uTime: 修改时间
     * unit：单位
     *
     */

    private Integer id;
    @NotBlank
    private String serviceMode;
    @NotBlank
    private String serviceType;
    private Integer serviceTypeId;
    private String product;
    private Integer productId;
    private Integer deviceCount;
    private Date uTime;
    private Date cTime;
    private String unit;
    private Integer ifFree;
    private String workingMode;
    private List<PriceAndNumDto> priceAndNumDtoList;

    public String getWorkingMode() {
        return workingMode;
    }

    public void setWorkingMode(String workingMode) {
        this.workingMode = workingMode;
    }

    public Integer getIfFree() {
        return ifFree;
    }

    public void setIfFree(Integer ifFree) {
        this.ifFree = ifFree;
    }

    public Integer getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(Integer serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getServiceMode() {return serviceMode;}

    public void setServiceMode(String serviceMode) {this.serviceMode = serviceMode;}

    public String getServiceType() {return serviceType;}

    public void setServiceType(String serviceType) {this.serviceType = serviceType;}

    public String getProduct() {return product;}

    public void setProduct(String product) {this.product = product;}

    public Integer getProductId() {return productId;}

    public void setProductId(Integer productId) {this.productId = productId;}

    public String getUnit() {return unit;}

    public void setUnit(String unit) {this.unit = unit;}

    public Integer getDeviceCount() {return deviceCount;}

    public void setDeviceCount(Integer deviceCount) {this.deviceCount = deviceCount;}

    public Date getuTime() {return uTime;}

    public void setuTime(Date uTime) {this.uTime = uTime;}

    public Date getcTime() {return cTime;}

    public void setcTime(Date cTime) {this.cTime = cTime;}

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PriceAndNumDto> getPriceAndNumDtoList() {return priceAndNumDtoList;}

    public void setPriceAndNumDtoList(List<PriceAndNumDto> priceAndNumDtoList) {this.priceAndNumDtoList = priceAndNumDtoList;}

    @Override
    public String toString() {
        return "ProductServicecModeListDto{" +
                "id=" + id +
                ", serviceMode='" + serviceMode + '\'' +
                ", serviceType='" + serviceType + '\'' +
                ", product='" + product + '\'' +
                ", productId=" + productId +
                ", deviceCount=" + deviceCount +
                ", uTime=" + uTime +
                ", cTime=" + cTime +
                ", unit='" + unit + '\'' +
                ", priceAndNumDtoList=" + priceAndNumDtoList +
                '}';
    }
}
