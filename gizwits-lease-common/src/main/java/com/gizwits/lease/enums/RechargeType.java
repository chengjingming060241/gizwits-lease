package com.gizwits.lease.enums;

/**
 * Created by yinhui on 2017/8/17.
 */
public enum RechargeType {
    FIXED(1,"固定金额"),
    CUSTOM(2,"自定义"),
    ;

    Integer code;
    String mess;

    RechargeType(Integer code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }
}
