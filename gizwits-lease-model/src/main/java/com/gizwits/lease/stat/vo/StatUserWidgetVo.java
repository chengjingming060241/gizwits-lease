package com.gizwits.lease.stat.vo;

/**
 * Created by GaGi on 2017/7/19.
 */
public class StatUserWidgetVo {
    private Integer totalCount;
    private Double newPercent;
    private Integer activeCount;
    private Double activePercent;

    public StatUserWidgetVo() {
        this.totalCount=0;
        this.newPercent=0.0;
        this.activeCount=0;
        this.activePercent=0.0;
    }

    public StatUserWidgetVo(Integer totalCount, Double newPercent, Integer activeCount, Double activePercent) {
        this.totalCount = totalCount;
        this.newPercent = newPercent;
        this.activeCount = activeCount;
        this.activePercent = activePercent;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Double getNewPercent() {
        return newPercent;
    }

    public void setNewPercent(Double newPercent) {
        this.newPercent = newPercent;
    }

    public Integer getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(Integer activeCount) {
        this.activeCount = activeCount;
    }

    public Double getActivePercent() {
        return activePercent;
    }

    public void setActivePercent(Double activePercent) {
        this.activePercent = activePercent;
    }
}
