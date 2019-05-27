package com.gizwits.lease.device.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 设备收费模式设定(麻将机系统特有需求)
 * </p>
 *
 * @author zhl
 * @since 2017-09-04
 */
@TableName("device_service_mode_setting")
public class DeviceServiceModeSetting extends Model<DeviceServiceModeSetting> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 设备序列号
     */
	private String sno;
    /**
     * 为设备设置收费模式的系统用户
     */
	@TableField("sys_account_id")
	private Integer sysAccountId;
    /**
     * 是否免费：0，收费，1，免费
     */
	@TableField("is_free")
	private Integer isFree = 0 ;

	@TableField("is_deleted")
	private Integer isDeleted = 0 ;
	/**
	 * 分配对象的accountId，即设备的owner_id
     */
	@TableField("assign_account_id")
	private Integer assignAccountId;
	private Date ctime;
	private Date utime;

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getAssignAccountId() {
		return assignAccountId;
	}

	public void setAssignAccountId(Integer assignAccountId) {
		this.assignAccountId = assignAccountId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public Integer getSysAccountId() {
		return sysAccountId;
	}

	public void setSysAccountId(Integer sysAccountId) {
		this.sysAccountId = sysAccountId;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
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
