package com.gizwits.lease.order.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.BaseEnum;
import com.gizwits.lease.constant.BooleanEnum;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.dto.OrderTimerAddDto;
import com.gizwits.lease.order.dto.OrderTimerEnableDto;
import com.gizwits.lease.order.dto.OrderTimerUpdateDto;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderTimer;
import com.gizwits.lease.order.dao.OrderTimerDao;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderTimerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 订单定时任务 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-08-09
 */
@Service
public class OrderTimerServiceImpl extends ServiceImpl<OrderTimerDao, OrderTimer> implements OrderTimerService {

    @Autowired
    private OrderBaseService orderBaseService;

    public List<OrderTimer> getOrderTimersByOrderNo(String orderNo){
        if(ParamUtil.isNullOrEmptyOrZero(orderNo)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        OrderBase orderBase = orderBaseService.selectById(orderNo);
        if(orderBase==null){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }

        EntityWrapper<OrderTimer> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_no",orderNo)
                     .eq("is_deleted",BooleanEnum.FALSE.getCode());

        return selectList(entityWrapper);
    }

    @Override
    public List<OrderTimer> getAllUsingOrderTimer(){
        EntityWrapper<OrderTimer> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_enable", BooleanEnum.TRUE.getCode())
                     .eq("is_expire",BooleanEnum.FALSE.getCode())
                     .eq("is_deleted",BooleanEnum.FALSE.getCode());

        return selectList(entityWrapper);
    }

    @Override
    public void createOrderTimer(OrderTimerAddDto orderTimerDto){
        OrderBase orderBase = orderBaseService.selectById(orderTimerDto.getOrderNo());
        if(orderBase==null){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        if(!orderBase.getOrderStatus().equals(OrderStatus.USING.getCode())){
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_STATUS_ERROR);
        }

        OrderTimer orderTimer = new OrderTimer();
        BeanUtils.copyProperties(orderTimerDto,orderTimer);
        orderTimer.setCtime(new Date());
        orderTimer.setSno(orderBase.getSno());
        insert(orderTimer);
    }

    @Override
    public boolean updateOrderTimer(OrderTimerUpdateDto orderTimerUpdateDto){
        OrderTimer orderTimer = selectById(orderTimerUpdateDto.getId());
        if(Objects.isNull(orderTimer)){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        OrderBase orderBase = orderBaseService.selectById(orderTimerUpdateDto.getOrderNo());
        if(orderBase==null){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        if(!orderBase.getOrderStatus().equals(OrderStatus.USING.getCode())){
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_STATUS_ERROR);
        }

        orderTimer.setUtime(new Date());
        orderTimer.setTime(orderTimerUpdateDto.getTime());
        orderTimer.setWeekDay(orderTimerUpdateDto.getWeekDay());
        orderTimer.setCommand(orderTimerUpdateDto.getCommand());
        return updateById(orderTimer);
    }

    @Override
    public boolean deleteOrderTimer(List<Integer> ids){
        if(ParamUtil.isNullOrEmptyOrZero(ids)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        List<OrderTimer> list = new ArrayList<>();
        for(Integer id:ids){
            OrderTimer orderTimer = selectById(id);
            if(Objects.isNull(orderTimer)){
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
            orderTimer.setIsDeleted(BooleanEnum.TRUE.getCode());
            orderTimer.setUtime(new Date());
            list.add(orderTimer);
        }
        return updateBatchById(list);
    }

    @Override
    public boolean enableOrderTimer(OrderTimerEnableDto orderTimerEnableDto){
        OrderTimer orderTimer = selectById(orderTimerEnableDto.getId());
        if(Objects.isNull(orderTimer)){
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        if(orderTimer.getIsEnable().equals(orderTimerEnableDto.getIsEnable())){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        orderTimer.setIsEnable(orderTimerEnableDto.getIsEnable());
        orderTimer.setUtime(new Date());
        return updateById(orderTimer);
    }
}
