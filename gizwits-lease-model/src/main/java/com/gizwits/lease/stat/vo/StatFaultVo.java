package com.gizwits.lease.stat.vo;

/**
 * Created by GaGi on 2017/8/15.
 */
public class StatFaultVo {
    private String remark;
    private Integer count;

    public StatFaultVo(String remark, Integer count) {
        this.remark = remark;
        this.count = count;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
