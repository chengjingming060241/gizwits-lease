package com.gizwits.lease.wallet.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 充值优惠表
 * </p>
 *
 * @author yinhui
 * @since 2017-08-21
 * @since 2017-08-17
 */
@TableName("recharge_money")
public class RechargeMoney extends Model<RechargeMoney> {

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
     * 类型 1，固定充值额，2，自定义充值额
     */
	private Integer type;
    /**
     * 充值金额
     */
	@TableField("charge_money")
	private Double chargeMoney;
    /**
     * 赠送金额
     */
	@TableField("discount_money")
	private Double discountMoney;
    /**
     * 优惠比例
     */
	private BigDecimal rate;
    /**
     * 创建者id
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 项目id：1艾芙芮  2卡励
     */
	@TableField("project_id")
	private Integer projectId;
    /**
     * 排序
     */
	private Integer sort;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Double getChargeMoney() {
		return chargeMoney;
	}

	public void setChargeMoney(Double chargeMoney) {
		this.chargeMoney = chargeMoney;
	}

	public Double getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(Double discountMoney) {
		this.discountMoney = discountMoney;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Integer getProjectId() {
		return projectId;
	}

	public void setProjectId(Integer projectId) {
		this.projectId = projectId;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
