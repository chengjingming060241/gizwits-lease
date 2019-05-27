package com.gizwits.lease.constant;

/**
 * Created by zhl on 2017/8/9.
 */
public enum BooleanEnum {
    TRUE(1,"是"),
    FALSE(0,"否");

    Integer code;
    String name;

    BooleanEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
