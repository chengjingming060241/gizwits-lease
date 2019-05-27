package com.gizwits.lease.constant;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;


public enum TradeStatus {
        EXPIRED(-1,"交易取消"),
        INIT(1,"创建"),
        SUCCESS(2,"交易成功"),
        FAIL(3,"交易失败"),
        ;

        Integer code;
        String msg;

        TradeStatus(int key , String msg) {
            this.code = key;
            this.msg = msg;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public static TradeStatus getOrderStatus(int key) {
            for (TradeStatus status : TradeStatus.values()) {
                if (status.getCode() == key)
                    return status;
            }
            return null;
        }

        public static Map enumToMap() {
            OrderStatus[] statuses = OrderStatus.class.getEnumConstants();
            Map map = new HashedMap();
            for (OrderStatus status: statuses){
                map.put(status.getCode(),status.getMsg());
            }
            return map;
        }
    }