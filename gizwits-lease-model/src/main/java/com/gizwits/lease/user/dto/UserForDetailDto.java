package com.gizwits.lease.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.user.entity.User;

import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Dto - 用户详情
 *
 * @author lilh
 * @date 2017/8/2 17:46
 */
public class UserForDetailDto {

    private Integer id;

    /** 头像地址 */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 性别 */
    private Integer gender;

    /** 性别描述 */
    private String genderDesc;

    /** 手机号 */
    private String mobile;

    /** 微信openid */
    private String openid;

    /** 账户余额 **/
    private Double balance;
    /**
     * 用户状态
     */
    private Integer status;


    /** 授权时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date authorizationTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date moveInBlackTime;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public UserForDetailDto(User user) {
        BeanUtils.copyProperties(user, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getGenderDesc() {
        return genderDesc;
    }

    public void setGenderDesc(String genderDesc) {
        this.genderDesc = genderDesc;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Date getAuthorizationTime() {
        return authorizationTime;
    }

    public void setAuthorizationTime(Date authorizationTime) {
        this.authorizationTime = authorizationTime;
    }

    public Date getMoveInBlackTime() {
        return moveInBlackTime;
    }

    public void setMoveInBlackTime(Date moveInBlackTime) {
        this.moveInBlackTime = moveInBlackTime;
    }

    public Integer getStatus() { return status; }

    public void setStatus(Integer status) { this.status = status; }
}
