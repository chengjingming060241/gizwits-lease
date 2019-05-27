package com.gizwits.lease.wallet.dto;

import java.io.Serializable;

/**
 * 用户钱包dto
 * Created by yinhui on 2017/7/31.
 */
public class UserWalletDto implements Serializable {
    private String openid;
    private Integer walletType;
    private Double fee=0.00D;
    private String mobile;
    /***项目id 1艾芙芮 2卡励 3沁尔康 4麻将*/
    private Integer projectId;

    public String getOpenid() {return openid;}

    public void setOpenid(String openid) {this.openid = openid;}

    public Integer getProjectId() {return projectId;}

    public void setWalletType(Integer walletType) {this.walletType = walletType;}

    public void setProjectId(Integer projectId) {this.projectId = projectId;}

    public Integer getWalletType() {
        return walletType;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
