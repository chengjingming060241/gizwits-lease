package com.gizwits.lease.product.dto;

import com.gizwits.lease.product.entity.ProductServiceDetail;
import org.springframework.beans.BeanUtils;

/**
 * Created by zhl on 2017/9/8.
 */
public class ProductServiceModeDetailForAppDto {
    private Integer id;
    private String serviceType;
    private Double price;
    private Double num;
    private String unit;

    public ProductServiceModeDetailForAppDto() {
    }

    public ProductServiceModeDetailForAppDto(ProductServiceDetail serviceDetail) {
        BeanUtils.copyProperties(serviceDetail,this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
