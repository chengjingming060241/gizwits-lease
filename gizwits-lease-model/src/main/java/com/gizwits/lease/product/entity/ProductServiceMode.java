package com.gizwits.lease.product.entity;

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
 * 产品(或者设备)服务方式
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@TableName("product_service_mode")
public class ProductServiceMode extends Model<ProductServiceMode> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 服务方式名称
     */
	private String name;

    /**
     * 单位
     */
	private String unit;
	/**
	 * 服务类型
	 */
	@TableField("service_type")
	private String serviceType;

	/**
	 * 服务类型ID: 服务类型ID对应product_command_config的ID
     */
	@TableField("service_type_id")
	private Integer serviceTypeId;

	/**
	 * 在没收费模式详情的时候需要的指令,如免费模式时需要下发的指令
     */
	private String command;

    /**
     * 所属产品ID
     */
	@TableField("product_id")
	private Integer productId;
	/**
	 * 创建人id
	 */
	@TableField("sys_user_id")
	private Integer sysUserId;
	/**
	 *  创建者姓名
	 */
	@TableField("sys_user_name")
	private String sysUserName;
	/**
	 * 状态
	 */
	private Integer status;

	/**
	 * 是否删除：0,否；1,是
     */
	@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted=0;
	/**
	 * 是否免费: 0,收费,1,免费
     */
	@TableField("is_free")
	private Integer isFree=0;

	/**
	 * 工作模式
	 */
	@TableField("working_mode")
	private String workingMode;

	public String getWorkingMode() {
		return workingMode;
	}

	public void setWorkingMode(String workingMode) {
		this.workingMode = workingMode;
	}

	public Integer getIsFree() {
		return isFree;
	}

	public void setIsFree(Integer isFree) {
		this.isFree = isFree;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
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

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getServiceType() {return serviceType;}

	public void setServiceType(String serviceType) {this.serviceType = serviceType;}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getSysUserId() {return sysUserId;}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public Integer getStatus() {return status;}

	public void setStatus(Integer status) {this.status = status;}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
