package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum - 数据类型
 *
 * @author lilh
 * @date 2017/7/5 11:43
 */
public enum DataType {

    BOOL("bool", "布尔值"),
    ENUM("enum", "枚举"),
    UINT16("uint16", "数值"),
    UINT32("uint32", "数值"),
    BINARY("binary", "扩展");

    /** 代码 */
    private String code;

    /** 描述 */
    private String desc;

    private static Map<String, String> map = new HashMap<>(5);

    static {
        Arrays.stream(DataType.values()).forEach(item -> map.put(item.code, item.desc));
    }

    DataType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getDesc(String code) {
        return map.get(code);
    }

    public static Map<String, String> map() {
        return map;
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
}
