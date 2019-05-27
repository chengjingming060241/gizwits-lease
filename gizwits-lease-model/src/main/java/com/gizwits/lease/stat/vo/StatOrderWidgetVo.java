package com.gizwits.lease.stat.vo;

import java.math.BigDecimal;

/**
 * Created by GaGi on 2017/7/19.
 */
public class StatOrderWidgetVo {
    private Integer orderCountToday;
    private StatWidgetDataVo orderCountYesterday;
    private Double orderNewPercentYesterday;
    private Integer orderCountMonth;
    private Integer shareOrderCount;
    private BigDecimal shareOrderMoney;



    public StatOrderWidgetVo(Integer orderCountToday, StatWidgetDataVo orderCountYesterday, Double orderNewPercentYesterday, Integer orderCountMonth) {
        this.orderCountToday = orderCountToday;
        this.orderCountYesterday = orderCountYesterday;
        this.orderNewPercentYesterday = orderNewPercentYesterday;
        this.orderCountMonth = orderCountMonth;
    }

    public Integer getShareOrderCount() {
        return shareOrderCount;
    }

    public void setShareOrderCount(Integer shareOrderCount) {
        this.shareOrderCount = shareOrderCount;
    }

    public BigDecimal getShareOrderMoney() {
        return shareOrderMoney;
    }

    public void setShareOrderMoney(BigDecimal shareOrderMoney) {
        this.shareOrderMoney = shareOrderMoney;
    }

    public StatWidgetDataVo getOrderCountYesterday() {
        return orderCountYesterday;
    }

    public void setOrderCountYesterday(StatWidgetDataVo orderCountYesterday) {
        this.orderCountYesterday = orderCountYesterday;
    }

    public Integer getOrderCountToday() {
        return orderCountToday;
    }

    public void setOrderCountToday(Integer orderCountToday) {
        this.orderCountToday = orderCountToday;
    }

    public Double getOrderNewPercentYesterday() {
        return orderNewPercentYesterday;
    }

    public void setOrderNewPercentYesterday(Double orderNewPercentYesterday) {
        this.orderNewPercentYesterday = orderNewPercentYesterday;
    }

    public Integer getOrderCountMonth() {
        return orderCountMonth;
    }

    public void setOrderCountMonth(Integer orderCountMonth) {
        this.orderCountMonth = orderCountMonth;
    }
}
