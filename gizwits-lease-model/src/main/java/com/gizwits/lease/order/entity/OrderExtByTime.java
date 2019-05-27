package com.gizwits.lease.order.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 订单扩展表(按时)
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@TableName("order_ext_by_time")
public class OrderExtByTime extends Model<OrderExtByTime> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,与订单保持一致
     */
    @TableId("order_no")
	private Integer orderNo;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 服务开始时间
     */
	@TableField("start_time")
	private Date startTime;
    /**
     * 服务结束时间
     */
	@TableField("end_time")
	private Date endTime;
    /**
     * 购买时长
     */
	private Double duration;
    /**
     * 单价,元
     */
	private Double price;
    /**
     * 单位,分钟/小时/天
     */
	private String unit;


	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
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

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Double getDuration() {
		return duration;
	}

	public void setDuration(Double duration) {
		this.duration = duration;
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
