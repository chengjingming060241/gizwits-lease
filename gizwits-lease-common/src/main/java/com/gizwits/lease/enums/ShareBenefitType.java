package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum 角色分润权限
 * Created by yinhui on 2017/8/8.
 */
public enum ShareBenefitType {

    NULL(1, "无"),
    ENTER_ACCOUNT(2, "入账"),
    GET_MONEY(3, "收款"),;
    private Integer code;
    private String name;

    private static Map<Integer, String> map = new HashMap<>(3);

    static {
        Arrays.stream(ShareBenefitType.values()).forEach(item -> map.put(item.code, item.name));
    }

    ShareBenefitType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Map<Integer, String> getMap() {
        return map;
    }

    public static void setMap(Map<Integer, String> map) {
        ShareBenefitType.map = map;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
