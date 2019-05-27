package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


public  enum ProblemOrigin{

        USER_SIDE(1,"移动用户端"),
        MANAGEMEN(2,"移动管理端")
        ;
        Integer code;
        String name;

        ProblemOrigin(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {return code;}

        public void setCode(Integer code) {this.code = code;}

        public String getName() {return name;}

        public void setName(String name) {this.name = name;}

        public static Map enumToMap(){
            ProblemOrigin[] origins = ProblemOrigin.class.getEnumConstants();
            Map map  = new HashedMap();
            for (ProblemOrigin origin: origins ){
                map.put(origin.getCode(),origin.getName());
            }
            return map;
        }
    }