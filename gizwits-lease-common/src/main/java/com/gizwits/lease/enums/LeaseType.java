package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Description:
 * User: yinhui
 * Date: 2018-04-10
 */
public enum  LeaseType {

    WATER("01","微信购水模式"),
    NORMAL("02","长租模式"),;

    String code;
    String desc;
    private static Map<String,String> map;

    static {
        map = Arrays.stream(LeaseType.values()).collect(Collectors.toMap(item->item.code,item->item.desc));
    }

    public static String getDesc(String code){
        return map.get(code);
    }

    LeaseType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Map<String, String> getMap() {
        return map;
    }

    public static void setMap(Map<String, String> map) {
        LeaseType.map = map;
    }
}
