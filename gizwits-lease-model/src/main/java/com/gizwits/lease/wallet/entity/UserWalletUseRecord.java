package com.gizwits.lease.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户钱包操作记录表
 * </p>
 *
 * @author yinhui
 * @since 2017-07-28
 */
@TableName("user_wallet_use_record")
public class UserWalletUseRecord extends Model<UserWalletUseRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
	/**
	 * 交易号
	 */
	@TableField("trade_no")
	private String tradeNo;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 钱包类型,枚举中获取值
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
     *  余额
     */
	private Double balance;
    /**
     * 操作类型：1,充值，2,消费
     */
	@TableField("operation_type")
	private Integer operationType;
    /**
     * 所属用户
     */
	private String username;
	/**
	 * 用户id
	 */
	@TableField("user_id")
	private Integer userId;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradeNo() {return tradeNo;}

	public void setTradeNo(String tradeNo) {this.tradeNo = tradeNo;}

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

	public Integer getWalletType() {return walletType;}

	public void setWalletType(Integer walletType) {this.walletType = walletType;}

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

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getOperationType() {
		return operationType;
	}

	public void setOperationType(Integer operationType) {
		this.operationType = operationType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserId() { return userId; }

	public void setUserId(Integer userId) { this.userId = userId; }

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
