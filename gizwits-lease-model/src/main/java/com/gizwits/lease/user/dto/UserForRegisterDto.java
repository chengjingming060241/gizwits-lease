package com.gizwits.lease.user.dto;

import com.gizwits.boot.validators.Mobile;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

/**
 * 用户注册Dto
 * Created by yinhui on 2017/8/11.
 */
public class UserForRegisterDto implements Serializable {
    /**
     *  手机号
     */
    @NotBlank
    @Mobile
    private String mobile;
    /**
     * 短线验证码
     */
    @NotBlank
    private String message;
    /**
     * 密码
     */
    @NotBlank
    @Pattern(regexp = "^[0-9a-zA-Z\\-_%`~!@#\\u0024\\u005E&\\u002A\\u0028\\u0029\\u002B=]+$", message = "参数格式错误")
    @Length(min = 6, max = 18)
    private String password;
    /**
     * 微信
     */
    private String WeChatUnionId;
    /**
     * 支付宝
     */
    private String alipayUnionid;
    /**
     * 新浪
     */
    private String sinaUnionid;
    /**
     * 百度
     */
    private String baiduUniond;

    /**
     * 用户名
     */
    private String username;
    /**
     * 性别
     */
    private Integer gender;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMessage() {return message;}

    public void setMessage(String message) {this.message = message;}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWeChatUnionId() {
        return WeChatUnionId;
    }

    public void setWeChatUnionId(String weChatUnionId) {
        WeChatUnionId = weChatUnionId;
    }

    public String getAlipayUnionid() {
        return alipayUnionid;
    }

    public void setAlipayUnionid(String alipayUnionid) {
        this.alipayUnionid = alipayUnionid;
    }

    public String getSinaUnionid() {
        return sinaUnionid;
    }

    public void setSinaUnionid(String sinaUnionid) {
        this.sinaUnionid = sinaUnionid;
    }

    public String getBaiduUniond() {
        return baiduUniond;
    }

    public void setBaiduUniond(String baiduUniond) {
        this.baiduUniond = baiduUniond;
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public Integer getGender() {return gender;}

    public void setGender(Integer gender) {this.gender = gender;}
}
