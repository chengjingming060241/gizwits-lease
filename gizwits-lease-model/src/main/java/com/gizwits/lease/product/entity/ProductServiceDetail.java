package com.gizwits.lease.product.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 产品(或者设备)收费价格详情
 * </p>
 *
 * @author yinhui
 * @since 2017-07-13
 */
@TableName("product_service_detail")
public class ProductServiceDetail extends Model<ProductServiceDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 收费模式的id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;

	@TableField("service_mode_id")
	private Integer serviceModeId;
	/*
	 * 添加时间
	 */
	private Date ctime;
	/**
	 * 更新时间
	 */
	private Date utime;

    /**
     * 产品的id
     */
	@TableField("product_id")
	private Integer productId;
    /**
     * 收费类型
     */
	@TableField("service_type")
	private String serviceType;
    /**
     * 收费类型的id
     */
	@TableField("service_type_id")
	private Integer serviceTypeId;
	/**
	 * 模式相关指令
     */
	@TableField("command")
	private String command;
    /**
     * 单价
     */
	private Double price;
    /**
     * 数量
     */
	private Double num;
    /**
     * 单位
     */
	private String unit;
    /**
     * 系统用户的id，创建者
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
	private Integer isDeleted;

	@TableField("normal_num")
	private Double normalNum;

	@TableField("normal_price")
	private Double normalPrice;

	@TableField("cold_price")
	private Double coldPrice;

	@TableField("cold_num")
	private Double coldNum;

	@TableField("warm_price")
	private Double warmPrice;

	@TableField("warm_num")
	private Double warmNum;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCtime() {return ctime;}

	public void setCtime(Date ctime) {this.ctime = ctime;}

	public Date getUtime() {return utime;}

	public void setUtime(Date utime) {this.utime = utime;}

	public Integer getServiceModeId() {
		return serviceModeId;
	}

	public void setServiceModeId(Integer serviceModeId) {
		this.serviceModeId = serviceModeId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getNum() {
		return num;
	}

	public void setNum(Double num) {
		this.num = num;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Integer getStatus() {return status;}

	public void setStatus(Integer status) {this.status = status;}

	public String getSysUserName() {return sysUserName;}

	public void setSysUserName(String sysUserName) {this.sysUserName = sysUserName;}

	public Double getNormalNum() {
		return normalNum;
	}

	public void setNormalNum(Double normalNum) {
		this.normalNum = normalNum;
	}

	public Double getNormalPrice() {
		return normalPrice;
	}

	public void setNormalPrice(Double normalPrice) {
		this.normalPrice = normalPrice;
	}

	public Double getColdPrice() {
		return coldPrice;
	}

	public void setColdPrice(Double coldPrice) {
		this.coldPrice = coldPrice;
	}

	public Double getColdNum() {
		return coldNum;
	}

	public void setColdNum(Double coldNum) {
		this.coldNum = coldNum;
	}

	public Double getWarmPrice() {
		return warmPrice;
	}

	public void setWarmPrice(Double warmPrice) {
		this.warmPrice = warmPrice;
	}

	public Double getWarmNum() {
		return warmNum;
	}

	public void setWarmNum(Double warmNum) {
		this.warmNum = warmNum;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
