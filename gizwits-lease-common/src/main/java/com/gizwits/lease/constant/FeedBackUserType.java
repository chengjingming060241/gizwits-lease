package com.gizwits.lease.constant;

import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * Created by GaGi on 2017/8/25.
 */
public enum  FeedBackUserType {
    USER(1,"移动用户端"),
    ADMIN(2,"移动管理端");

    private Integer code;
    private String name;

    FeedBackUserType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static Map enumToMap(){
        FeedBackUserType[] onlineStatuses = FeedBackUserType.class.getEnumConstants();
        Map map =new HashedMap();
        for (FeedBackUserType onlineStatus: onlineStatuses){
            map.put(onlineStatus.getCode(),onlineStatus.getName());
        }
        return map;
    }

    public static FeedBackUserType getSDeviceWorkStatusEnum(int code) {
        for (FeedBackUserType onlineStatus : FeedBackUserType.values()) {
            if (onlineStatus.getCode() == code)
                return onlineStatus;
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
}
