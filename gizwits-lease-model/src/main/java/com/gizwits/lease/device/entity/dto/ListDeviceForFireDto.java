package com.gizwits.lease.device.entity.dto;

import com.alibaba.fastjson.JSONObject;

import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yinhui on 2017/9/2.
 */
public class ListDeviceForFireDto implements Serializable{
    /** 设备id */
    @NotEmpty
    private List<String> snos;

    private JSONObject attrs;

    /** 数据点标识名 */
    private String name;

    /** 值 */
    private Object value;

    public List<String> getSnos() {
        return snos;
    }

    public void setSnos(List<String> snos) {
        this.snos = snos;
    }

    public JSONObject getAttrs() {
        return attrs;
    }

    public void setAttrs(JSONObject attrs) {
        this.attrs = attrs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
