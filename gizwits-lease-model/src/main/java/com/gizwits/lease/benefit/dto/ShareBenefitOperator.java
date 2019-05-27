package com.gizwits.lease.benefit.dto;

import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;

/**
 * Created by zhl on 2017/9/1.
 */
public class ShareBenefitOperator {
    private Integer id;

    private Integer sysAccountId;

    private String name;

    private String shareBenefitRuleId;

    public ShareBenefitOperator(Operator operator) {
        this.id = operator.getId();
        this.sysAccountId = operator.getSysAccountId();
        this.name = operator.getName();
        this.shareBenefitRuleId = operator.getShareBenefitRuleId();
    }

    public ShareBenefitOperator(Agent agent) {
        this.id = agent.getId();
        this.sysAccountId = agent.getSysAccountId();
        this.name = agent.getName();
        this.shareBenefitRuleId = agent.getShareBenefitRuleId();
    }

    public String getShareBenefitRuleId() {
        return shareBenefitRuleId;
    }

    public void setShareBenefitRuleId(String shareBenefitRuleId) {
        this.shareBenefitRuleId = shareBenefitRuleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
