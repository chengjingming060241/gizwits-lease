package com.gizwits.lease.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lilh
 * @date 2017/8/30 16:16
 */
public enum DeviceNormalStatus {

    ENTRY(0, "入库"),
    SHIFT_OUT(1, "出库"),
    SERVING(2, "服务中"),
    PAUSE(3, "暂停服务"),
    RETURNED(4, "已返厂"),
    SCRAPED(5, "已报废"),
    WAIT_TO_ENTRY(6, "待入库");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToDesc;

    static {
        codeToDesc = Arrays.stream(DeviceNormalStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    DeviceNormalStatus(Integer code, String desc) {
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
