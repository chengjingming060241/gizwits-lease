package com.gizwits.lease.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户钱包充值单表
 * </p>
 *
 * @author yinhui
 * @since 2017-07-31
 */
@TableName("user_wallet_charge_order")
public class UserWalletChargeOrder extends Model<UserWalletChargeOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,充值单号
     */
    @TableId("charge_order_no")
	private String chargeOrderNo;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 钱包ID,枚举中获取值
     */
	@TableField("wallet_type")
	private Integer walletType;
    /**
     * 钱包名称,枚举中获取值
     */
	@TableField("wallet_name")
	private String walletName;
    /**
     * 操作金额
     */
	private Double fee;
	/**
	 * 优惠金额
	 */
	@TableField("discount_money")
	private Double discountMoney;
    /**
     *  余额
     */
	private Double balance;
    /**
     * 充值状态：0 待支付 1，充值成功，2，充值失败
     */
	private Integer status;
    /**
     * 所属用户
     */
	private String username;
	/**
	 * 用户id
	 */
	@TableField("user_id")
	private Integer userId;
	/**
	 * 支付时间
	 */
	@TableField("pay_time")
	private Date payTime;
	/**
	 * 支付方式
	 */
	@TableField("pay_type")
	private Integer payType;

	@TableField("launch_area_name")
	private String launchAreaName;

	@TableField("device_name")
	private String deviceName;

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getChargeOrderNo() {return chargeOrderNo;}

	public void setChargeOrderNo(String chargeOrderNo) {this.chargeOrderNo = chargeOrderNo;}

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

	public Integer getWalletType() {
		return walletType;
	}

	public void setWalletType(Integer walletType) {
		this.walletType = walletType;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public Double getFee() {
		return fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	public Double getDiscountMoney() {return discountMoney;}

	public void setDiscountMoney(Double discountMoney) {this.discountMoney = discountMoney;}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserId() { return userId; }

	public void setUserId(Integer userId) { this.userId = userId; }

	public Integer getPayType() {return payType;}

	public void setPayType(Integer payType) {this.payType = payType;}

	public String getLaunchAreaName() {
		return launchAreaName;
	}

	public void setLaunchAreaName(String launchAreaName) {
		this.launchAreaName = launchAreaName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	protected Serializable pkVal() {
		return this.chargeOrderNo;
	}

}
