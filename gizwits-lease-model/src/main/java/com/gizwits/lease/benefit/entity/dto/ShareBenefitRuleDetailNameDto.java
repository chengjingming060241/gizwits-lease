package com.gizwits.lease.benefit.entity.dto;

import java.io.Serializable;

/**
 * 详细分润规则名称判断dto
 * Created by yinhui on 2017/8/1.
 */
public class ShareBenefitRuleDetailNameDto implements Serializable {
    private String ruleId;
    private String detailName;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }
}
