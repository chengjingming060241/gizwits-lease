package com.gizwits.lease.benefit.entity.dto;

import com.gizwits.lease.benefit.dto.ShareBenefitDeviceRangeVo;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Pattern;

/**
 * 分润规则详情dto
 * Created by yinhui on 2017/8/1.
 */
public class ShareBenefitRuleDetailDto implements Serializable {
    private String ruleDetailId;
    /**
     * 详细分润名称
     */
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$", message = "参数格式错误")
    @Length(max = 40)
    private String ruleDetailName;

    @DecimalMin("0.00")
    @DecimalMax("100.00")
    private BigDecimal sharePercentage;
    @NotBlank
    private String shareType;

    private String shareTypeDescription;

    private List<ShareBenefitRuleDetailDeviceDto> ruleDetailDeviceDtoList;

    private ShareBenefitDeviceRangeVo devicePercentageRange;

    public ShareBenefitDeviceRangeVo getDevicePercentageRange() {
        return devicePercentageRange;
    }

    public void setDevicePercentageRange(ShareBenefitDeviceRangeVo devicePercentageRange) {
        this.devicePercentageRange = devicePercentageRange;
    }

    public String getRuleDetailId() {
        return ruleDetailId;
    }

    public void setRuleDetailId(String ruleDetailId) {
        this.ruleDetailId = ruleDetailId;
    }

    public String getRuleDetailName() {return ruleDetailName;}

    public void setRuleDetailName(String ruleDetailName) {this.ruleDetailName = ruleDetailName;}

    public BigDecimal getSharePercentage() {return sharePercentage;}

    public void setSharePercentage(BigDecimal sharePercentage) {this.sharePercentage = sharePercentage;}

    public String getShareType() {return shareType;}

    public void setShareType(String shareType) {this.shareType = shareType;}

    public String getShareTypeDescription() { return shareTypeDescription; }

    public void setShareTypeDescription(String shareTypeDescription) { this.shareTypeDescription = shareTypeDescription; }

    public List<ShareBenefitRuleDetailDeviceDto> getRuleDetailDeviceDtoList() {return ruleDetailDeviceDtoList;}

    public void setRuleDetailDeviceDtoList(List<ShareBenefitRuleDetailDeviceDto> ruleDetailDeviceDtoList) {this.ruleDetailDeviceDtoList = ruleDetailDeviceDtoList;}
}
