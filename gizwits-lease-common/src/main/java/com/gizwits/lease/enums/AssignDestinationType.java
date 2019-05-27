package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 分配目标
 *
 * @author lilh
 * @date 2017/8/24 11:24
 */
public enum AssignDestinationType {

    LAUNCH_AREA(1, "投放点"),
    OPERATOR(2, "运营商"),
    AGENT(3, "代理商");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToDesc;

    private static Map<Integer, AssignDestinationType> codeToType;

    static {
        codeToDesc = Arrays.stream(AssignDestinationType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
        codeToType = Arrays.stream(AssignDestinationType.values()).collect(Collectors.toMap(item -> item.code, item -> item));
    }

    AssignDestinationType(Integer code, String desc) {
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

    public static AssignDestinationType getType(Integer code) {
        return codeToType.get(code);
    }
}
