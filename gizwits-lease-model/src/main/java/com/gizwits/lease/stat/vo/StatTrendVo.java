package com.gizwits.lease.stat.vo;

import java.math.BigDecimal;

/**
 * Created by GaGi on 2017/7/15.
 */
public class StatTrendVo {
    private Number count;
    private String time;
    private BigDecimal precent;
    public Number getCount() {
        return count;
    }

    public void setCount(Number count) {
        this.count = count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getPrecent() {
        return precent;
    }

    public void setPrecent(BigDecimal precent) {
        this.precent = precent;
    }
}
