package com.gizwits.lease.benefit.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 分润账单表
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@TableName("share_benefit_sheet")
public class ShareBenefitSheet extends Model<ShareBenefitSheet> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    /**
     * 分润账单号
     */
	@TableField("sheet_no")
	private String sheetNo;
    /**
     * 运营商绑定的系统账号
     */
	@TableField("sys_account_id")
	private Integer sysAccountId;
    /**
     * 运营商名称
     */
	@TableField("operator_name")
	private String operatorName;
    /**
     * 账单状态：0，创建；1，待审核;2，待分润；3,执行分润中；4，分润成功；5，分润失败；
     */
	private Integer status;
    /**
     * 分润支付类型：1，微信；2，支付宝；3，线下；
     */
	@TableField("pay_type")
	private Integer payType;
    /**
     * 订单数
     */
	@TableField("order_count")
	private Integer orderCount;
    /**
     * 所有订单的总合计金额
     */
	@TableField("total_money")
	private Double totalMoney;
    /**
     * 当前运营商的分润金额
     */
	@TableField("share_money")
	private Double shareMoney;
	private Date ctime;
	private Date utime;
    /**
     * 分润执行商户订单号
     */
	@TableField("trade_no")
	private String tradeNo;
    /**
     * 分润成功时的微信订单号
     */
	@TableField("payment_no")
	private String paymentNo;
    /**
     * 微信付款成功时间
     */
	@TableField("payment_time")
	private Date paymentTime;

	/**
	 * 审核时间
	 */
	@TableField("audit_time")
	private Date auditTime;

	/**
	 * 支付账号
	 */
	@TableField("pay_account")
	private String payAccount;

	/**
	 * 收款人openid
     */
	@TableField("receiver_openid")
	private String receiverOpenid;

	/**
	 * 收款人真实姓名
     */
	@TableField("receiver_name")
	private String receiverName;

	/**
	 * 是否使用trade_no重试支付，如果此字段为6，则分润单不可修改，只再次支付
     */
	@TableField("is_try_again")
	private Integer isTryAgain;

	@TableField("rule_id")
	private String ruleId;



	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	public Integer getIsTryAgain() {
		return isTryAgain;
	}

	public void setIsTryAgain(Integer isTryAgain) {
		this.isTryAgain = isTryAgain;
	}

	public String getReceiverOpenid() {
		return receiverOpenid;
	}

	public void setReceiverOpenid(String receiverOpenid) {
		this.receiverOpenid = receiverOpenid;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getSheetNo() {
		return sheetNo;
	}

	public void setSheetNo(String sheetNo) {
		this.sheetNo = sheetNo;
	}

	public Integer getSysAccountId() {
		return sysAccountId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Double getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(Double shareMoney) {
		this.shareMoney = shareMoney;
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

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public String getPayAccount() {
		return payAccount;
	}

	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
