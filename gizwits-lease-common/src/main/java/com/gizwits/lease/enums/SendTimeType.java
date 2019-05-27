package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *  Enum  发送消息时间类型
 * Created by yinhui on 2017/8/8.
 */
public enum SendTimeType {
    NOW(1,"立即发送"),
    SEND_ON_TIME(2,"定时发送"),
    ;

    private Integer code;
    private String mess;
    private static Map<Integer,String> map = new HashMap<>(4);

    static {
        Arrays.stream(SendTimeType.values()).forEach(item -> map.put(item.code, item.mess));
    }

    SendTimeType(Integer code, String mess) {
        this.code = code;
        this.mess = mess;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public Map<Integer, String> getMap() {
        return map;
    }

    public void setMap(Map<Integer, String> map) {
        SendTimeType.map = map;
    }
}
