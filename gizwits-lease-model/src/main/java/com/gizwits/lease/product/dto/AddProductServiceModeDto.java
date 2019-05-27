package com.gizwits.lease.product.dto;

import java.io.Serializable;
import java.util.List;

/**
 * 添加服务模式
 * Created by yinhui on 2017/7/11.
 */
public class AddProductServiceModeDto implements Serializable{
    private String serviceMode;
    private String serviceType;
    private Integer productId;
    private String unit;
    private List<PriceAndNumDto> priceAndUnitList;


    public String getServiceMode() {return serviceMode;}

    public void setServiceMode(String serviceMode) {this.serviceMode = serviceMode;}

    public String getServiceType() {return serviceType;}

    public void setServiceType(String serviceType) {this.serviceType = serviceType;}

    public Integer getProductId() {return productId;}

    public void setProductId(Integer productId) {this.productId = productId;}

    public String getUnit() {return unit;}

    public void setUnit(String unit) {this.unit = unit;}

    public List<PriceAndNumDto> getPriceAndUnitList() {return priceAndUnitList;}

    public void setPriceAndUnitList(List<PriceAndNumDto> priceAndUnitList) {this.priceAndUnitList = priceAndUnitList;}

}