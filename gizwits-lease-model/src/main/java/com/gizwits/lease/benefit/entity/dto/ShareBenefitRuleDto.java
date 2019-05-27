package com.gizwits.lease.benefit.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

/**
 * 分润规则dto
 * Created by yinhui on 2017/8/1.
 */
public class ShareBenefitRuleDto implements Serializable {
    private String ruleId;
    /**
     * 分润规则名称
     */
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$", message = "参数格式错误")
    @Length(max = 40)
    private String ruleName;
    private Integer sysAccountId;
    @NotBlank
    private String operatorName;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @NotBlank

    private String frequency;
    private String frequencyDescription;
    @NotEmpty
    private List<ShareBenefitRuleDetailDto> ruleDetailDtoList;

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
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

    public String getFrequencyDescription() {
        return frequencyDescription;
    }

    public void setFrequencyDescription(String frequencyDescription) {
        this.frequencyDescription = frequencyDescription;
    }

    public List<ShareBenefitRuleDetailDto> getRuleDetailDtoList() {
        return ruleDetailDtoList;
    }

    public void setRuleDetailDtoList(List<ShareBenefitRuleDetailDto> ruleDetailDtoList) {
        this.ruleDetailDtoList = ruleDetailDtoList;
    }
}
