package com.gizwits.lease.order.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 订单定时任务
 * </p>
 *
 * @author zhl
 * @since 2017-08-09
 */
@TableName("order_timer")
public class OrderTimer extends Model<OrderTimer> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 关联订单号
     */
	@TableField("order_no")
	private String orderNo;
    /**
     * 设备SNO
     */
	private String sno;
    /**
     * 定时周几执行，多个日期用逗号隔开：1周一；2周二；3周三；4周四；5周五；6周六；7周日
     */
	@TableField("week_day")
	private String weekDay;
    /**
     * 执行时间，24小时制，如: 14:30:00
     */
	private String time;
    /**
     * 是否启用：0，否；1，是
     */
	@TableField("is_enable")
	private Integer isEnable;
    /**
     * 是否过期，订单过期后所有定时无效
     */
	@TableField("is_expire")
	private Integer isExpire;

	/**
	 * 是否删除0，否；1，是
     */
	@TableField("is_deleted")
	private Integer isDeleted;
    /**
     * 控制指令内容
     */
	private String command;
    /**
     * 创建时间
     */
	private Date ctime;
	private Date utime;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public Integer getIsExpire() {
		return isExpire;
	}

	public void setIsExpire(Integer isExpire) {
		this.isExpire = isExpire;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
