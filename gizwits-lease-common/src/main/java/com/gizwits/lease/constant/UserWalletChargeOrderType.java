package com.gizwits.lease.constant;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum UserWalletChargeOrderType {
        EXPIRE(-1, "过期"),
        INIT(1,"创建"),
        PAYING(2,"支付中"),
        PAYED(3,"支付完成"),
        PAY_FAIL(4,"支付失败"),
        FINISH(5,"订单完成"),
        ;
        Integer code;
        String name;

        UserWalletChargeOrderType(Integer code, String name){
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

        public static Map enumToMap() {
            UserWalletChargeOrderType[] deviceStatuses = UserWalletChargeOrderType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (UserWalletChargeOrderType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }