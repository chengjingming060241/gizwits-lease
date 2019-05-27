package com.gizwits.lease.stat.vo;

import java.math.BigDecimal;

/**
 * Created by GaGi on 2017/8/15.
 */
public class StatShareOrderWidgetVo {
    private Integer shareOrderCount;
    private BigDecimal shareOrderMoney;

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
}
