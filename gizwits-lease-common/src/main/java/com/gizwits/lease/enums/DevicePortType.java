package com.gizwits.lease.enums;

import com.gizwits.lease.constant.DeviceStatus;

import org.apache.commons.collections.map.HashedMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by yinhui on 2017/8/24.
 */
public enum  DevicePortType {
    NOTHING(0,"无"),
    WATER(1,"常温"),
    HOT(2,"热水"),
    ICE(3,"冰水"),
    WARM(4, "温开水"),
   ;

   private Integer code;
   private String mess;

    private static Map<Integer, String> codeToName;

    static {
        codeToName = Arrays.stream(DevicePortType.values()).collect(Collectors.toMap(item -> item.code, item -> item.mess));
    }

    DevicePortType(Integer code, String mess) {
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

    public static Map enumToMap() {
        DevicePortType[] portTypes = DevicePortType.class.getEnumConstants();
        Map deviceMap = new HashedMap();
        for (DevicePortType portType: portTypes){
            deviceMap.put(portType.getCode(),portType.getMess());
        }
        return deviceMap;
    }

    public static DevicePortType getDevicePortTypeEnum(int code) {
        for (DevicePortType portType : DevicePortType.values()) {
            if (portType.getCode() == code)
                return portType;
        }
        return null;
    }


    public static String getName(Integer code) {
        return codeToName.get(code);
    }
}
