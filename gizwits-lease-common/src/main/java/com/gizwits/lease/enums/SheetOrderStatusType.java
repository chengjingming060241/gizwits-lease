package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 参与分润的订单的状态
 *
 * @author lilh
 * @date 2017/8/4 17:57
 */
public enum SheetOrderStatusType {

    //状态：1、待审核；2、审核通过；3、审核不通过；4、执行分润中；5、分润成功；
    TO_AUDIT(1, "待审核"),
    AUDIT_PASSED(2, "审核通过"),
    AUDIT_NOT_PASSED(3, "审核不通过"),
    SHARING(4, "执行分润中"),
    SHARE_SUCCESS(5, "分润成功");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToDesc;

    static {
        codeToDesc = Arrays.stream(SheetOrderStatusType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    SheetOrderStatusType(Integer code, String desc) {
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
