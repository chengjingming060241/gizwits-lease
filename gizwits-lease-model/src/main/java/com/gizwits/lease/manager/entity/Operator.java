package com.gizwits.lease.manager.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 运营商表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public class Operator extends Model<Operator> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 运营商名称
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
     * 描述
     */
    private String description;
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

    /***
     * 对应的系统id
     */
    @TableField("sys_account_id")
    private Integer sysAccountId;

    /**
     * 创建人
     */
    @TableField("sys_user_id")
    private Integer sysUserId;

    /**
     * 创建人
     */
    @TableField("sys_user_name")
    private String sysUserName;

    /**
     * 分润规则
     */
    @TableField("share_benefit_rule_id")
    private String shareBenefitRuleId;

    private Integer status;

    @TableField("is_allot")
    private Integer isAllot = new Integer(0) ;

    @TableField("cover_level")
    private Integer coverLevel;

    @TableLogic
    @TableField("is_deleted")
    private Integer isDeleted;

    public Integer getCoverLevel() {
        return coverLevel;
    }

    public void setCoverLevel(Integer coverLevel) {
        this.coverLevel = coverLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
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

    public String getShareBenefitRuleId() {
        return shareBenefitRuleId;
    }

    public void setShareBenefitRuleId(String shareBenefitRuleId) {
        this.shareBenefitRuleId = shareBenefitRuleId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsAllot() {return isAllot;}

    public void setIsAllot(Integer isAllot) {this.isAllot = isAllot;}

    public Integer getIsDeleted() { return isDeleted; }

    public void setIsDeleted(Integer isDeleted) { this.isDeleted = isDeleted; }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
