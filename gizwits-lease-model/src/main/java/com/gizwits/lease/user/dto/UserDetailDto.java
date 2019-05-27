package com.gizwits.lease.user.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by GaGi on 2017/8/29.
 */
public class UserDetailDto {

    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
