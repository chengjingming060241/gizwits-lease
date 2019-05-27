package com.gizwits.lease.model;

import java.math.BigDecimal;

/**
 * Created by kufufu on 2017/6/29.
 */
public class DeviceAddressModel {
    private String address;
    private BigDecimal longitude;
    private BigDecimal latitude;

    private String country; // 国家
    private String province; // 省
    private String city;  //市
    private Integer cityCode; //城市编码
    private String adcode; //区域编码
    private String road; //道路名

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public DeviceAddressModel(String address, BigDecimal longitude, BigDecimal latitude) {
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public DeviceAddressModel() {
    }
}
