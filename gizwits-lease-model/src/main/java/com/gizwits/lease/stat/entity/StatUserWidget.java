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
 * 设备订单看板数据统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
@TableName("stat_user_widget")
public class StatUserWidget extends Model<StatUserWidget> {

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
     * 当前用户总数
     */
	@TableField("total_count")
	private Integer totalCount;
    /**
     * 昨天用户增长率
     */
	@TableField("new_percent")
	private Double newPercent;
    /**
     * 今日活跃用户数
     */
	@TableField("active_count")
	private Integer activeCount;
    /**
     * 昨天用户活跃率
     */
	@TableField("active_percent")
	private Double activePercent;
	/**
	 * 今天新增用户数
	 */
	@TableField("new_count")
	private Integer newCount;


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

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Double getNewPercent() {
		return newPercent;
	}

	public void setNewPercent(Double newPercent) {
		this.newPercent = newPercent;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public Double getActivePercent() {
		return activePercent;
	}

	public void setActivePercent(Double activePercent) {
		this.activePercent = activePercent;
	}

	public Integer getNewCount() {
		return newCount;
	}

	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
