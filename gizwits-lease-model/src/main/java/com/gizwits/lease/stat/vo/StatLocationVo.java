package com.gizwits.lease.stat.vo;

/**
 * Created by GaGi on 2017/7/17.
 */
public class StatLocationVo {
    private String province;
    private Number count=0;
    private Number proportion=0.0;

    public Number getProportion() {
        return proportion;
    }

    public void setProportion(Number proportion) {
        this.proportion = proportion;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Number getCount() {
        return count;
    }

    public void setCount(Number count) {
        this.count = count;
    }
}
