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
 * 看板统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@TableName("stat_widget")
public class StatWidget extends Model<StatWidget> {

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
	 * 修改时间
	 */

	private Date utime;
    /**
     * 归属系统用户id
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 设备总数
     */
	@TableField("device_total_count")
	private Integer deviceTotalCount;
    /**
     * 设备在线数
     */
	@TableField("device_online_count")
	private Integer deviceOnlineCount;
    /**
     * 设备新增数
     */
	@TableField("device_new_count")
	private Integer deviceNewCount;
    /**
     * 设备新增率
     */
	@TableField("device_new_percent")
	private Double deviceNewPercent;
    /**
     * 故障设备总数
     */
	@TableField("alarm_total_count")
	private Integer alarmTotalCount;
    /**
     * 故障新增数
     */
	@TableField("alarm_new_count")
	private Integer alarmNewCount;
    /**
     * 故障率
     */
	@TableField("alarm_percent")
	private Double alarmPercent;
    /**
     * 用户总人数
     */
	@TableField("user_total_count")
	private Integer userTotalCount;
    /**
     * 用户日增长率
     */
	@TableField("user_day_percent")
	private Double userDayPercent;
    /**
     * 用户近7天活跃率
     */
	@TableField("user_active_percent")
	private Double userActivePercent;
    /**
     * 投放点总数
     */
	@TableField("place_total_count")
	private Integer placeTotalCount;
	/**
	 * 订单新增率
	 */
	@TableField("order_new_percent")
	private Double orderNewPercent;

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public Double getOrderNewPercent() {
		return orderNewPercent;
	}

	public void setOrderNewPercent(Double orderNewPercent) {
		this.orderNewPercent = orderNewPercent;
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

	public Integer getDeviceTotalCount() {
		return deviceTotalCount;
	}

	public void setDeviceTotalCount(Integer deviceTotalCount) {
		this.deviceTotalCount = deviceTotalCount;
	}

	public Integer getDeviceOnlineCount() {
		return deviceOnlineCount;
	}

	public void setDeviceOnlineCount(Integer deviceOnlineCount) {
		this.deviceOnlineCount = deviceOnlineCount;
	}

	public Integer getDeviceNewCount() {
		return deviceNewCount;
	}

	public void setDeviceNewCount(Integer deviceNewCount) {
		this.deviceNewCount = deviceNewCount;
	}

	public Double getDeviceNewPercent() {
		return deviceNewPercent;
	}

	public void setDeviceNewPercent(Double deviceNewPercent) {
		this.deviceNewPercent = deviceNewPercent;
	}

	public Integer getAlarmTotalCount() {
		return alarmTotalCount;
	}

	public void setAlarmTotalCount(Integer alarmTotalCount) {
		this.alarmTotalCount = alarmTotalCount;
	}

	public Integer getAlarmNewCount() {
		return alarmNewCount;
	}

	public void setAlarmNewCount(Integer alarmNewCount) {
		this.alarmNewCount = alarmNewCount;
	}

	public Double getAlarmPercent() {
		return alarmPercent;
	}

	public void setAlarmPercent(Double alarmPercent) {
		this.alarmPercent = alarmPercent;
	}

	public Integer getUserTotalCount() {
		return userTotalCount;
	}

	public void setUserTotalCount(Integer userTotalCount) {
		this.userTotalCount = userTotalCount;
	}

	public Double getUserDayPercent() {
		return userDayPercent;
	}

	public void setUserDayPercent(Double userDayPercent) {
		this.userDayPercent = userDayPercent;
	}

	public Double getUserActivePercent() {
		return userActivePercent;
	}

	public void setUserActivePercent(Double userActivePercent) {
		this.userActivePercent = userActivePercent;
	}

	public Integer getPlaceTotalCount() {
		return placeTotalCount;
	}

	public void setPlaceTotalCount(Integer placeTotalCount) {
		this.placeTotalCount = placeTotalCount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
