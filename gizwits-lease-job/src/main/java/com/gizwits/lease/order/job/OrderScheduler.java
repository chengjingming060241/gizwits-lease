package com.gizwits.lease.order.job;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.utils.DateKit;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtByQuantity;
import com.gizwits.lease.order.entity.OrderStatusFlow;
import com.gizwits.lease.order.service.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by GaGi on 2017/8/26.
 */
@Component
public class OrderScheduler {
    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private OrderExtByQuantityService orderExtByQuantityService;
    @Autowired
    private OrderStatusFlowService orderStatusFlowService;
    @Autowired
    private OrderDataFlowService orderDataFlowService;

    protected static Logger logger = LoggerFactory.getLogger(OrderScheduler.class);

    @Scheduled(cron = "#{cronConfig.getEveryFiveMin()}")
    public void startOrderChangeExpired() {
        logger.info("[检查订单是否过期]开始");
        //查询订单状态为init和paying的订单
        List<Integer> statusList = new ArrayList<>();
        statusList.add(OrderStatus.INIT.getCode());
        statusList.add(OrderStatus.PAYING.getCode());
        EntityWrapper<OrderBase> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("order_status", statusList);
        entityWrapper.setSqlSelect("order_no,ctime,order_status,user_id");
        List<OrderBase> list = orderBaseService.selectList(entityWrapper);
        Date now = new Date();
        Integer status = OrderStatus.EXPIRE.getCode();
        for (int i = 0; i < list.size(); ++i) {
            OrderBase orderBase = list.get(i);
            Date orderTime = orderBase.getCtime();
            Date expiredTime = DateKit.addMinute(orderTime, 30);
            if (now.after(expiredTime)) {
                //记录订单状态流向
                logger.info("订单:" + orderBase.getOrderNo() + "从" + OrderStatus.getOrderStatus(orderBase.getOrderStatus()) + "修改成" + OrderStatus.getOrderStatus(status));
                OrderBase forUpdate = new OrderBase();
                forUpdate.setUtime(now);
                forUpdate.setOrderStatus(status);
                orderBaseService.update(forUpdate, new EntityWrapper<OrderBase>().eq("order_no", orderBase.getOrderNo()));
                orderStatusFlowService.saveOne(orderBase, status, orderBase.getUserId() + "");
            }
        }
        logger.info("[检查订单是否过期]结束");
    }

    /**
     * 设备离线后，需要后台检查订单是否超过租赁时间，超过标记为已完成
     */
    @Scheduled(cron = "#{cronConfig.getEveryFiveMin()}")
    public void startOrderUseExpired() {
        logger.info("[检查订单是否超过租赁时间] 开始！");
        //查出所有使用中订单
        Wrapper<OrderBase> wrapper = new EntityWrapper<>();
        wrapper.eq("order_status", OrderStatus.USING.getCode());
        wrapper.setSqlSelect("order_no,order_status,ctime,user_id");
        List<OrderBase> list = orderBaseService.selectList(wrapper);
        for (OrderBase orderBase : list)
            try {
                String orderNo = orderBase.getOrderNo();
                logger.info("[检查订单是否超过租赁时间] 开始处理订单号:{}", orderNo);
                //查出订单的有效时间
                Wrapper<OrderStatusFlow> flowWrapper = new EntityWrapper<>();
                flowWrapper.eq("order_no", orderNo).eq("now_status", OrderStatus.USING.getCode());
                OrderStatusFlow orderStatusFlow = orderStatusFlowService.selectOne(flowWrapper);
                OrderExtByQuantity orderExtByQuantity = orderExtByQuantityService.selectById(orderNo);
                if (Objects.isNull(orderExtByQuantity)) {
                    continue;
                }
                String unit = orderExtByQuantity.getUnit();

                long startTime = orderStatusFlow.getCtime().getTime();
                long leaseTime = (long) (orderExtByQuantity.getQuantity() * 1000);
                switch (unit) {
                    default:
                        logger.warn("[检查订单是否超过租赁时间] 订单{}未能识别时间单位:{}", orderNo, unit);
                        return;
                    case "年":
                        leaseTime *= 365 * 24 * 60 * 60;
                        break;
                    case "月":
                        leaseTime *= 30 * 24 * 60 * 60;
                        break;
                    case "天":
                        leaseTime *= 24 * 60 * 60;
                        break;
                    case "时":
                        leaseTime *= 60 * 60;
                        break;
                    case "小时":
                        leaseTime *= 60 * 60;
                        break;
                    case "分":
                        leaseTime *= 60;
                        break;
                    case "分钟":
                        leaseTime *= 60;
                        break;

                }
                long now = System.currentTimeMillis();
                Date endTime = new Date(startTime + leaseTime);
                logger.info("[检查订单是否超过租赁时间] 订单号:{}，开始时间:{}，租赁量:{}，租赁单位:{}，原定结束时间:{}",
                        orderNo, orderStatusFlow.getCtime(), orderExtByQuantity.getQuantity(), unit, endTime);
                if (now > endTime.getTime()) {
                    //更新为完成
                    logger.info("订单超出租赁时间，需要更新为已完成");
                    OrderStatusFlow forInsert = new OrderStatusFlow();
                    forInsert.setOrderNo(orderNo);
                    forInsert.setPreStatus(orderBase.getOrderStatus());
                    forInsert.setNowStatus(OrderStatus.FINISH.getCode());
                    forInsert.setCreatorId(orderBase.getUserId() + "");
                    forInsert.setCtime(endTime);
                    orderStatusFlowService.insert(forInsert);
                    OrderBase forUpdate = new OrderBase();
                    forUpdate.setOrderNo(orderNo);
                    forUpdate.setOrderStatus(OrderStatus.FINISH.getCode());
                    forUpdate.setUtime(endTime);
                    orderBaseService.updateById(forUpdate);
                    orderDataFlowService.saveFinishData(orderBase, "finished by scheduler");
                }
            } catch (Exception e) {
                logger.error("[检查订单是否超过租赁时间] 出错：", e);
            }

        logger.info("[检查订单是否超过租赁时间] 结束。");
    }


    @Scheduled(cron = "#{cronConfig.getUserTrend()}")
    public void statOrderPayed() {
        // 兼容一下已支付订单状态修改失败的情形
        logger.info("[兼容一下已支付订单状态修改失败的情形]开始");
        Date now = new Date();
        List<OrderBase> payedOrders = orderBaseService.selectList(new EntityWrapper<OrderBase>().eq("order_status", OrderStatus.PAYED.getCode()).le("pay_time", now));
        if (CollectionUtils.isNotEmpty(payedOrders)) {
            List<String> orderNos = payedOrders.stream().map(OrderBase::getOrderNo).collect(Collectors.toList());
            OrderBase forUpdate = new OrderBase();
            forUpdate.setOrderStatus(OrderStatus.USING.getCode());
            forUpdate.setUtime(now);
            EntityWrapper<OrderBase> entityWrapper = new EntityWrapper<>();
            entityWrapper.in("order_no", orderNos);
            orderBaseService.update(forUpdate, entityWrapper);
        }
        logger.info("[兼容一下已支付订单状态修改失败的情形]结束");
    }

    /**
     * 如下发指令后，设备在2分钟内有数据上报，并且该数据代表已经正在出水，但是10分钟内设备都没有上报出水完成，那么对应的订单标记完成，对应的出水口也标记为空闲。
     */
    @Scheduled(cron = "#{cronConfig.getEveryFiveMin()}")
    public void startOrderUse10MinExpired() {
        logger.info("[检查订单是否10分钟内设备都没有上报出水完成] 开始！");
        // 查出所有使用中订单
        Wrapper<OrderBase> wrapper = new EntityWrapper<>();
        wrapper.eq("order_status", OrderStatus.USING.getCode());
        List<OrderBase> orderBaseList = orderBaseService.selectList(wrapper);
        long now = System.currentTimeMillis();
        long duration = 10 * 60 * 1000;
        orderBaseList.stream().filter(item -> (now - item.getUtime().getTime()) >= duration).forEach(tem -> orderBaseService.finish(tem.getOrderNo()));
    }
}
