package com.gizwits.lease.manager.dto;

import java.math.BigDecimal;

/**
 * Created by zhl on 2017/8/11.
 */
public class OperatorExtDto {
    private Integer id;
    private Integer operatorId;
    private BigDecimal cashPledge;
    private String rechargePromotion;
    private String logoUrl;
    private String phone;
    private String description;

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public BigDecimal getCashPledge() {
        return cashPledge;
    }

    public void setCashPledge(BigDecimal cashPledge) {
        this.cashPledge = cashPledge;
    }

    public String getRechargePromotion() {
        return rechargePromotion;
    }

    public void setRechargePromotion(String rechargePromotion) {
        this.rechargePromotion = rechargePromotion;
    }
}
