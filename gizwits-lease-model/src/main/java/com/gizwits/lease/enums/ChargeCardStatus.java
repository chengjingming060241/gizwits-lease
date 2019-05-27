package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lilh
 * @date 2017/8/29 16:13
 */
public enum ChargeCardStatus {

    USING(1, "使用中"),
    DISABLE(2, "停用");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToDesc;

    static {
        codeToDesc = Arrays.stream(ChargeCardStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    ChargeCardStatus(Integer code, String desc) {
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
        return codeToDesc.get(code);
    }
}
