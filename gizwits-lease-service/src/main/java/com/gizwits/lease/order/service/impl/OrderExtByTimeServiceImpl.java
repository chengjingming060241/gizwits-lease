package com.gizwits.lease.order.service.impl;

import com.gizwits.lease.order.entity.OrderExtByTime;
import com.gizwits.lease.order.dao.OrderExtByTimeDao;
import com.gizwits.lease.order.service.OrderExtByTimeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单扩展表(按时) 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@Service
public class OrderExtByTimeServiceImpl extends ServiceImpl<OrderExtByTimeDao, OrderExtByTime> implements OrderExtByTimeService {
	
}
