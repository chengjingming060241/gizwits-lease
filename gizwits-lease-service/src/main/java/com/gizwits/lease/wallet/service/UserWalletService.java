package com.gizwits.lease.wallet.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.wallet.dto.RechargeDto;
import com.gizwits.lease.wallet.dto.UserWalletDto;
import com.gizwits.lease.wallet.dto.WalletPayDto;
import com.gizwits.lease.wallet.entity.UserWallet;

/**
 * <p>
 * 用户钱包表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-28
 */
public interface UserWalletService extends IService<UserWallet> {
    /**
     * 查询用户当前余额
     * @param userWalletDto
     * @return
     */
    UserWallet selectUserWallet(UserWalletDto userWalletDto);

    /**
     * 查询当前用户余额
     * @param userId
     * @param walletType
     * @return
     */
    UserWallet selectUserWallet(Integer userId,Integer walletType);

    /**
     * 重载更新
     * @param userId
     * @param fee
     * @param discountMoney
     * @param operatorType
     * @Param tradeNO
     * @return
     */
    UserWallet updateMoney(Integer userId,Double fee,Double discountMoney,Integer operatorType,String tradeNO);


    void pay(WalletPayDto data);

    /**
     * 获得充值页面数据
     * @param userWalletDto
     * @return
     */
    RechargeDto getRechargeDto(UserWalletDto userWalletDto);

    UserWallet create(Integer userId,Integer walletType);

    void refund(OrderBase orderBase, TradeBase tradeBase);

}
