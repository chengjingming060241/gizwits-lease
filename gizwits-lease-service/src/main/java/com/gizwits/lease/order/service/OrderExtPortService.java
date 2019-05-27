package com.gizwits.lease.order.service;

import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 订单扩展表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-24
 */
public interface OrderExtPortService extends IService<OrderExtPort> {

    /**
     * 通过设备sno和出水口查询最后一个使用中的订单
     * @param sno
     * @param port
     * @return
     */
    OrderBase getOrderBaseBySnoAndPort(String sno, Integer port,Integer status);
	
}
