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
 * 用户地图分布统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
@TableName("stat_user_location")
public class StatUserLocation extends Model<StatUserLocation> {

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
     * 省id
     */
	@TableField("province_id")
	private Integer provinceId;
    /**
     * 省名称
     */
	private String province;
    /**
     * 省对应的用户数量
     */
	@TableField("user_count")
	private Integer userCount;
	/**
	 * 用户占比
	 */
	@TableField("proportion")
	private Double proportion;

	public Double getProportion() {
		return proportion;
	}

	public void setProportion(Double proportion) {
		this.proportion = proportion;
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

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
