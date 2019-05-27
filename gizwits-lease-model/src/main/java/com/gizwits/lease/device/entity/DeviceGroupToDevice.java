package com.gizwits.lease.device.entity;

import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 设备组与设备的关系
 * </p>
 *
 * @author lilh
 * @since 2017-08-23
 */
@TableName("device_group_to_device")
public class DeviceGroupToDevice extends Model<DeviceGroupToDevice> {

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
     * 设备组id
     */
	@TableField("device_group_id")
	private Integer deviceGroupId;
    /**
     * 设备sno
     */
	@TableField("device_sno")
	private String deviceSno;

	@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted;

	private Date utime;


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

	public Integer getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(Integer deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	public String getDeviceSno() {
		return deviceSno;
	}

	public void setDeviceSno(String deviceSno) {
		this.deviceSno = deviceSno;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public Integer getIsDeleted() { return isDeleted; }

	public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

	public Date getUtime() { return utime; }

	public void setUtime(Date utime) { this.utime = utime; }
}
