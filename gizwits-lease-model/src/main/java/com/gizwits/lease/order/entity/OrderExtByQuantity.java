package com.gizwits.lease.order.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 订单扩展记录表(按量)
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@TableName("order_ext_by_quantity")
public class OrderExtByQuantity extends Model<OrderExtByQuantity> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,与订单保持一致
     */
    @TableId("order_no")
	private String orderNo;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 购买的量
     */
	private Double quantity;
    /**
     * 单价,元
     */
	private Double price;
    /**
     * 单位,立方
     */
	private String unit;


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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	protected Serializable pkVal() {
		return this.orderNo;
	}

}
