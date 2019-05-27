package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.HashedMap;


public enum DeviceWorkStatus{
        ONLINE(1,"在线"),
        OFFLINE(2,"离线"),
        USING(3,"使用中"),
        FREE(4,"空闲"),
        STOP(5,"禁用"),
        FAULT(6,"故障"),
        AWAIT(7,"待机"),
        NORMAL(8,"正常");
        private Integer code;
        private String name;

    private static Map<Integer, String> codeToName;

    static {
        codeToName = Arrays.stream(DeviceWorkStatus.values()).collect(Collectors.toMap(item -> item.code, item -> item.name));
    }

    DeviceWorkStatus(Integer code, String name) {
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

    public static String getName(Integer code) {
        return codeToName.get(code);
    }
}