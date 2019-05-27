package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum StatusCommandType {
        FREE("FREE","空闲状态"),
        USING("USING","使用中状态"),
        FINISH("FINISH","完成使用状态")
        ;
        String code;
        String name;

        StatusCommandType(String code, String name){
            this.code = code;
            this.name = name;
        }
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static Map enumToMap() {
            StatusCommandType[] deviceStatuses = StatusCommandType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (StatusCommandType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }