package com.gizwits.lease.order.service.impl;

import com.gizwits.lease.order.entity.OrderExtByQuantity;
import com.gizwits.lease.order.dao.OrderExtByQuantityDao;
import com.gizwits.lease.order.service.OrderExtByQuantityService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单扩展记录表(按量) 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@Service
public class OrderExtByQuantityServiceImpl extends ServiceImpl<OrderExtByQuantityDao, OrderExtByQuantity> implements OrderExtByQuantityService {

}
