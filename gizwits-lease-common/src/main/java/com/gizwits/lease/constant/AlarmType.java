package com.gizwits.lease.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;

public enum AlarmType {
        ALERT(2, "报警"),
        FLAUT(1, "故障");

        Integer code;
        String name;

        private static Map<Integer, String> codeToName;

        static {
            codeToName = Arrays.stream(AlarmType.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
        }

        AlarmType(int key, String msg) {
            this.code = key;
            this.name = msg;
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
            AlarmType[] deviceStatuses = AlarmType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (AlarmType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
        public static AlarmType getAlarmTypeEnum(int code) {
            for (AlarmType alarmType : AlarmType.values()) {
                if (alarmType.getCode() == code)
                    return alarmType;
            }
            return null;
        }

        public static String getName(Integer code) {
            return codeToName.get(code);
        }
    }
