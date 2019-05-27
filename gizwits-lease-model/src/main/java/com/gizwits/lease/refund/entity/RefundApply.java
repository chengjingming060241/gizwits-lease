package com.gizwits.lease.refund.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 退款申请表
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
@TableName("refund_apply")
public class RefundApply extends Model<RefundApply> {

    private static final long serialVersionUID = 1L;

    /**
     * 退款单id
     */
    @TableId("refund_no")
	private String refundNo;
    /**
     * 状态：1待审核，2审核通过，3审核不通过，4已退款
     */
	private Integer status;
    /**
     * 对应的订单id
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 退款金额
     */
	private Double amount;
    /**
     * 退款路径，枚举值和支付类型一样
     */
	private Integer path;
    /**
     * 用户id
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 用户手机号
     */
	@TableField("user_mobile")
	private String userMobile;
    /**
     * 用户支付宝帐号
     */
	@TableField("user_alipay_account")
	private String userAlipayAccount;
    /**
     * 用户支付宝真实姓名
     */
	@TableField("user_alipay_real_name")
	private String userAlipayRealName;
    /**
     * 退款原因
     */
	@TableField("refund_reason")
	private String refundReason;
    /**
     * 审核原因
     */
	@TableField("audit_reason")
	private String auditReason;
    /**
     * 审核人id
     */
	@TableField("auditor_id")
	private Integer auditorId;
    /**
     * 审核时间
     */
	@TableField("audit_time")
	private Date auditTime;
    /**
     * 退款人id
     */
	@TableField("refunder_id")
	private Integer refunderId;
    /**
     * 退款时间
     */
	@TableField("refund_time")
	private Date refundTime;
    /**
     * 和订单的sys_user_id一致
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
	/**
	 * 用户昵称
	 */
	private String nickname;


	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPath() {
		return path;
	}

	public void setPath(Integer path) {
		this.path = path;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserAlipayAccount() {
		return userAlipayAccount;
	}

	public void setUserAlipayAccount(String userAlipayAccount) {
		this.userAlipayAccount = userAlipayAccount;
	}

	public String getUserAlipayRealName() {
		return userAlipayRealName;
	}

	public void setUserAlipayRealName(String userAlipayRealName) {
		this.userAlipayRealName = userAlipayRealName;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getAuditReason() {
		return auditReason;
	}

	public void setAuditReason(String auditReason) {
		this.auditReason = auditReason;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getRefunderId() {
		return refunderId;
	}

	public void setRefunderId(Integer refunderId) {
		this.refunderId = refunderId;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
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

	public String getNickname() { return nickname; }

	public void setNickname(String nickname) { this.nickname = nickname; }

	@Override
	protected Serializable pkVal() {
		return this.refundNo;
	}

}
