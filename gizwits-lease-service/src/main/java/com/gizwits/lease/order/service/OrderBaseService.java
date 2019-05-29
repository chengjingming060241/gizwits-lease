package com.gizwits.lease.order.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;

import com.gizwits.lease.device.vo.DeviceUsingVo;
import com.gizwits.lease.enums.OrderAbnormalReason;
import com.gizwits.lease.order.dto.*;


import com.gizwits.lease.order.dto.OrderAppDetailDto;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.order.dto.OrderDetailDto;
import com.gizwits.lease.order.dto.OrderListDto;
import com.gizwits.lease.order.dto.OrderQueryByMobileDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.dto.PageOrderAppList;
import com.gizwits.lease.order.dto.PayOrderDto;
import com.gizwits.lease.order.dto.PrePayDto;
import com.gizwits.lease.order.dto.WXOrderListDto;
import com.gizwits.lease.order.dto.WXOrderQueryDto;

import com.gizwits.lease.order.dto.*;


import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.vo.AppOrderDetailVo;
import com.gizwits.lease.order.vo.AppOrderVo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
public interface OrderBaseService extends IService<OrderBase> {

	OrderBase getOrderBaseByTradeNo(String tradeNo);

	OrderBase getDeviceLastUsingOrder(String deviceId);
	OrderBase getDeviceLastOrderByStatus(String deviceId, Integer status);
	OrderBase getOrderBaseByOrderNo(String orderNo);

	/**
	 * 分页信息
	 */
	Page<OrderListDto> getOrderListDtoPage(OrderQueryDto orderQueryDto);

	/**
	 * 分页信息
	 */
	PageOrderAppList<OrderListDto> getOrderAppListDtoPage(OrderQueryDto orderQueryDto);

	/**
	 * 获得订单详情信息
	 */
	OrderDetailDto orderDetail(OrderBase orderBase);

	/**
	 * 获得订单详情信息
	 */
	OrderAppDetailDto orderAppDetail(OrderBase orderBase);

	/**
	 * 查找待分润的的订单
	 */
	List<OrderBase> getReadyForShareBenefit(String sno, Date lastExecuteTime);

	/**
	 * 改变订单的状态
	 */
	void updateOrderStatusAndHandle(OrderBase orderBase, Integer status);

	/**
	 * 改变订单的状态
	 */
	void updateOrderStatusAndHandle(String orderNo, Integer status);

	/*	*
        *微信订单列表
         * @param pageable
         * @return
         */
	Page<WXOrderListDto> getWXOrderListPage(Pageable<WXOrderQueryDto> pageable);

	/**
	 * 支付失败后的显示订单数据
	 */
	WXOrderListDto getWxPayingOrder(String deviceSno);

	/**
	 * 删除用户页面展示的订单
	 */
	void deleteUserShowOrder(List<String> orderNos);

	/**
	 * 根据openid查询当前的订单号和设备
	 * @param openid
	 * @return
	 */
	//AppOrderVo getUsingOrderByOpenid(String openid);

	/**
	 * 用户订单
	 */
	Page<WXOrderListDto> WxOrderListPage(Pageable<OrderQueryByMobileDto> pageable);

	/**
	 * 根据用户端串过来的dto创建订单
	 *
	 * @param userBrowserAgentType 用户浏览器类型
	 * @return orderBase实体
	 */
	AppOrderVo createOrder(PayOrderDto orderDto, Integer userBrowserAgentType);

	OrderBase createOrderForChargeCard(String cardNum, Device device);

	/**
	 * 根据openid查询当前的订单号和设备
	 */
	AppOrderVo getUsingOrderByUserIdentify(String openid);


	List<OrderBase> findByUserIdAndStatus(Integer userId, Integer orderStatus);

	void sendOrderMessage(OrderBase orderBase);

	void closeOrder(String orderNo);

	/**
	 * 支付宝支付
	 */
//	String alipayPrePay(PrePayDto prePayDto, Integer browserAgentType);


	/**
	 * 检查设备和订单,是否满足下单和支付的条件
	 *
	 * @param userBrowserAgentType 用户浏览器类型
	 */
	Map<String, Object> checkBeforeOrder(String sno, Integer userBrowserAgentType, String userIdentify, String mobile,Integer port);


	Boolean checkAndUpdateConsumeOrder(String orderNo, Double totalFee);

	/**
	 * 获取使用中的订单，下单成功后获取的订单
	 */
	AppOrderVo getForAppOrder(AppUsingOrderDto orderDto);

	OrderBase getUsingOrderByOpenid(String sno, String openid);

	/**
	 * 移动用户端订单详情
	 */
	AppOrderDetailVo getOrderDetailForApp(String orderNo);

	/**
	 * 获取用户使用中的设备
	 */
	List<DeviceUsingVo> getUsingDeviceList(String openid);

	AppOrderVo getOrderDetail(String orderNo);

	AppOrderVo judgeOrder(String orderNo);
	/**
	 * 处理异常订单
	 */
	void handleAbnormalOrder(OrderBase orderBase, OrderAbnormalReason reason);

    void finish(String orderNo);

    //处理已退款
    void handleRefundOrder(OrderBase orderBase, OrderAbnormalReason reason);

	/**
	 * 获取用户每条充值记录并联最后一次消费的投放点等信息
	 * @param userId
	 * @return
	 */
	LastOrderForUserWalletChargeOrderDto getLastOrderForUserWalletChargeOrder(Integer userId);
}

