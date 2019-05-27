package com.gizwits.lease.order.service;

import com.gizwits.lease.order.dto.OrderTimerAddDto;
import com.gizwits.lease.order.dto.OrderTimerEnableDto;
import com.gizwits.lease.order.dto.OrderTimerUpdateDto;
import com.gizwits.lease.order.entity.OrderTimer;
import com.baomidou.mybatisplus.service.IService;

import java.util.List;

/**
 * <p>
 * 订单定时任务 服务类
 * </p>
 *
 * @author zhl
 * @since 2017-08-09
 */
public interface OrderTimerService extends IService<OrderTimer> {

    /**
     * 查询指定订单定时任务
     * @param orderNo
     * @return
     */
    List<OrderTimer> getOrderTimersByOrderNo(String orderNo);

    /**
     * 查询所有可用定时任务
     * @return
     */
    List<OrderTimer> getAllUsingOrderTimer();

    /**
     * 创建定时
     * @param orderTimerDto
     */
    void createOrderTimer(OrderTimerAddDto orderTimerDto);

    /**
     * 修改定时
     * @param orderTimerUpdateDto
     * @return
     */
    boolean updateOrderTimer(OrderTimerUpdateDto orderTimerUpdateDto);

    /**
     * 删除定时
     * @param ids
     * @return
     */
    boolean deleteOrderTimer(List<Integer> ids);

    /**
     * 启禁用定时
     * @param orderTimerEnableDto
     * @return
     */
    boolean enableOrderTimer(OrderTimerEnableDto orderTimerEnableDto);
}
