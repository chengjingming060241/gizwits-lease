package com.gizwits.lease.constant;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum WalletEnum{
        BALENCE(1,"余额"),
        DEPOSIT(2,"押金"),
        DISCOUNT(3,"赠送")
        ;
        Integer code;
        String name;

        WalletEnum(Integer code, String name) {
            this.code = code;
            this.name = name;
        }
        public static Map enumToMap(){
            WalletEnum[] walletEnums = WalletEnum.class.getEnumConstants();
            Map map =new HashedMap();
            for (WalletEnum walletEnum: walletEnums){
                map.put(walletEnum.getCode(),walletEnum.getName());
            }
            return map;
        }

        public static WalletEnum getWalletEnumm(int code) {
            for (WalletEnum walletEnum : WalletEnum.values()) {
                if (walletEnum.getCode() == code)
                    return walletEnum;
            }
            return null;
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