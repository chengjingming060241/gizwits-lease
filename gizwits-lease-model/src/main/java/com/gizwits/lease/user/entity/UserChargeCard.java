package com.gizwits.lease.user.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 充值卡
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@TableName("user_charge_card")
public class UserChargeCard extends Model<UserChargeCard> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 充值卡号
     */
	@TableField("card_num")
	private String cardNum;
    /**
     * 用户id
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 用户名称
     */
	@TableField("user_name")
	private String userName;
    /**
     * 手机号
     */
	private String mobile;
    /**
     * 卡内余额
     */
	private Double money;
    /**
     * 充值卡状态，1,使用中 2,暂停
     */
	private Integer status;
    /**
     * 绑定时间
     */
	@TableField("bind_card_time")
	private Date bindCardTime;
    /**
     * 是否绑定微信，0,未绑定 1,已绑定
     */
	@TableField("is_bind_wx")
	private Integer isBindWx;


	public Integer getId() {
		return id;
	}

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

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getBindCardTime() {
		return bindCardTime;
	}

	public void setBindCardTime(Date bindCardTime) {
		this.bindCardTime = bindCardTime;
	}

	public Integer getIsBindWx() {
		return isBindWx;
	}

	public void setIsBindWx(Integer isBindWx) {
		this.isBindWx = isBindWx;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
