package com.gizwits.lease.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.TradeOrderType;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.TradeOrderType;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.dao.OrderBaseDao;
import com.gizwits.lease.order.dao.OrderStatusFlowDao;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderStatusFlow;
import com.gizwits.lease.order.service.OrderStatusFlowService;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
@Service
public class OrderStatusFlowServiceImpl extends ServiceImpl<OrderStatusFlowDao, OrderStatusFlow> implements OrderStatusFlowService {
    @Autowired
    private OrderBaseDao orderBaseDao;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    public boolean saveOne(OrderBase orderBase, Integer nowStatus, String creatorId){
        if (ParamUtil.isNullOrEmptyOrZero(orderBase)|| Objects.isNull(nowStatus)||ParamUtil.isNullOrEmptyOrZero(creatorId)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        OrderStatusFlow orderStatusFlow = new OrderStatusFlow();
        orderStatusFlow.setOrderNo(orderBase.getOrderNo());
        orderStatusFlow.setPreStatus(orderBase.getOrderStatus());
        orderStatusFlow.setNowStatus(nowStatus);
        orderStatusFlow.setCreatorId(creatorId);
        orderStatusFlow.setCtime(new Date());
        return insert(orderStatusFlow);
    }

    public boolean saveOne(UserWalletChargeOrder chargeOrder, Integer nowStatus, String creatorId){
        OrderStatusFlow orderStatusFlow = new OrderStatusFlow();
        orderStatusFlow.setOrderNo(chargeOrder.getChargeOrderNo());
        orderStatusFlow.setPreStatus(chargeOrder.getStatus());
        orderStatusFlow.setNowStatus(nowStatus);
        orderStatusFlow.setCreatorId(creatorId);
        orderStatusFlow.setCtime(new Date());
        return insert(orderStatusFlow);
    }

    @Override
    public boolean saveOne(String orderNo, Integer nowStatus, String creatorId) {
        Integer orderType = LeaseUtil.getOrderType(orderNo);
        if (orderType.equals(TradeOrderType.CONSUME.getCode())) {//消费订单
            OrderBase orderBase = orderBaseDao.selectById(orderNo);
            return saveOne(orderBase,nowStatus,creatorId);

        }else if(orderType.equals(TradeOrderType.CHARGE.getCode())){
            UserWalletChargeOrder chargeOrder = userWalletChargeOrderService.selectOne(new EntityWrapper<UserWalletChargeOrder>().eq("charge_order_no",orderNo));
            return saveOne(chargeOrder,nowStatus,creatorId);
        }
        return false;
    }


}
