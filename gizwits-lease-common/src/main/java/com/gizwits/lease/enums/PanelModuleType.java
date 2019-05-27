package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 面板模块分类
 *
 * @author lilh
 * @date 2017/7/15 15:25
 */
public enum PanelModuleType {

    DEVICE(1, "设备分析"),
    USER(2, "用户分析"),
    ORDER(3, "订单分析"),
    ALARM(4, "告警分析"),
    SHARE_BENEFIT(5, "分润分析");

    private Integer code;

    private String desc;

    private static Map<Integer, String> map;

    private static Map<String, Integer> descToIdMap;

    static {
        map = Arrays.stream(PanelModuleType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
        descToIdMap = Arrays.stream(PanelModuleType.values()).collect(Collectors.toMap(item -> item.desc, item -> item.code));
    }

    PanelModuleType(Integer code, String desc) {
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

    public static String getDesc(Integer code) {
        return map.get(code);
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Integer getCode(String desc) {
        return descToIdMap.get(desc);
    }
}
