package com.gizwits.lease.enums;

/**
 * 收费模式
 * Created by yinhui on 2017/9/1.
 */
public enum  ServiceType {
    FREE(1,"免费");
    Integer code;
    String desc;

    ServiceType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
