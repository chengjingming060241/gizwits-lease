package com.gizwits.lease.user.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by zhl on 2017/9/8.
 */
public class UserForUpdateMobileDto {
    @NotNull
    private String openid;
    private String oldMobile;
    private String oldCode;
    @NotNull
    private String newMobile;
    @NotNull
    private String newCode;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOldMobile() {
        return oldMobile;
    }

    public void setOldMobile(String oldMobile) {
        this.oldMobile = oldMobile;
    }

    public String getOldCode() {
        return oldCode;
    }

    public void setOldCode(String oldCode) {
        this.oldCode = oldCode;
    }

    public String getNewMobile() {
        return newMobile;
    }

    public void setNewMobile(String newMobile) {
        this.newMobile = newMobile;
    }

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }
}
