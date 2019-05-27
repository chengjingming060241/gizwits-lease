package com.gizwits.lease.benefit.entity.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/9/5.
 */
public class ShareBenefitRuleNameCheckDto {
    @NotNull
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
