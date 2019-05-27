package com.gizwits.lease.manager.dto;

import java.math.BigDecimal;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.gizwits.boot.dto.SysUserForAddDto;
import com.gizwits.boot.validators.Mobile;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 添加运营商
 *
 * @author lilh
 * @date 2017/7/31 15:37
 */
public class OperatorForAddDto {

    /** 运营商名称 */
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$", message = "参数格式错误")
    @Length(max = 40)
    private String name;

    /** 联系人 */
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$", message = "参数格式错误")
    @Length(max = 20)
    private String contact;

    /** 手机号 */
    @NotBlank
    @Mobile
    private String mobile;

    /** 省 */
    @NotBlank
    private String province;

    /** 市 */
    @NotBlank
    private String city;

    /** 区/县 */
    private String area;

    /** 详细地址 */
    @NotBlank
    private String address;

    /** 是否有系统账号,1:有, 0:无 */
    @NotNull
    private Integer bindingExistAccount;

    /** 押金金额,用户只有交过押金才可使用设备 **/
    private BigDecimal cashPledge;

    /** 用户充值优惠配置 **/
    private String rechargePromotion;

    /** 绑定的系统账号，bindingExistAccount为1时有效 */
    private Integer bindingAccountId;

    private String logoUrl;

    private String phone;

    private String description;

    private String webSite;

    @Valid
    private SysUserForAddDto user;

    private Integer coverLevel;

    private Integer isAllot;

    public Integer getIsAllot() {
        return isAllot;
    }

    public void setIsAllot(Integer isAllot) {
        this.isAllot = isAllot;
    }

    public Integer getCoverLevel() {
        return coverLevel;
    }

    public void setCoverLevel(Integer coverLevel) {
        this.coverLevel = coverLevel;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public BigDecimal getCashPledge() {
        return cashPledge;
    }

    public void setCashPledge(BigDecimal cashPledge) {
        this.cashPledge = cashPledge;
    }

    public String getRechargePromotion() {
        return rechargePromotion;
    }

    public void setRechargePromotion(String rechargePromotion) {
        this.rechargePromotion = rechargePromotion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public Integer getBindingExistAccount() {
        return bindingExistAccount;
    }

    public void setBindingExistAccount(Integer bindingExistAccount) {
        this.bindingExistAccount = bindingExistAccount;
    }

    public Integer getBindingAccountId() {
        return bindingAccountId;
    }

    public void setBindingAccountId(Integer bindingAccountId) {
        this.bindingAccountId = bindingAccountId;
    }

    public SysUserForAddDto getUser() {
        return user;
    }

    public void setUser(SysUserForAddDto user) {
        this.user = user;
    }
}
