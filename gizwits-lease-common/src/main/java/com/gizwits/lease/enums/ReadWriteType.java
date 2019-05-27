package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum - 产品读写类型
 *
 * @author lilh
 * @date 2017/7/5 11:35
 */
public enum ReadWriteType {

    READ_ONLY("status_readonly", "只读"),
    WRITABLE("status_writable", "可写"),
    ALERT("alert", "报警"),
    FAULT("fault", "故障");


    /** 代码 */
    private String code;

    /** 描述 */
    private String desc;

    private static Map<String, String> map = new HashMap<>(4);

    static {
        Arrays.stream(ReadWriteType.values()).forEach(item -> map.put(item.code, item.desc));
    }

    ReadWriteType(String code, String desc) {
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
