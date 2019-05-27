package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhl on 2017/9/12.
 */
public enum  ShareRuleDetailType {
    ALL("ALL","全部"),
    SINGLE("SINGLE","个别设备"),
    ;

    String code;
    String mess;

    ShareRuleDetailType(String code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    private static Map<String, String> codeToName;

    static {
        codeToName = Arrays.stream(ShareRuleDetailType.values()).collect(Collectors.toMap(item -> item.code, item -> item.mess));
    }

    public static String getDescription(String code) {
        return codeToName.get(code);
    }
}
