package com.gizwits.lease.order.service.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.order.dao.OrderPayRecordDao;
import com.gizwits.lease.order.entity.OrderPayRecord;
import com.gizwits.lease.order.service.OrderPayRecordService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-07-17
 */
@Service
public class OrderPayRecordServiceImpl extends ServiceImpl<OrderPayRecordDao, OrderPayRecord> implements OrderPayRecordService {

    @Autowired
    private OrderPayRecordDao orderPayRecordDao;

    public OrderPayRecord saveOne(String orderNo,Integer payType,String params){
        OrderPayRecord payRecord = new OrderPayRecord();
        payRecord.setOrderNo(orderNo);
        payRecord.setPayType(payType);
        payRecord.setParams(params);
        payRecord.setCtime(new Date());
        payRecord.setStatus(OrderStatus.PAYING.getCode());
        insert(payRecord);
        return payRecord;
    }

    public OrderPayRecord saveOne(String orderNo,Integer payType,String params,Integer status){
        OrderPayRecord payRecord = new OrderPayRecord();
        payRecord.setOrderNo(orderNo);
        payRecord.setPayType(payType);
        payRecord.setParams(params);
        payRecord.setCtime(new Date());
        payRecord.setStatus(status);
        insert(payRecord);
        return payRecord;
    }


    public OrderPayRecord updateOne(String orderNo,Integer status){
        if(StringUtils.isEmpty(orderNo))
            return null;
        OrderPayRecord orderPayRecord = orderPayRecordDao.findByPayingOrderNo(orderNo);
        if(orderPayRecord==null)
            return null;
        orderPayRecord.setStatus(status);
        orderPayRecord.setUtime(new Date());
        updateById(orderPayRecord);
        return orderPayRecord;
    }
}
