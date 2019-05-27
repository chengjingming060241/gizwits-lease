package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lilh
 * @date 2017/8/29 17:44
 */
public enum ChargeCardOperationType {

    ENABLE("ENABLE", "启用"),
    DISABLE("DISABLE", "禁用");

    private String code;

    private String desc;

    private static Map<String, String> codeToDesc;

    static {
        codeToDesc = Arrays.stream(ChargeCardOperationType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    ChargeCardOperationType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(String code) {
        return codeToDesc.get(code);
    }
}
