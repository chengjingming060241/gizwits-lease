package com.gizwits.lease.device.vo;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/8/24.
 */
public class DevicePortDto implements Serializable {
    private Integer id;
    /**出水口号*/
    private Integer port;
    /**状态*/
    private String status;
    /**出水类型*/
    private Integer water;
    /**排序字段*/
    private Integer sort;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getPort() {return port;}

    public void setPort(Integer port) {this.port = port;}

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public Integer getWater() {return water;}

    public void setWater(Integer water) {this.water = water;}

    public Integer getSort() {return sort;}

    public void setSort(Integer sort) {this.sort = sort;}
}
