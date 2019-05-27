package com.gizwits.lease.device.entity.dto;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Description:产品指令显示
 * User: yinhui
 * Date: 2018-01-16
 */
public class DeviceProductShowDto {
    private Integer showType;
    private  JSONObject jsonObject;

    private String range;

    public Integer getShowType() {
        return showType;
    }

    public void setShowType(Integer showType) {
        this.showType = showType;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public String getRange() { return range; }

    public void setRange(String range) { this.range = range; }
}
