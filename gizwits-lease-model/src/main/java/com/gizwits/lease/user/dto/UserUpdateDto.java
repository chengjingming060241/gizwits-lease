package com.gizwits.lease.user.dto;

import com.gizwits.boot.validators.Mobile;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by GaGi on 2017/8/18.
 */
public class UserUpdateDto {
    @NotBlank(message ="请输入手机号码!")
    @Mobile
    private String mobile ;

    private String nickname;

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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {return gender;}

    public void setGender(Integer gender) {this.gender = gender;}
}
