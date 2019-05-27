package com.gizwits.lease.order.service;

import com.gizwits.lease.order.entity.OrderPayRecord;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhl
 * @since 2017-07-17
 */
public interface OrderPayRecordService extends IService<OrderPayRecord> {

    OrderPayRecord saveOne(String orderNo,Integer payType,String params);

    OrderPayRecord updateOne(String orderNo,Integer status);

    OrderPayRecord saveOne(String orderNo,Integer payType,String params,Integer status);
}
