package com.gizwits.lease.product.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 品牌表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public class Brand extends Model<Brand> {

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
     * 品牌名称
     */
	private String name;
    /**
     * 品牌序列号
     */
	@TableField("brand_serial_number")
	private String brandSerialNumber;
    /**
     * 品牌logo地址
     */
	@TableField("logo_url")
	private String logoUrl;
    /**
     * 品牌介绍
     */
	private String introduce;
    /**
     * 品牌创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 所属厂商(企业)
     */
	@TableField("manufacturer_id")
	private Integer manufacturerId;
    /**
     * 企业名称
     */
	@TableField("manufacturer_name")
	private String manufacturerName;


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

	public String getBrandSerialNumber() {
		return brandSerialNumber;
	}

	public void setBrandSerialNumber(String brandSerialNumber) {
		this.brandSerialNumber = brandSerialNumber;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(Integer manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getManufacturerName() {
		return manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
