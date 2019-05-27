package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum BindType {
        BIND(1,"绑定"),
        UNBIND(0,"未绑定")
        ;
        Integer code;
        String name;

        BindType(Integer code, String name){
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
            BindType[] deviceStatuses = BindType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (BindType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }