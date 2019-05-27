package com.gizwits.lease.order.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.constant.OrderDataFlowRoute;
import com.gizwits.lease.constant.OrderDataFlowType;
import com.gizwits.lease.order.dao.OrderDataFlowDao;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderDataFlow;
import com.gizwits.lease.order.service.OrderDataFlowService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单指令跟踪表 服务实现类
 * </p>
 *
 * @author Joke
 * @since 2018-02-11
 */
@Service
public class OrderDataFlowServiceImpl extends ServiceImpl<OrderDataFlowDao, OrderDataFlow> implements
		OrderDataFlowService {

	private void saveDataFlow(OrderBase orderBase, OrderDataFlowRoute route, OrderDataFlowType type, String data, String remark) {
		OrderDataFlow flow = new OrderDataFlow();
		flow.setOrderNo(orderBase.getOrderNo());
		flow.setSno(orderBase.getSno());
		flow.setMac(orderBase.getMac());
		flow.setRoute(route.getCode());
		flow.setType(type.getCode());
		flow.setData(data);
		flow.setRemark(remark);
		flow.setSysUserId(orderBase.getSysUserId());
		insert(flow);
	}

	@Override
	public void saveOldStatusData(OrderBase orderBase, String data) {
		OrderDataFlowType type = OrderDataFlowType.OLD_STATUS;
		saveDataFlow(orderBase, OrderDataFlowRoute.DEVICE_TO_SERVER, type, data, type.getMsg());
	}

	@Override
	public void saveSendCmdData(OrderBase orderBase, String data) {
		OrderDataFlowType type = OrderDataFlowType.SEND_CMD;
		saveDataFlow(orderBase, OrderDataFlowRoute.SERVER_TO_DEVICE, type, data, type.getMsg());
	}

	@Override
	public void saveUsingData(OrderBase orderBase, String data) {
		OrderDataFlowType type = OrderDataFlowType.USING;
		saveDataFlow(orderBase, OrderDataFlowRoute.DEVICE_TO_SERVER, type, data, type.getMsg());
	}

	@Override
	public void saveAbnormalData(OrderBase orderBase, OrderDataFlowRoute route, String data, String abnormalReason) {
		OrderDataFlowType type = OrderDataFlowType.ABNORMAL;
		saveDataFlow(orderBase, route, type, data, abnormalReason);
	}

	@Override
	public void saveFinishData(OrderBase orderBase, String data) {
		OrderDataFlowType type = OrderDataFlowType.FINISH;
		saveDataFlow(orderBase, OrderDataFlowRoute.DEVICE_TO_SERVER, type, data, type.getMsg());
	}

	@Override
	public void saveOtherData(OrderBase orderBase, String data) {
		OrderDataFlowType type = OrderDataFlowType.OTHER;
		saveDataFlow(orderBase, OrderDataFlowRoute.DEVICE_TO_SERVER, type, data, type.getMsg());
	}
}
