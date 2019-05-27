package com.gizwits.lease.enums;

/**
 * Created by zhl on 2017/9/6.
 */
public enum CoverLevel {
    COUNTRY(1, "国家级"),
    PROVINCE(2, "省级"),
    CITY(3, "市级"),
    DISTRICT(4, "区县级"),;

    private Integer code;

    private String desc;


    CoverLevel(Integer code, String desc) {
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
