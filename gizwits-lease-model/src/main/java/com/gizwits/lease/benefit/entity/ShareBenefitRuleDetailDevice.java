package com.gizwits.lease.benefit.entity;

import com.baomidou.mybatisplus.enums.IdType;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 分润规则详细设备表
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@TableName("share_benefit_rule_detail_device")
public class ShareBenefitRuleDetailDevice extends Model<ShareBenefitRuleDetailDevice> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id")
	private String id;
    /**
     * 分润规则详细表ID
     */
	@TableField("rule_detail_id")
	private String ruleDetailId;
    /**
     * 设备SNO
     */
	private String sno;
    /**
     * 创建时间
     */
	private Date ctime;
	private Date utime;
	@TableField("is_deleted")
	private Integer isDeleted;

	@TableField("share_percentage")
	private BigDecimal sharePercentage;

	@TableField("children_percentage")
	private BigDecimal childrenPercentage;

	public BigDecimal getSharePercentage() {
		return sharePercentage;
	}

	public void setSharePercentage(BigDecimal sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public BigDecimal getChildrenPercentage() {
		return childrenPercentage;
	}

	public void setChildrenPercentage(BigDecimal childrenPercentage) {
		this.childrenPercentage = childrenPercentage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRuleDetailId() {
		return ruleDetailId;
	}

	public void setRuleDetailId(String ruleDetailId) {
		this.ruleDetailId = ruleDetailId;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
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
