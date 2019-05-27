package com.gizwits.lease.device.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 设备分配记录
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@TableName("device_assign_record")
public class DeviceAssignRecord extends Model<DeviceAssignRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;

	/**
	 * 创建时间
	 */
	private Date ctime;
    /**
     * 设备sno
     */
	private String sno;
    /**
     * 设备mac
     */
	private String mac;
    /**
     * 原运营商
     */
	@TableField("source_operator")
	private Integer sourceOperator;
    /**
     * 现运营商
     */
	@TableField("destination_operator")
	private Integer destinationOperator;
    /**
     * 创建人
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 创建人名称
     */
	@TableField("sys_user_name")
	private String sysUserName;

	/** 操作类型:ASSIGN和UNBIND */
	@TableField("operate_type")
	private String operateType;


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

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public Integer getSourceOperator() {
		return sourceOperator;
	}

	public void setSourceOperator(Integer sourceOperator) {
		this.sourceOperator = sourceOperator;
	}

	public Integer getDestinationOperator() {
		return destinationOperator;
	}

	public void setDestinationOperator(Integer destinationOperator) {
		this.destinationOperator = destinationOperator;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
