package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum LocationType {
        GIZWITS("GIZWITS","机智云接口"),
        GD("GD","高德地图接口")
        ;
        String code;
        String name;

        LocationType(String code, String name){
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
            LocationType[] deviceStatuses = LocationType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (LocationType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }