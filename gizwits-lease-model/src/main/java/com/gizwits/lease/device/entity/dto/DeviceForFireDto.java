package com.gizwits.lease.device.entity.dto;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 发送控制指令
 *
 * @author lilh
 * @date 2017/7/22 9:38
 */
public class DeviceForFireDto {

    /** 设备id */
    @NotBlank
    private String sno;

    private JSONObject attrs;

    /** 数据点标识名 */
    private String name;

    /** 值 */
    private Object value;

    public JSONObject getAttrs() {
        return attrs;
    }

    public void setAttrs(JSONObject attrs) {
        this.attrs = attrs;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
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
