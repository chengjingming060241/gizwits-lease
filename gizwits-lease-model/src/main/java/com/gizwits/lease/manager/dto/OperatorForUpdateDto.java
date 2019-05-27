package com.gizwits.lease.manager.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.gizwits.boot.dto.SysUserExtForAddDto;
import com.gizwits.boot.validators.Mobile;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigDecimal;

/**
 * Dto - 更新运营商
 *
 * @author lilh
 * @date 2017/8/1 14:38
 */
public class OperatorForUpdateDto {

    @NotNull
    private Integer id;

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$", message = "参数格式错误")
    @Length(max = 40)
    private String name;

    private String province;

    @NotBlank
    private String city;

    private String area;

    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]+$", message = "参数格式错误")
    @Length(max = 20)
    private String contact;

    @NotBlank
    @Mobile
    private String mobile;

    @NotBlank
    private String address;

    /** 押金金额,用户只有交过押金才可使用设备 **/
    private BigDecimal cashPledge;

    /** 用户充值优惠配置 **/
    private String rechargePromotion;

    /** 微信相关信息 */
    private Account account;

    private String logoUrl;

    private String phone;

    private String description;

    private String webSite;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /** 微信相关信息 */
    //private SysUserExtForAddDto ext;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public static class Account {
        private Integer id;

        /** 微信相关信息 */
        private SysUserExtForAddDto ext;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public SysUserExtForAddDto getExt() {
            return ext;
        }

        public void setExt(SysUserExtForAddDto ext) {
            this.ext = ext;
        }
    }
}
