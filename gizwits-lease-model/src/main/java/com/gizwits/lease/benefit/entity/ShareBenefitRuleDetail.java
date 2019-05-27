package com.gizwits.lease.benefit.entity;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 分润规则详细表
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@TableName("share_benefit_rule_detail")
public class ShareBenefitRuleDetail extends Model<ShareBenefitRuleDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id")
	private String id;
    /**
     * 分润规则主表ID
     */
	@TableField("rule_id")
	private String ruleId;
    /**
     * 详细分润规则名称
     */
	private String name;
    /**
     * 分润比例，显示的是百分数
     */
	@TableField("share_percentage")
	private BigDecimal sharePercentage;
    /**
     * 分润类型，使用设备：ALL,所有设备；SINGLE，个别设备；
     */
	@TableField("share_type")
	private String shareType;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 修改时间
     */
	private Date utime;
	@TableField("is_deleted")
	private Integer isDeleted;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getSharePercentage() {
		return sharePercentage;
	}

	public void setSharePercentage(BigDecimal sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public String getShareType() {
		return shareType;
	}

	public void setShareType(String shareType) {
		this.shareType = shareType;
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

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
