package com.gizwits.lease.order.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.constant.OrderDataFlowRoute;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderDataFlow;

/**
 * <p>
 * 订单指令跟踪表 服务类
 * </p>
 *
 * @author Joke
 * @since 2018-02-11
 */
public interface OrderDataFlowService extends IService<OrderDataFlow> {
	void saveOldStatusData(OrderBase orderBase, String data);
	void saveSendCmdData(OrderBase orderBase, String data);
	void saveUsingData(OrderBase orderBase, String data);
	void saveAbnormalData(OrderBase orderBase, OrderDataFlowRoute route, String data, String abnormalReason);
	void saveFinishData(OrderBase orderBase, String data);
	void saveOtherData(OrderBase orderBase, String data);
}
