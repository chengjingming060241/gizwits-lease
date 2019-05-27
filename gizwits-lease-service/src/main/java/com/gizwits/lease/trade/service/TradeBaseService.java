package com.gizwits.lease.trade.service;

import com.gizwits.lease.trade.entity.TradeBase;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-29
 */
public interface TradeBaseService extends IService<TradeBase> {
    /**
     * 根据订单和支付回调路径
     * @param orderNo
     * @param fee
     * @param notifyUrl  @return tradeBase
     * @param tradeType
     * @param orderType
     */
    TradeBase createTrade(String orderNo, Double fee, String notifyUrl, Integer tradeType, Integer orderType);

    TradeBase createTradeBase(String orderNo, Double fee, String notifyUrl, Integer tradeType,Integer orderType);

    /**
     * 将tradeBase里面的状态更改为toStatus
     * @param tradeBase
     * @param toStatus
     * @return 修改成功与否
     */
    Boolean updateTradeStatus(TradeBase tradeBase,Integer toStatus);

    /**
     * 将tradeNo对应的tradeBase里面的状态更改为toStatus
     * @param tradeNo
     * @param toStatus
     * @return
     */
    Boolean updateTradeStatus(String tradeNo,Integer toStatus);

    /**
     * 根据交易号查找
     * @param tradeNo
     * @return
     */
    TradeBase  selectByTradeNo(String tradeNo);

}
