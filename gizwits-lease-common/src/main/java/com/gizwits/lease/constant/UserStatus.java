package com.gizwits.lease.constant;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum UserStatus {
        NORMAL(1,"正常"),
        BLACK(2,"黑名单")
        ;
        Integer code;
        String name;

        UserStatus(Integer code, String name){
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
            UserStatus[] deviceStatuses = UserStatus.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (UserStatus deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }