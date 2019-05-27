package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 面板分类
 *
 * @author lilh
 * @date 2017/7/15 15:12
 */
public enum PanelType {
    DATA(1, "数据展示"),
    CHART(2, "图表展示");

    private Integer code;

    private String desc;

    private static Map<Integer, String> map;

    private static Map<String, Integer> descToId;

    static {
        map = Arrays.stream(PanelType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
        descToId = Arrays.stream(PanelType.values()).collect(Collectors.toMap(item -> item.desc, item -> item.code));
    }

    PanelType(Integer code, String desc) {
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

    public static Integer getCode(String desc) {
        return descToId.get(desc);
    }
}
