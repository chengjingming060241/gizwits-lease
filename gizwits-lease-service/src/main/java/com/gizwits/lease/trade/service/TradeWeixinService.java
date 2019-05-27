package com.gizwits.lease.trade.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.lease.order.dto.PrePayDto;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.trade.entity.TradeBase;
import com.gizwits.lease.trade.entity.TradeWeixin;
import com.gizwits.lease.user.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhl on 2017/6/30.
 */
public interface TradeWeixinService extends IService<TradeWeixin> {


    ResponseObject getWxJsapipayInfo(String orderNo, Double fee, User user, SysUserExt sysUserExt, TradeBase tradeBase);

    /**
     * 微信支付回调处理
     *
     * @param request
     * @param response
     */
    void handleWxPayCallback(HttpServletRequest request, HttpServletResponse response);

    /**
     * 订单执行退款操作
     *
     * @param orderNo
     * @return
     */
    void handleWxRefund(String orderNo);

    /**
     * 微信预支付,跟客户端有关系
     *
     * @param prePayDto
     * @param userBrowserAgentType 用户浏览器类型
     * @return
     */
    ResponseObject prePay(PrePayDto prePayDto, Integer userBrowserAgentType);

    /**
     * 充值卡充值，微信预支付
     * 由于不需要sno参数，prePay(PrePayDto prePayDto, Integer userBrowserAgentType)方法不适用，此方法在其基础上做相应改动
     * @param prePayDto
     * @param userBrowserAgentType
     * @return
     */
    ResponseObject cardRechargePrePay(PrePayDto prePayDto, Integer userBrowserAgentType);

    /**
     * 创建tradeWeixin
     *
     * @param tradeBase
     * @param sysUserExt
     */
    void createTradeWeixin(TradeBase tradeBase, SysUserExt sysUserExt);

    /**
     * 查询订单是否已经支付
     *
     * @param orderNo
     * @return
     */
    boolean queryOrderPayStatus(String orderNo);

    /**
     * 退款，如果抛出异常表示退款失败，否则表示退款成功
     * @param orderBase
     * @param tradeBase
     */
    void refund(OrderBase orderBase, TradeBase tradeBase);

}
