package com.gizwits.lease.enums;

/**
 * @author lilh
 * @date 2017/8/3 11:35
 */
public enum MoveType {

    MOVE_IN_BLACK(2, "移入黑名单"),
    MOVE_OUT_BLACK(1, "移出黑名单");

    private Integer code;

    private String desc;

    MoveType(Integer code, String desc) {
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
