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
 * 用户钱包表
 * </p>
 *
 * @author yinhui
 * @since 2017-07-28
 */
@TableName("user_wallet")
public class UserWallet extends Model<UserWallet> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
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
     * 钱数
     */
	private Double money = 0.00;
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
	 * 运营商id，只针对押金钱包
	 */
	@TableField("sys_user_id")
	private Integer sysUserId;

	public Integer getId() {return id;}

	public void setId(Integer id) {
		this.id = id;
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

	public Integer getWalletType() {return walletType;}

	public void setWalletType(Integer walletType) {this.walletType = walletType;}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getUserId() { return userId; }

	public void setUserId(Integer userId) { this.userId = userId; }

	public Integer getSysUserId() { return sysUserId; }

	public void setSysUserId(Integer sysUserId) { this.sysUserId = sysUserId; }

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
