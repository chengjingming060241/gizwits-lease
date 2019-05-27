package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum QrcodeType {
        WEIXIN("WEIXIN","微信设备"),
        WEB("WEB","网页链接")
        ;
        String code;
        String name;

        QrcodeType(String code, String name){
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
            QrcodeType[] deviceStatuses = QrcodeType.class.getEnumConstants();
            Map deviceMap = new HashedMap();
            for (QrcodeType deviceStatus: deviceStatuses){
                deviceMap.put(deviceStatus.getCode(),deviceStatus.getName());
            }
            return deviceMap;
        }
    }