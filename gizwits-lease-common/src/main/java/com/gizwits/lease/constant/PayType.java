package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;


public enum PayType{
        WX_JSAPI(1,"微信公众号"),
        WX_APP(2,"APP微信支付"),
        ALIPAY(3,"支付宝支付"),
        CARD(4,"充值卡支付"),
        BALANCE(5,"余额支付"),
        WEIXINPAY(6,"微信支付"),
        DISCOUNT(7,"优惠金额支付"),
        BALANCE_DISCOUNT(8,"钱包和优惠金额支付"),
        WX_H5(9,"微信H5支付"),

        ;
        PayType(Integer code,String name){
            this.code = code;
            this.name = name;
        }
        Integer code;
        String name;

        private static Map<Integer, String> codeToName;

        static {
            codeToName = Arrays.stream(PayType.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
        }
        public static String getName(Integer code) {
            return codeToName.get(code);
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

        public static PayType getPayType(int code) {
            for (PayType type : PayType.values()) {
                if (type.getCode() == code)
                    return type;
            }
            return null;
        }

        public static Map enumToMap() {
            PayType[] payTypes = PayType.class.getEnumConstants();
            Map payMap = new HashedMap();
            for (PayType payType: payTypes){
                payMap.put(payType.getCode(),payType.getName());
            }
            return payMap;
        }

    }