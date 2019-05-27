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
 * 所有要参与分润的订单
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@TableName("share_benefit_sheet_order")
public class ShareBenefitSheetOrder extends Model<ShareBenefitSheetOrder> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    /**
     * 分润单ID
     */
	@TableField("sheet_no")
	private String sheetNo;
    /**
     * 运营商账号ID
     */
	@TableField("sys_account_id")
	private Integer sysAccountId;
    /**
     * 设备sno
     */
	@TableField("device_sno")
	private String deviceSno;
    /**
     * 订单号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 详细分润规则ID
     */
	@TableField("share_rule_detail_id")
	private String shareRuleDetailId;
    /**
     * 直属运营商分润比例
     */
	@TableField("share_percentage")
	private Double sharePercentage;
    /**
     * 上一级在该订单的分润比例
     */
	@TableField("children_share_percentage")
	private Double childrenSharePercentage;
    /**
     * 订单在该运营商的分润金额，四舍五入保留4位小数
     */
	@TableField("share_money")
	private Double shareMoney;

	/**
	 * 订单金额
	 */
	@TableField("order_amount")
	private Double orderAmount;

    /**
     * 状态：1、待审核；2、审核通过；3、审核不通过；4、执行分润中；5、分润成功；
     */
	private Integer status;
	private Date ctime;
	private Date utime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public void setSysAccountId(Integer sysAccountId) {
		this.sysAccountId = sysAccountId;
	}

	public String getDeviceSno() {
		return deviceSno;
	}

	public void setDeviceSno(String deviceSno) {
		this.deviceSno = deviceSno;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getShareRuleDetailId() {
		return shareRuleDetailId;
	}

	public void setShareRuleDetailId(String shareRuleDetailId) {
		this.shareRuleDetailId = shareRuleDetailId;
	}

	public Double getSharePercentage() {
		return sharePercentage;
	}

	public void setSharePercentage(Double sharePercentage) {
		this.sharePercentage = sharePercentage;
	}

	public Double getChildrenSharePercentage() {
		return childrenSharePercentage;
	}

	public void setChildrenSharePercentage(Double childrenSharePercentage) {
		this.childrenSharePercentage = childrenSharePercentage;
	}

	public Double getShareMoney() {
		return shareMoney;
	}

	public void setShareMoney(Double shareMoney) {
		this.shareMoney = shareMoney;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
