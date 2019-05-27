package com.gizwits.lease.enums;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Enum - 面板模块中的项
 *
 * @author lilh
 * @date 2017/7/15 15:31
 */
public enum PanelModuleItemType {

    //======== 数据展示 ========
    // 设备分析
    CURRENT_DEVICE_TOTAL_NUMBER(1, "当前设备总数"),
    TODAY_DEVICE_NEW_ADD_NUMBER(2, "今日设备新增数"),
    TODAY_DEVICE_ORDER_RATE(3, "今日设备订单率"),
    CURRENT_DEVICE_FAULT_NUMBER(4, "当前设备故障数"),

    //用户分析
    CURRENT_USER_TOTAL_NUMBER(5, "当前用户总人数"),
    YESTERDAY_USER_INCREMENT_RATE(6, "昨日用户增长率"),
    TODAY_ACTIVE_USER_TOTAL_NUMBER(7, "今日活跃用户数"),
    YESTERDAY_ACTIVE_USER_RATE(8, "昨日用户活跃率"),

    //订单分析
    TODAY_ORDER_TOTAL_NUMBER(9, "今日订单数"),
    YESTERDAY_ORDER_TOTAL_NUMBER(10, "昨日订单数"),
    YESTERDAY_ORDER_INCREMENT_RATE(11, "昨日订单增长率"),
    CURRENT_MONTH_ORDER_TOTAL_NUMBER(12, "本月订单数"),

    //告警分析
    CURRENT_DEVICE_ALARM_TOTAL_NUMBER(13, "当前设备告警数"),
    TODAY_NEW_ADD_ALARM_RECORD(14, "今日新增告警记录"),
    CURRENT_DEVICE_ALARM_RATE(15, "当前设备故障率"),

    //分润分析
    TODAY_SHARE_BENEFIT_SHEET_NUMBER(31, "今日分润账单数"),
    TODAY_SHARE_BENEFIT_AMOUNT(32, "今日分润金额"),

    //======= 图表展示 ==========
    //设备分析
    DEVICE_REGION_DISTRIBUTION(16, "设备地区分布"),
    DEVICE_REGION_DISTRIBUTION_RANK(17, "设备地区分布排行"),
    NEW_ADD_DEVICE_ONLINE_TREND(18, "新增设备上线趋势"),
    DEVICE_ACTIVE_TREND(19, "设备活跃趋势"),
    DEVICE_ORDER_RATE_TREND(20, "设备订单率趋势"),
    DEVICE_USE_PERIOD_ANALYZE(21, "设备使用时段分析"),

    //用户分析
    USER_REGION_DISTRIBUTION(22, "用户地区分布"),
    USER_REGION_DISTRIBUTION_RANK(23, "用户地区分布排行"),
    USER_GENDER_RATE(24, "用户性别占比"),
    USER_USE_NUMBER_RATE(25, "使用次数占比"),
    NEW_ADD_USER_TREND(26, "新增用户趋势"),
    ACTIVE_USER_TREND(27, "活跃用户趋势"),
    USER_TOTAL_NUMBER_TREND(28, "用户总数趋势"),

    //订单分析
    ORDER_AMOUNT(29, "订金金额"),
    ORDER_TOTAL_NUMBER(30, "订单数量"),
    ORDER_TODAY_AMOUNT(33, "今日订单总额"),

    EXP_DEVICES(34, "15天内到期设备数"),
    OFF_DEVICES(35, "已上线离线设备数"),
    REMAIN_DEVICES(36, "水量剩余10%设备数");


    private Integer code;

    private String desc;

    private static Map<Integer, String> map;

    static {
        map = Arrays.stream(PanelModuleItemType.values()).collect(Collectors.toMap(item -> item.code, item -> item.desc));
    }

    PanelModuleItemType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getDesc(Integer code) {
        return map.get(code);
    }

    public static void main(String[] args) {
        map.forEach((key, value) -> System.out.println(key + "->" + value));
    }
}
