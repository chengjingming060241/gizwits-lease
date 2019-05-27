package com.gizwits.lease.user.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 充值卡充值订单
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@TableName("user_charge_card_order")
public class UserChargeCardOrder extends Model<UserChargeCardOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 充值单号
     */
    @TableId("order_no")
	private String orderNo;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 支付时间
     */
	@TableField("pay_time")
	private Date payTime;
    /**
     * 充值卡号
     */
	@TableField("card_num")
	private String cardNum;
    /**
     * 充值金额
     */
	private Double money;
    /**
     * 支付方式
     */
	@TableField("pay_type")
	private Integer payType;
    /**
     * 充值订单状态
     */
	private Integer status;
    /**
     * 充值人姓名
     */
	private String username;
    /**
     * 充值人id
     */
	@TableField("user_id")
	private Integer userId;


	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getPayType() {
		return payType;
	}

	public void setPayType(Integer payType) {
		this.payType = payType;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	protected Serializable pkVal() {
		return this.orderNo;
	}

}
