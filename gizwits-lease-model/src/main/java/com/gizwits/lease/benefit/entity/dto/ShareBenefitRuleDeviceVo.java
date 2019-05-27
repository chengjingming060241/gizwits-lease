package com.gizwits.lease.benefit.entity.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhl on 2017/8/8.
 */
public class ShareBenefitRuleDeviceVo {

    private String ruleDetailDeviceId;
    private String sno;
    private String ruleDetailId;
    private BigDecimal sharePercentage;
    private String ruleId;
    private Date startTime;
    private Date lastExecuteTime;
    private Integer sysAccountId;
    private BigDecimal childrenPercentage;
    private String operatorName;

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getSno() {
        return sno;
    }

    public String getRuleDetailDeviceId() {
        return ruleDetailDeviceId;
    }

    public void setRuleDetailDeviceId(String ruleDetailDeviceId) {
        this.ruleDetailDeviceId = ruleDetailDeviceId;
    }

    public String getRuleDetailId() {
        return ruleDetailId;
    }

    public void setRuleDetailId(String ruleDetailId) {
        this.ruleDetailId = ruleDetailId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public BigDecimal getChildrenPercentage() {
        return childrenPercentage;
    }

    public void setChildrenPercentage(BigDecimal childrenPercentage) {
        this.childrenPercentage = childrenPercentage;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public BigDecimal getSharePercentage() {
        return sharePercentage;
    }

    public void setSharePercentage(BigDecimal sharePercentage) {
        this.sharePercentage = sharePercentage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }
}
