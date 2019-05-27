package com.gizwits.lease.benefit.entity.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 分润规则列表
 *
 * @author lilh
 * @date 2017/8/11 18:33
 */
public class ShareBenefitRuleListDto {


    private String id;
    /**
     * 分润规则名称
     */
    private String shareBenefitRuleName;

    private Integer operatorId;

    /**
     * 运营商名称
     */
    private String operatorName;
    /**
     * 账单首次生成时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;
    /**
     * 对账单生成频率: DAY,WEEK,MONTH,YEAR
     */
    private String frequency;

    /** 频率描述,供前台显示 */
    private String frequencyDesc;

    private Integer sysAccountId;

    /**
     * 上一次执行分润时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastExecuteTime;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date utime;

    /**
     * 分润规则的所有者
     */
    private Integer sysUserId;

    private Integer isDeleted;

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public ShareBenefitRuleListDto(ShareBenefitRule shareBenefitRule) {
        BeanUtils.copyProperties(shareBenefitRule, this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShareBenefitRuleName() {
        return shareBenefitRuleName;
    }

    public void setShareBenefitRuleName(String shareBenefitRuleName) {
        this.shareBenefitRuleName = shareBenefitRuleName;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getFrequencyDesc() {
        return frequencyDesc;
    }

    public void setFrequencyDesc(String frequencyDesc) {
        this.frequencyDesc = frequencyDesc;
    }

    public Date getLastExecuteTime() {
        return lastExecuteTime;
    }

    public void setLastExecuteTime(Date lastExecuteTime) {
        this.lastExecuteTime = lastExecuteTime;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
