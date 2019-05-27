package com.gizwits.lease.benefit.dto;

import java.math.BigDecimal;

/**
 * Created by zhl on 2017/8/31.
 */
public class ShareBenefitDeviceRangeVo {
    private BigDecimal max;
    private BigDecimal min;

    public ShareBenefitDeviceRangeVo() {
    }

    public ShareBenefitDeviceRangeVo(BigDecimal min, BigDecimal max) {
        this.min = min;
        this.max = max;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }
}
