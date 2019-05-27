package com.gizwits.lease.product.vo;

import com.gizwits.lease.product.entity.ProductServiceDetail;

/**
 * Created by GaGi on 2017/8/4.
 */
public class ProductServiceDetailVo {
    private Integer id;
    private Double num;
    private String unit;
    private Double price;
    private Double normalPrice;
    private Double normalNum;
    private Double coldPrice;
    private Double coldNum;
    private Double warmPrice;
    private Double warmNum;

    /**
     * 收费类型
     */
    private String serviceType;

    public ProductServiceDetailVo() {
    }

    public ProductServiceDetailVo(ProductServiceDetail productServiceDetail) {
        this.id = productServiceDetail.getId();
        this.num = productServiceDetail.getNum();
        this.unit = productServiceDetail.getUnit();
        this.price = productServiceDetail.getPrice();
        this.normalPrice = productServiceDetail.getNormalPrice();
        this.normalNum = productServiceDetail.getNormalNum();
        this.coldPrice = productServiceDetail.getColdPrice();
        this.coldNum = productServiceDetail.getColdNum();
        this.warmPrice = productServiceDetail.getWarmPrice();
        this.warmNum = productServiceDetail.getWarmNum();
        this.serviceType = productServiceDetail.getServiceType();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public Double getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(Double normalPrice) {
        this.normalPrice = normalPrice;
    }

    public Double getNormalNum() {
        return normalNum;
    }

    public void setNormalNum(Double normalNum) {
        this.normalNum = normalNum;
    }

    public Double getColdPrice() {
        return coldPrice;
    }

    public void setColdPrice(Double coldPrice) {
        this.coldPrice = coldPrice;
    }

    public Double getColdNum() {
        return coldNum;
    }

    public void setColdNum(Double coldNum) {
        this.coldNum = coldNum;
    }

    public Double getWarmPrice() {
        return warmPrice;
    }

    public void setWarmPrice(Double warmPrice) {
        this.warmPrice = warmPrice;
    }

    public Double getWarmNum() {
        return warmNum;
    }

    public void setWarmNum(Double warmNum) {
        this.warmNum = warmNum;
    }
}
