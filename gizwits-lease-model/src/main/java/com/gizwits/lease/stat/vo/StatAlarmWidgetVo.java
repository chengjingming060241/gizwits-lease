package com.gizwits.lease.stat.vo;

/**
 * Created by GaGi on 2017/7/19.
 */
public class StatAlarmWidgetVo {
    private Integer warnCount;
    private Integer warnRecord;
    private Double alarmPercent;

    public StatAlarmWidgetVo(Integer warnCount, Integer warnRecord, Double alarmPercent) {
        this.warnCount = warnCount;
        this.warnRecord = warnRecord;
        this.alarmPercent = alarmPercent;
    }

    public Integer getWarnCount() {

        return warnCount;
    }

    public void setWarnCount(Integer warnCount) {
        this.warnCount = warnCount;
    }

    public Integer getWarnRecord() {
        return warnRecord;
    }

    public void setWarnRecord(Integer warnRecord) {
        this.warnRecord = warnRecord;
    }

    public Double getAlarmPercent() {
        return alarmPercent;
    }

    public void setAlarmPercent(Double alarmPercent) {
        this.alarmPercent = alarmPercent;
    }
}
