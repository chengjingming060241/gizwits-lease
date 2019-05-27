package com.gizwits.lease.trade.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author gagi
 * @since 2017-07-29
 */
@TableName("trade_base")
public class TradeBase extends Model<TradeBase> {

    private static final long serialVersionUID = 1L;

    /**
     * 交易号，主键，按照一定规则生成
     */
    @TableId("trade_no")
	private String tradeNo;
    /**
     * 创建时间
     */
	private Date ctime;
    /**
     * 更改时间
     */
	private Date utime;
    /**
     * 交易状态，1交易创建，2交易成功，3交易失败
     */
	private Integer status;
    /**
     * 交易金额，单位为分
     */
	@TableField("total_fee")
	private Double totalFee;
    /**
     * 业务订单号，
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 1用户消费订单，2分润订单，3充值订单
     */
	@TableField("order_type")
	private Integer orderType;
    /**
     * 发起交易时传递的回调URL
     */
	@TableField("notify_url")
	private String notifyUrl;
    /**
     * 交易回调时间
     */
	@TableField("nofify_time")
	private Date nofifyTime;

	/**
	 * 交易类型
	 */
	@TableField("trade_type")
	private Integer tradeType;

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public Date getNofifyTime() {
		return nofifyTime;
	}

	public void setNofifyTime(Date nofifyTime) {
		this.nofifyTime = nofifyTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.tradeNo;
	}

}
