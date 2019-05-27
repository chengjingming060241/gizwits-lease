package com.gizwits.lease.enums;

/**
 * @author lilh
 * @date 2017/7/31 16:15
 */
public enum ExistEnum {

    NOT_EXIST(0, "没有系统账号"),
    EXIST(1, "有系统账号");

    private Integer code;

    private String desc;


    ExistEnum(Integer code, String desc) {
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
