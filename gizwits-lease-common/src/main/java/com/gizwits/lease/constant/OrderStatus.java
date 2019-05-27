package com.gizwits.lease.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;


public enum OrderStatus {
        ABNORMAL(-2,"异常"),
        EXPIRE(-1, "过期"),
        INIT(0, "创建"),
        PAYING(1,"支付中"),
        PAYED(2,"支付完成"),
        FAIL(3,"支付失败"),
        USING(4, "使用中"),
        FINISH(5, "订单完成"),
        REFUNDING(6,"退款中"),
        REFUNDED(7,"已退款"),
        REFUND_FAIL(8,"退款失败");

        Integer code;
        String msg;

        OrderStatus(int key , String msg) {
            this.code = key;
            this.msg = msg;
        }
        private static Map<Integer, String> codeToMsg;

        static {
            codeToMsg = Arrays.stream(OrderStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.msg));
        }

        public static String getStatus(Integer code){
            return codeToMsg.get(code);
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

        public static OrderStatus getOrderStatus(int key) {
            for (OrderStatus status : OrderStatus.values()) {
                if (status.getCode() == key)
                    return status;
            }
            return null;
        }

        public static Map enumToMap() {
            OrderStatus[] orderStatuses = OrderStatus.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (OrderStatus orderStatus: orderStatuses){
                deviceMap.put(orderStatus.getCode(),orderStatus.getMsg());
            }
            return deviceMap;
        }
        public static String getMsg(Integer code) {
            return codeToMsg.get(code);
        }
    }
