package com.gizwits.lease.order.service;

import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderStatusFlow;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
public interface OrderStatusFlowService extends IService<OrderStatusFlow> {
    boolean saveOne(OrderBase orderBase, Integer nowStatus, String creatorId);
    boolean saveOne(String  orderNo, Integer nowStatus, String creatorId);
}
