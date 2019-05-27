package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 运营商和代理商状态
 *
 * @author lilh
 * @date 2017/7/31 18:19
 */
public enum StatusType {

    NEED_TO_ASSIGN(1, "待分配"),
    OPERATING(2, "正常"),
    SUSPENDED(3, "暂停");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToDesc;

    static {
        codeToDesc = Arrays.stream(StatusType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    StatusType(Integer code, String desc) {
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
