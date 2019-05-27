package com.gizwits.lease.wallet.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.order.dto.ChargeOrderDto;
import com.gizwits.lease.order.dto.DepositOrderDto;
import com.gizwits.lease.wallet.dto.DepositListDto;
import com.gizwits.lease.wallet.dto.DepositQueryDto;
import com.gizwits.lease.wallet.dto.RefundDto;
import com.gizwits.lease.wallet.dto.UserWalletChargeListDto;
import com.gizwits.lease.wallet.dto.UserWalletChargeOrderQueryDto;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;

/**
 * <p>
 * 用户钱包充值单表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-31
 */
public interface UserWalletChargeOrderService extends IService<UserWalletChargeOrder> {
    /**
     * 创建充值订单
     * @param chargeOrderDto
     * @return
     */
    UserWalletChargeOrder createRechargeOrder(ChargeOrderDto chargeOrderDto);

    UserWalletChargeOrder createDepositOrder(DepositOrderDto depositOrderDto);

    void updateChargeOrderStatus(String chargeOrderNo, Integer toStatus);

    /**
     * 更新充值单
     * @param totalFee
     * @return
     */
    public Boolean checkAndUpdateChargeOrder(Double totalFee, String orderNo,Integer payType,String tradeNo);

    void updateChargeOrderStatus(UserWalletChargeOrder userWalletChargeOrder, Integer toStatus);

    /**
     * 充值单列表
     * @param pageable
     * @return
     */
    Page<UserWalletChargeListDto> list(Pageable<UserWalletChargeOrderQueryDto> pageable);

    /**
     * 押金列表
     * @param pageable
     * @return
     */
    Page<DepositListDto> listDeposit(Pageable<DepositQueryDto> pageable);

    /**
     * 押金详情
     * @param chargeOrderNo
     * @return
     */
    DepositListDto depositDetail(String chargeOrderNo);

    /**
     * 退款页面
     * @param chargeOrderNo
     * @return
     */
    RefundDto refundInfo(String chargeOrderNo);

    /**
     * 退款
     * @param chargeOrderNo
     * @return
     */
    void refund(String chargeOrderNo);

    /**
     * 充值总金额
     * @return
     */
    double sumRealRecharge();

    /**
     * 已消费金额
     * @return
     */
    double sumRealUse();

    /**
     * 赠送总金额
     * @return
     */
    double sumRealGive();

    /**
     * 已产生的已完成状态的订单的赠送金额余额的总和
     * @return
     */
    double sumRealGiveUse();
}
