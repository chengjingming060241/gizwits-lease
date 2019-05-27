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
 * 代理商表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public class Agent extends Model<Agent> {

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
     * 代理商名称
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
     * 代理商logo url
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
     * 父级代理商ID
     */
	@TableField("parent_agent_id")
	private Integer parentAgentId;

	/**
	 * 分润规则ID
     */
	@TableField("share_benefit_rule_id")
	private String shareBenefitRuleId;
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
	 * 对应的系统账号，用于登录
	 */
	@TableField("sys_account_id")
	private Integer sysAccountId;

	/**
	 * 状态：1,待分配 2,正常 3,暂停
	 */
	@TableField("status")
	private Integer status;

	public String getShareBenefitRuleId() {
		return shareBenefitRuleId;
	}

	public void setShareBenefitRuleId(String shareBenefitRuleId) {
		this.shareBenefitRuleId = shareBenefitRuleId;
	}
	/**
	 * 是否删除，0,未删除 1,已删除
	 */
	@TableLogic
	@TableField("is_deleted")
	private Integer isDeleted;

	@TableField("cover_level")
	private Integer coverLevel;

	public Integer getCoverLevel() {
		return coverLevel;
	}

	public void setCoverLevel(Integer coverLevel) {
		this.coverLevel = coverLevel;
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

	public Integer getParentAgentId() {
		return parentAgentId;
	}

	public void setParentAgentId(Integer parentAgentId) {
		this.parentAgentId = parentAgentId;
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

	public Integer getSysAccountId() {
		return sysAccountId;
	}

	public void setSysAccountId(Integer sysAccountId) {
		this.sysAccountId = sysAccountId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
