package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum 是否
 * Created by yinhui on 2017/8/8.
 */
public enum IsTrueEnum {
    FALSE(0, " 否"),
    TRUE(1, "是"),;
    private Integer code;
    private String desc;
    private static Map<Integer, String> map;

    static {
        map = Arrays.stream(IsTrueEnum.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    IsTrueEnum(Integer code, String desc) {
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

    public static String getDesc(Integer code) {
        return map.get(code);
    }


}
