package com.gizwits.lease.wallet.dto;


import com.gizwits.lease.wallet.entity.UserWallet;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yinhui on 2017/7/31.
 */
public class RechargeDto implements Serializable{
    private UserWallet userWallet;
    private List<RechargeMoneyDto> rechargeMoneyDtos ;

    public RechargeDto(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public RechargeDto() {}



    public UserWallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public List<RechargeMoneyDto> getRechargeMoneyDtos() {return rechargeMoneyDtos;}

    public void setRechargeMoneyDtos(List<RechargeMoneyDto> rechargeMoneyDtos) {this.rechargeMoneyDtos = rechargeMoneyDtos;}
}


