package com.gizwits.lease.constant;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;


public enum TradeOrderType {
        CONSUME(1,"用户消费订单"),
        SHARE(2,"分润订单"),
        CHARGE(3,"充值订单"),
        CARD(4,"充值卡订单"),
        REFUND(5, "退款单（非原路退回）"),
        ;
        Integer code;
        String name;

        TradeOrderType(Integer code, String name){
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
            TradeOrderType[] deviceStatuses = TradeOrderType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (TradeOrderType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }