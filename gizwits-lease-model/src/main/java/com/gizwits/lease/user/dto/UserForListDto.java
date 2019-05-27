package com.gizwits.lease.user.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.enums.MutexType;
import com.gizwits.lease.user.entity.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 用户列表
 *
 * @author lilh
 * @date 2017/8/2 16:52
 */
public class UserForListDto {

    private static final String DEFAULT_REGION = "未知";

    private Integer id;

    /** 注册时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date ctime;

    /** 头像地址 */
    private String avatar;

    /** 昵称 */
    private String nickname;

    /** openid */
    private String openid;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** province + city */
    private String region;

    /** 性别 */
    private Integer gender;

    /** 性别描述 */
    private String genderDesc;

    /** 手机号 */
    private String mobile;

    /** 是否绑定手机 */
    private String bindMobile;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date authorizationTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date moveInBlackTime;

    public UserForListDto(User user) {
        BeanUtils.copyProperties(user, this);
        this.bindMobile = Objects.isNull(this.mobile) ? MutexType.NO.getDesc() : MutexType.YES.getDesc();
        if (StringUtils.isBlank(this.getProvince()) && StringUtils.isBlank(this.getCity())) {
            this.region = DEFAULT_REGION;
        } else {
            this.region = this.getProvince() + "/" + this.getCity();
        }
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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
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

    public String getBindMobile() {
        return bindMobile;
    }

    public void setBindMobile(String bindMobile) {
        this.bindMobile = bindMobile;
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
}
