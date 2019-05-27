package com.gizwits.lease.constant;


import org.apache.commons.collections.map.HashedMap;
import java.util.Map;


public enum CommandType {
        SERVICE("SERVICE","收费类型指令"),
        CONTROL("CONTROL","控制指令"),
        STATUS("STATUS","状态指令"),
        SHOW("SHOW","展示指令"),
        ;
        String code;
        String name;

        CommandType(String code, String name){
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
            CommandType[] deviceStatuses = CommandType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (CommandType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }