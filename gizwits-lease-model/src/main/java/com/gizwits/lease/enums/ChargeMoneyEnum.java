package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Enum 充值金额配置
 * Created by yinhui on 2017/8/9.
 */
public enum ChargeMoneyEnum {
    TEN(10.0, 0.21),
    FORTY(40.0, 0.21),
    EIGHTY(80.0, 0.21),
    HUNDRED_AND_TWENTY(120.0, 0.21);

    Double money;
    Double realMoney;
    private static Map<Double, Double> map = new HashMap<>(4);

    static {
        Arrays.stream(ChargeMoneyEnum.values()).forEach(item -> map.put(item.money, item.realMoney));
    }

    public static ChargeMoneyEnum getChargeMoneyEnum(Double money) {
        for (ChargeMoneyEnum status : ChargeMoneyEnum.values()) {
            if (Objects.equals(status.getMoney(),money))
                return status;
        }
        return null;
    }

    ChargeMoneyEnum(Double money, Double realMoney) {
        this.money = money;
        this.realMoney = realMoney;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getRealMoney() {
        return realMoney;
    }

    public void setRealMoney(Double realMoney) {
        this.realMoney = realMoney;
    }

    public static Map<Double, Double> getMap() {
        return map;
    }

    public static void setMap(Map<Double, Double> map) {
        ChargeMoneyEnum.map = map;
    }
}
