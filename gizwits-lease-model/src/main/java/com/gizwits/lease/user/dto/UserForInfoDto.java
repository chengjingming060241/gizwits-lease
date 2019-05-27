package com.gizwits.lease.user.dto;

import java.io.Serializable;

/**
 * 个人中心dto
 * Created by yinhui on 2017/8/11.
 */
public class UserForInfoDto implements Serializable {

    private String openid;
    /**
     * 用户名
     */
    private String username;

    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 1男 2女 3其他
     */
    private Integer gender;

    /**
     * 电话
     */
    private String mobile;

    private String province;

    private String city;

    /**
     * 押金
     */
    private Double deposit;

    /**
     * 是否绑定微信 0未绑定 1已绑定
     */
    private Integer isBindWeChat;

    /**
     * 是否绑定支付宝 0未绑定 1已绑定
     */
    private Integer isBindAlipay;

    /**
     * 是否有密码
     */
    private Integer hasPassword;


    /**
     * 充值卡数量
     */
    private Integer cardCount;


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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(Integer hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Integer getIsBindWeChat() {
        return isBindWeChat;
    }

    public void setIsBindWeChat(Integer isBindWeChat) {
        this.isBindWeChat = isBindWeChat;
    }

    public Integer getIsBindAlipay() {
        return isBindAlipay;
    }

    public void setIsBindAlipay(Integer isBindAlipay) {
        this.isBindAlipay = isBindAlipay;
    }

    public Integer getCardCount() {
        return cardCount;
    }

    public void setCardCount(Integer cardCount) {
        this.cardCount = cardCount;
    }
}
