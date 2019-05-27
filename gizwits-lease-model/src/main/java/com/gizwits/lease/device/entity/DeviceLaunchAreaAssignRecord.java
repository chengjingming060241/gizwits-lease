package com.gizwits.lease.device.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 投放点分配记录
 * </p>
 *
 * @author lilh
 * @since 2017-09-02
 */
@TableName("device_launch_area_assign_record")
public class DeviceLaunchAreaAssignRecord extends Model<DeviceLaunchAreaAssignRecord> {

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
     * 投放点id
     */
	@TableField("device_launch_area_id")
	private Integer deviceLaunchAreaId;
    /**
     * 投放点名称
     */
	@TableField("device_launch_area_name")
	private String deviceLaunchAreaName;
    /**
     * 原来的拥有者
     */
	@TableField("source_owner_id")
	private Integer sourceOwnerId;
    /**
     * 分配之后的拥有者
     */
	@TableField("target_owner_id")
	private Integer targetOwnerId;
    /**
     * 操作类型，ASSIGN和UNBIND
     */
	@TableField("operate_type")
	private String operateType;
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

	public Integer getDeviceLaunchAreaId() {
		return deviceLaunchAreaId;
	}

	public void setDeviceLaunchAreaId(Integer deviceLaunchAreaId) {
		this.deviceLaunchAreaId = deviceLaunchAreaId;
	}

	public String getDeviceLaunchAreaName() {
		return deviceLaunchAreaName;
	}

	public void setDeviceLaunchAreaName(String deviceLaunchAreaName) {
		this.deviceLaunchAreaName = deviceLaunchAreaName;
	}

	public Integer getSourceOwnerId() {
		return sourceOwnerId;
	}

	public void setSourceOwnerId(Integer sourceOwnerId) {
		this.sourceOwnerId = sourceOwnerId;
	}

	public Integer getTargetOwnerId() {
		return targetOwnerId;
	}

	public void setTargetOwnerId(Integer targetOwnerId) {
		this.targetOwnerId = targetOwnerId;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
