package com.gizwits.lease.enums;

/**
 * @author lilh
 * @date 2017/8/26 10:20
 */
public enum ArchiveType {

    UNARCHIVE(0, "未归档"),
    ARCHIVED(1,"已归档");

    private Integer code;

    private String desc;

    ArchiveType(Integer code, String desc) {
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
