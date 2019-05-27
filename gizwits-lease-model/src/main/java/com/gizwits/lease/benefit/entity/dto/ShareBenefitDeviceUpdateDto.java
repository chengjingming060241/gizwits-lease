package com.gizwits.lease.benefit.entity.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhl on 2017/8/25.
 */
public class ShareBenefitDeviceUpdateDto {

    private String ruleId;
    private List<String> deviceSnoList;
    private BigDecimal childrenPercentage;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public List<String> getDeviceSnoList() {
        return deviceSnoList;
    }

    public void setDeviceSnoList(List<String> deviceSnoList) {
        this.deviceSnoList = deviceSnoList;
    }

    public BigDecimal getChildrenPercentage() {
        return childrenPercentage;
    }

    public void setChildrenPercentage(BigDecimal childrenPercentage) {
        this.childrenPercentage = childrenPercentage;
    }
}
