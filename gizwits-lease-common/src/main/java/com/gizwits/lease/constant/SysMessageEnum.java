package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public enum SysMessageEnum{

        RECIPIENT_ID(1,"recipient_id"),
        ADDRESSER_ID(2,"addresser_id");

        Integer code;
        String name;

        SysMessageEnum(Integer code, String name) {
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
        public static Map enumToMap(){
            SysMessageEnum[] messages = SysMessageEnum.class.getEnumConstants();
            Map map =new HashedMap();
            for (SysMessageEnum messageEnum: messages){
                map.put(messageEnum.getCode(),messageEnum.getName());
            }
            return map;
        }

        public static SysMessageEnum getSysMessageEnum(int code) {
            for (SysMessageEnum messageEnum : SysMessageEnum.values()) {
                if (messageEnum.getCode() == code)
                    return messageEnum;
            }
            return null;
        }
    }