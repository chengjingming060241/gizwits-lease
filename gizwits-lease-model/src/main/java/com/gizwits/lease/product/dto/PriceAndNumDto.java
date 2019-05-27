package com.gizwits.lease.product.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/11.
 */
public class PriceAndNumDto implements Serializable{
    private Integer id;
    /**
     * 热水
     */
    private Double price;
    private Double num;
    /**
     * 常温
     */
    private Double normalPrice;
    private Double normalNum;

    /**
     * 冰水
     */
    private Double coldPrice;
    private Double coldNum;

    /**
     * 温开水
     */
    private Double warmPrice;
    private Double warmNum;


    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

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
