package com.gizwits.lease.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.gizwits.lease.order.dao.OrderExtPortDao;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderExtPortService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 * 订单扩展表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-24
 */
@Service
public class OrderExtPortServiceImpl extends ServiceImpl<OrderExtPortDao, OrderExtPort> implements OrderExtPortService {

    @Autowired
    private OrderBaseDao orderBaseDao;

    @Override
    public OrderBase getOrderBaseBySnoAndPort(String sno, Integer port,Integer status) {
        OrderBase orderBase = orderBaseDao.findLastUsingDevicePort(sno,port,status);
        return orderBase;
    }
}
