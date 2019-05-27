package com.gizwits.lease.wallet.dto;

import com.gizwits.lease.operator.entity.OperatorExt;
import com.gizwits.lease.wallet.entity.UserWallet;

/**
 * Created by zhl on 2017/8/11.
 */
public class UserWalletPreVO {

    private UserWallet userWallet;

    private OperatorExt operatorExt;

    public UserWallet getUserWallet() {
        return userWallet;
    }

    public void setUserWallet(UserWallet userWallet) {
        this.userWallet = userWallet;
    }

    public OperatorExt getOperatorExt() {
        return operatorExt;
    }

    public void setOperatorExt(OperatorExt operatorExt) {
        this.operatorExt = operatorExt;
    }
}
