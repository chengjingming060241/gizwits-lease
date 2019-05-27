package com.gizwits.lease.device.entity.dto;

import com.gizwits.lease.device.entity.DeviceLaunchArea;

import java.io.Serializable;

/**
 * @author Jin
 * @date 2019/2/27
 */
public class DeviceLaunchAreaAndServiceDto extends DeviceLaunchArea implements Serializable {
    private static final long serialVersionUID = -4703668086825261944L;

    private String serviceType;
    private String owner;
    private Double price;
    private Double num;
    private Double normalPrice;
    private Double normalNum;
    private Double coldPrice;
    private Double coldNum;
    private Double warmPrice;
    private Double warmNum;

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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

    @Override
    public String toString() {
        return "DeviceLaunchAreaAndServiceDto{" +
                "serviceType='" + serviceType + '\'' +
                ", owner='" + owner + '\'' +
                ", price=" + price +
                ", num=" + num +
                ", normalPrice=" + normalPrice +
                ", normalNum=" + normalNum +
                ", coldPrice=" + coldPrice +
                ", coldNum=" + coldNum +
                ", warmPrice=" + warmPrice +
                ", warmNum=" + warmNum +
                '}';
    }
}
