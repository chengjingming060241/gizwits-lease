package com.gizwits.lease.wallet.dto;


import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/28.
 */
public class UserWalletUseDto implements Serializable{
    private UserWalletDto userWalletDto;
    private Double fee;
    private Integer operationType;
    private UserWalletChargeOrder userWalletChargeOrder;

    public UserWalletChargeOrder getUserWalletChargeOrder() {
        return userWalletChargeOrder;
    }

    public void setUserWalletChargeOrder(UserWalletChargeOrder userWalletChargeOrder) {
        this.userWalletChargeOrder = userWalletChargeOrder;
    }

    public UserWalletDto getUserWalletDto() {return userWalletDto;}

    public void setUserWalletDto(UserWalletDto userWalletDto) {this.userWalletDto = userWalletDto;}

    public Double getFee() {return fee;}

    public void setFee(Double fee) {this.fee = fee;}

    public Integer getOperationType() {return operationType;}

    public void setOperationType(Integer operationType) {this.operationType = operationType;}
}
