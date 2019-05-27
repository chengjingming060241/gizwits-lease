package com.gizwits.lease.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;


public enum AlarmStatus {
        UNRESOLVE(0,"未修复"),
        RESOLVE(1,"已修复")
        ;
        Integer code;
        String name;

        private static Map<Integer, String> codeToName;

        static {
            codeToName = Arrays.stream(AlarmStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
        }

        AlarmStatus(Integer code, String name){
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
            AlarmStatus[] deviceStatuses = AlarmStatus.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (AlarmStatus deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }

        public static AlarmStatus getAlarmStatusEnum(int code) {
            for (AlarmStatus alarmStatus : AlarmStatus.values()) {
                if (alarmStatus.getCode() == code)
                    return alarmStatus;
            }
            return null;
        }

        public static String getName(Integer code) {
            return codeToName.get(code);
        }
    }