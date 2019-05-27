package com.gizwits.lease.enums;

/**
 * @author lilh
 * @date 2017/8/14 16:19
 */
public enum MutexType {

    NO(0, "否"),
    YES(1, "是");

    private Integer code;

    private String desc;

    MutexType(Integer code, String desc) {
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
