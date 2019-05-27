package com.gizwits.lease.benefit.dto;

import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;

/**
 * Created by zhl on 2017/8/31.
 */
public class ShareBenefitOperatorObjectDto {
    private Integer id;
    private Integer sysAccountId;
    private String name;

    public ShareBenefitOperatorObjectDto(Operator operator) {
        this.id = operator.getId();
        this.sysAccountId = operator.getSysAccountId();
        this.name = operator.getName();
    }

    public ShareBenefitOperatorObjectDto(Agent agent) {
        this.id = agent.getId();
        this.sysAccountId = agent.getSysAccountId();
        this.name = agent.getName();
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
