package com.gizwits.lease.stat.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单分析统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@TableName("stat_order")
public class StatOrder extends Model<StatOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 归属系统用户id
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 设备id
     */
	@TableField("sno")
	private String sno;
    /**
     * 运营商id
     */
	@TableField("operator_id")
	private Integer operatorId;
    /**
     * 订单总金额
     */
	@TableField("order_amount")
	private Double orderAmount;
    /**
     * 订单数量
     */
	@TableField("order_count")
	private Integer orderCount;

	@TableField("ordered_percent")
	private Double orderedPercent;

	@TableField("order_amount_wx")
	private Double orderAmountWx;

	@TableField("order_count_wx")
	private Integer orderCountWx;

	@TableField("order_amount_wallet")
	private Double orderAmountWallet;

	@TableField("order_count_wallet")
	private Integer orderCountWallet;

	public Double getOrderedPercent() {
		return orderedPercent;
	}

	public void setOrderedPercent(Double orderedPercent) {
		this.orderedPercent = orderedPercent;
	}

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

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public Double getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Double orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public Double getOrderAmountWx() {
		return orderAmountWx;
	}

	public void setOrderAmountWx(Double orderAmountWx) {
		this.orderAmountWx = orderAmountWx;
	}

	public Integer getOrderCountWx() {
		return orderCountWx;
	}

	public void setOrderCountWx(Integer orderCountWx) {
		this.orderCountWx = orderCountWx;
	}

	public Double getOrderAmountWallet() {
		return orderAmountWallet;
	}

	public void setOrderAmountWallet(Double orderAmountWallet) {
		this.orderAmountWallet = orderAmountWallet;
	}

	public Integer getOrderCountWallet() {
		return orderCountWallet;
	}

	public void setOrderCountWallet(Integer orderCountWallet) {
		this.orderCountWallet = orderCountWallet;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
