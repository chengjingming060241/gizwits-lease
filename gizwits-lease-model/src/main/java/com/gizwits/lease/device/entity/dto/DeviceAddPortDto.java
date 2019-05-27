package com.gizwits.lease.device.entity.dto;

import java.io.Serializable;

/**设备出水口添加
 * Created by yinhui on 2017/9/5.
 */
public class DeviceAddPortDto implements Serializable {
    private Integer id;
    /**出水口号*/
    private Integer port;
    /**出水类型*/
    private Integer waterType;
    /**排序字段*/
    private Integer sort;

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getWaterType() {
        return waterType;
    }

    public void setWaterType(Integer waterType) {
        this.waterType = waterType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}
