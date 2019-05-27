package com.gizwits.lease.stat.vo;

/**
 * Created by GaGi on 2017/7/19.
 */
public class StatDeviceWidgetVo {
    private Integer totalCount;
    private Integer newCount;
    private Double orderedPercent;
    private Integer alarmCount;

    public StatDeviceWidgetVo(Integer totalCount, Integer newCount, Double orderedPercent, Integer alarmCount) {
        this.totalCount = totalCount;
        this.newCount = newCount;
        this.orderedPercent = orderedPercent;
        this.alarmCount = alarmCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getNewCount() {
        return newCount;
    }

    public void setNewCount(Integer newCount) {
        this.newCount = newCount;
    }

    public Double getOrderedPercent() {
        return orderedPercent;
    }

    public void setOrderedPercent(Double orderedPercent) {
        this.orderedPercent = orderedPercent;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }
}
