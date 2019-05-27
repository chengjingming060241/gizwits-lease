package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 分润单状态
 *
 * @author lilh
 * @date 2017/8/3 16:26
 */
public enum ShareBenefitSheetStatusType {

    CREATED(0, "创建"),
    TO_AUDIT(1, "待审核"),
    TO_SHARE(2, "待分润"),
    SHARING(3, "执行分润中"),
    SHARE_SUCCESS(4, "分润成功"),
    SHARE_FAILED(5, "分润失败"),
    SHARE_FAILED_RETRY(6, "分润失败需要重试");

    private Integer code;

    private String desc;

    private static Map<Integer, String> codeToName;

    static {
        codeToName = Arrays.stream(ShareBenefitSheetStatusType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    ShareBenefitSheetStatusType(Integer code, String desc) {
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
        return codeToName.get(code);
    }
}
