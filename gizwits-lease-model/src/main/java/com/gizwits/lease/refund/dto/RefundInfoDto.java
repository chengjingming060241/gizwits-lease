package com.gizwits.lease.refund.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * <p>
 * 退款申请表
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
public class RefundInfoDto {
	@ApiModelProperty("退款单号")
	private String refundNo;
	@ApiModelProperty("退款状态")
	private Integer status;
	@ApiModelProperty("退款状态描述")
	private String statusStr;
	@ApiModelProperty("订单号")
	private String orderNo;
	@ApiModelProperty("订单金额")
	private Double amount;
	@ApiModelProperty("用户昵称")
	private String userName;
	@ApiModelProperty("用户手机号")
	private String userMobile;
	@ApiModelProperty("退款原因")
	private String refundReason;
	@ApiModelProperty("审核原因")
	private String auditReason;
	@ApiModelProperty("审核人")
	private String auditor;
	@ApiModelProperty("审核时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
	private Date auditTime;
	@ApiModelProperty("退款人")
	private String refunder;
	@ApiModelProperty("退款时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
	private Date refundTime;
	@ApiModelProperty("订单支付时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
	private Date payTime;
	@ApiModelProperty("退款申请时间")
	@JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
	private Date ctime;
	@ApiModelProperty("设备sno")
	private String sno;

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

	public String getStatusStr() {
		return statusStr;
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
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

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getRefunder() {
		return refunder;
	}

	public void setRefunder(String refunder) {
		this.refunder = refunder;
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getSno() { return sno; }

	public void setSno(String sno) { this.sno = sno; }
}
