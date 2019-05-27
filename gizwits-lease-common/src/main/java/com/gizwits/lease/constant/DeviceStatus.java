package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DeviceStatus {
        LOCKED(-1,"锁定"),
        ONLINE(1,"在线"),
        OFFLINE(2,"离线"),
        USING(3,"使用中"),
        FREE(4,"空闲"),
        STOP(5,"禁用"),
        FAULT(6,"故障"),
        AWAIT(7,"待机"),
        NORMAL(8,"正常"),
        USABLE(9,"有故障但仍可使用"),
        ;
        Integer code;
        String name;

        private static Map<Integer, String> codeToName;

        static {
            codeToName = Arrays.stream(DeviceStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
        }

        DeviceStatus(Integer code,String name){
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
            DeviceStatus[] deviceStatuses = DeviceStatus.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (DeviceStatus deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }

        public static String getName(Integer code) {
            return codeToName.get(code);
        }

        public static String getShowName(Integer workStatus, Integer faultStatus, Boolean lock) {
            if(lock){
                return LOCKED.name;
            }
            if(faultStatus.equals(FAULT.getCode())){
                return FAULT.name;
            }
            return codeToName.get(workStatus);
        }
    }