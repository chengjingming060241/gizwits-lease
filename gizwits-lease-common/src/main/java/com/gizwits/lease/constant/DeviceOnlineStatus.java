package com.gizwits.lease.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;


public enum DeviceOnlineStatus{
        ONLINE(1,"在线"),
        OFFLINE(2,"离线");
        private Integer code;
        private String name;

        private static Map<Integer, String> codeToName;

        static {
            codeToName = Arrays.stream(DeviceOnlineStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
        }

        DeviceOnlineStatus(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static Map enumToMap(){
            DeviceOnlineStatus[] onlineStatuses = DeviceOnlineStatus.class.getEnumConstants();
            Map map =new HashedMap();
            for (DeviceOnlineStatus onlineStatus: onlineStatuses){
                map.put(onlineStatus.getCode(),onlineStatus.getName());
            }
            return map;
        }

        public static DeviceOnlineStatus getSDeviceOnlineStatusEnum(int code) {
            for (DeviceOnlineStatus deviceOnlineStatus : DeviceOnlineStatus.values()) {
                if (deviceOnlineStatus.getCode() == code)
                    return deviceOnlineStatus;
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

        public static String getName(Integer code) {
            return codeToName.get(code);
        }

    }