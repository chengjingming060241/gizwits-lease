package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhl on 2017/8/4.
 */
public enum ShareBenefitRuleFrequency {
    DAY("DAY", "每天"),
    WEEK("WEEK", "每周"),
    MONTH("MONTH", "每月"),
    YEAR("YEAR", "每年");

    private String code;

    private String desc;

    private static Map<String, String> codeToName;

    static {
        codeToName = Arrays.stream(ShareBenefitRuleFrequency.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    ShareBenefitRuleFrequency(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

    public static String getDesc(String code) {
        return codeToName.get(code);
    }
}
