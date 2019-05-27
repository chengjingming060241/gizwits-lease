package com.gizwits.lease.manager.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 厂商(或企业)表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public class Manufacturer extends Model<Manufacturer> {

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
     * 企业名称
     */
	private String name;
    /**
     * 所属行业
     */
	private String industry;
    /**
     * 公司官网
     */
	@TableField("web_site")
	private String webSite;
    /**
     * 子域名
     */
	@TableField("sub_domain")
	private String subDomain;
    /**
     * 公司logo url
     */
	@TableField("logo_url")
	private String logoUrl;
    /**
     * 企业电话
     */
	private String phone;
    /**
     * 手机号码
     */
	private String mobile;
    /**
     * 联系人
     */
	private String contact;
    /**
     * 部门
     */
	private String department;
    /**
     * 电子邮件
     */
	private String email;
    /**
     * QQ号码
     */
	private String qq;
    /**
     * 省
     */
	private String province;
    /**
     * 市
     */
	private String city;
    /**
     * 区/县
     */
	private String area;
    /**
     * 详细地址
     */
	private String address;
    /**
     * 父级企业ID
     */
	@TableField("parent_manufacturer_id")
	private Integer parentManufacturerId;

	/**
	 * 是否删除
	 */
	@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted;

	/**
	 * 对应系统用户ID
	 */
	@TableField("sys_account_id")
	private Integer sysAccountId;

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

	/**
	 * 机智云平台产品对应的企业id
	 */
	@TableField("enterprise_id")
	private String enterpriseId;

	/**
	 * 机智云平台产品对应的企业secret
	 */
	@TableField("enterprise_secret")
	private String enterpriseSecret;

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

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getSubDomain() {
		return subDomain;
	}

	public void setSubDomain(String subDomain) {
		this.subDomain = subDomain;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getParentManufacturerId() {
		return parentManufacturerId;
	}

	public void setParentManufacturerId(Integer parentManufacturerId) {
		this.parentManufacturerId = parentManufacturerId;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getSysAccountId() {
		return sysAccountId;
	}

	public void setSysAccountId(Integer sysAccountId) {
		this.sysAccountId = sysAccountId;
	}

	public String getSysUserName() {
		return sysUserName;
	}

	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
	}

	public String getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(String enterpriseId) {
        this.enterpriseId = enterpriseId;
    }

    public String getEnterpriseSecret() {
        return enterpriseSecret;
    }

    public void setEnterpriseSecret(String enterpriseSecret) {
        this.enterpriseSecret = enterpriseSecret;
    }

    @Override
	protected Serializable pkVal() {
		return this.id;
	}

}
