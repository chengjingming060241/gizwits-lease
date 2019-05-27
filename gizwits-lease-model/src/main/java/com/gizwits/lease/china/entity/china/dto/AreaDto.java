package com.gizwits.lease.china.entity.china.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/14.
 */
public class AreaDto implements Serializable{
    /**
     * 城市编码
     */
    private Integer code;
    /**
     * 城市名
     */
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
