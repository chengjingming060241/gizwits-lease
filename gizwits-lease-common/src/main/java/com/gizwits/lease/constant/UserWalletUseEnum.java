package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum UserWalletUseEnum {
    RECHARGE(1, "充值"),
    PAY(2, "支付"),
    REFUND(3,"退款");
    Integer code;
    String name;
    private static Map<Integer, String> map = new HashedMap();

    static {
        map = Arrays.stream(UserWalletUseEnum.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
    }

    UserWalletUseEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
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

    public static String getName(Integer code) {
        return map.get(code);
    }
}
