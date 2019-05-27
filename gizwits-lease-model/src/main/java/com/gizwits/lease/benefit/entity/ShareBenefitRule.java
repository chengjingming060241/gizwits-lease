package com.gizwits.lease.benefit.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@TableName("share_benefit_rule")
public class ShareBenefitRule extends Model<ShareBenefitRule> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id")
	private String id;
    /**
     * 分润规则名称
     */
	@TableField("share_benefit_rule_name")
	private String shareBenefitRuleName;
    /**
     * 运营商关联系统用户
     */
	@TableField("sys_account_id")
	private Integer sysAccountId;
    /**
     * 运营商名称
     */
	@TableField("operator_name")
	private String operatorName;
    /**
     * 账单首次生成时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 对账单生成频率: DAY,WEEK,MONTH,YEAR
     */
	private String frequency;
	/**
	 * 上一次执行分润时间
	 */
	@TableField("last_execute_time")
	private Date lastExecuteTime;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 修改时间
     */
	private Date utime;
    /**
     * 分润规则的所有者
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
	@TableField("is_deleted")
	private Integer isDeleted;


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

	public Integer getSysAccountId() {
		return sysAccountId;
	}

	public void setSysAccountId(Integer sysAccountId) {
		this.sysAccountId = sysAccountId;
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

	public Date getLastExecuteTime() {return lastExecuteTime;}

	public void setLastExecuteTime(Date lastExecuteTime) {this.lastExecuteTime = lastExecuteTime;}

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
