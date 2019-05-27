package com.gizwits.lease.product.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品数据点
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@TableName("product_data_point")
public class ProductDataPoint extends Model<ProductDataPoint> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;

	/**
	 * 产品id
	 */
	@TableField("product_id")
	private Integer productId;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 显示名称
     */
	@TableField("show_name")
	private String showName;
    /**
     * 标志名称
     */
	@TableField("identity_name")
	private String identityName;
    /**
     * 读写类型
     */
	@TableField("read_write_type")
	private String readWriteType;
    /**
     * 数据类型
     */
	@TableField("data_type")
	private String dataType;
    /**
     * 备注
     */
	private String remark;
	/**
	 * 告警级别
	 */
    @TableField("device_alarm_rank")
	private Integer deviceAlarmRank;

	/**
	 * 数据点返回的数据
	 */
	@TableField("value_limit")
	private String valueLimit;

	/**
	 * 是否监控数据点
     */
	@TableField("is_monit")
	private Integer isMonit;

	@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted;


	public Integer getIsMonit() {
		return isMonit;
	}

	public void setIsMonit(Integer isMonit) {
		this.isMonit = isMonit;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
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

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getIdentityName() {
		return identityName;
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}

	public String getReadWriteType() {
		return readWriteType;
	}

	public void setReadWriteType(String readWriteType) {
		this.readWriteType = readWriteType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getValueLimit() {
		return valueLimit;
	}

	public void setValueLimit(String valueLimit) {
		this.valueLimit = valueLimit;
	}

	public Integer getDeviceAlarmRank() {
		return deviceAlarmRank;
	}

	public void setDeviceAlarmRank(Integer deviceAlarmRank) {
		this.deviceAlarmRank = deviceAlarmRank;
	}

	public Integer getIsDeleted() { return isDeleted; }

	public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
