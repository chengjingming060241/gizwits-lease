package com.gizwits.lease.listener;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.constant.OrderDataFlowRoute;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.StatusCommandType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceExt;
import com.gizwits.lease.device.service.DeviceExtService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.OrderAbnormalReason;
import com.gizwits.lease.event.WxPayCallbackEvent;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.entity.OrderExtPort;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderDataFlowService;
import com.gizwits.lease.order.service.OrderExtPortService;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.refund.service.RefundApplyService;
import com.gizwits.lease.util.DeviceUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhl on 2017/7/19.
 */
@Component
public class WxPayCallbackListener {

    private static Logger logger = LoggerFactory.getLogger("ORDER_LOGGER");

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RefundApplyService refundApplyService;

    @Autowired
    private OrderDataFlowService orderDataFlowService;

    @Autowired
    private OrderExtPortService orderExtPortService;

    @Autowired
    private DeviceExtService deviceExtService;

    /**
     * 微信支付成功回调时间
     *
     * @param wxPayCallbackEvent
     */
    @Async
    @EventListener({WxPayCallbackEvent.class})
    public void handleWxPayCallback(WxPayCallbackEvent wxPayCallbackEvent) {
        OrderBase orderBase = wxPayCallbackEvent.getOrderBase();
        logger.info("=====>> 微信支付回调成功order{},准备下发指令:{}", orderBase.getOrderNo(), orderBase.getCommand());

        if (logger.isDebugEnabled()) {
            logger.debug("====>>> 微信支付回调订单信息:{}", JSONObject.toJSONString(orderBase));
        }

        Device device = deviceService.selectById(orderBase.getSno());
        Product product = productService.selectById(device.getProductId());
        JSONObject nowData = redisService.getDeviceCurrentStatus(product.getGizwitsProductKey(), device.getMac());
        orderDataFlowService.saveOldStatusData(orderBase, nowData == null ? null : nowData.toJSONString());
        long controlTime = System.currentTimeMillis();

        if (sandCommand(orderBase)) {

            // 设备启动成功
            checkDeviceStatus(orderBase, device, product, controlTime);
            // logger.info("修改订单orderNo = {}状态为使用中", orderBase.getOrderNo());
            // orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.USING.getCode());
            OrderExtPort orderExtPort =
                    orderExtPortService.selectOne(
                            new EntityWrapper<OrderExtPort>()
                                    .eq("order_no", orderBase.getOrderNo())
                                    .eq("sno", orderBase.getSno()));

            if (!ParamUtil.isNullOrEmptyOrZero(orderExtPort)) {
                DeviceExt deviceExtPort = deviceExtService.selectBySnoAndPort(orderBase.getSno(), orderExtPort.getPort());

                if (!ParamUtil.isNullOrEmptyOrZero(deviceExtPort)) {
                    logger.info("设备mac：{},出水口port：{}状态为使用中", deviceExtPort.getMac(), deviceExtPort.getPort());
                    deviceExtPort.setStatus(DeviceStatus.USING.getCode());
                    deviceExtPort.setUtime(new Date());
                    deviceExtService.updateById(deviceExtPort);

                    //设备有出水口为使用中则设备为使用中
                    device.setUtime(new Date());
                    device.setWorkStatus(DeviceStatus.USING.getCode());
                    deviceService.updateById(device);
                }
            }
        }
    }

    private boolean sandCommand(OrderBase orderBase) {
        orderDataFlowService.saveSendCmdData(orderBase, orderBase.getCommand());
        boolean flag = false;

        try {
            flag = deviceService.remoteDeviceControlByOrder(orderBase);
        } catch (Exception e) {
            logger.warn("=====>>> 设备:{},下发订单指令:{},请求平台接口错误!!!执行退款操作", orderBase.getSno(), orderBase.getCommand());
        }

        if (flag) {//设备启动成功
            return true;
        } else {

            // 场景一：云端下发失败，立即退款。
            // 延迟1秒再处理，因为收到支付完成的回调后，会先开启事务，锁住订单，使订单变成支付完成状态，
            // 但下发指令是异步的，可能会在支付完成的事务还没提交时，就进入了退款逻辑，这时候更新订单为退款中状态会死锁。
            sleep(1000);
            logger.info("=====>>> 设备:{},下发订单指令:{},失败!!!执行退款操作", orderBase.getSno(), orderBase.getCommand());
            orderDataFlowService.saveAbnormalData(orderBase, OrderDataFlowRoute.SERVER_TO_DEVICE, orderBase.getCommand()
                    , OrderAbnormalReason.SEND_COMMAND_FAIL.getDescription());
            orderBaseService.handleAbnormalOrder(orderBase, OrderAbnormalReason.SEND_COMMAND_FAIL);
            refundApplyService.refund(orderBase);
            return false;
        }
    }

    private void checkDeviceStatus(OrderBase orderBase, Device device, Product product, long firstControlTime){
        logger.info("====>>> 订单order:{},订单指令下发成功，1秒后开始检查设备上报", orderBase.getOrderNo());

        if (!redisService.containsProductStatusCommand(product.getGizwitsProductKey(), StatusCommandType.USING.getCode())){
            logger.info("====>>> 设备{}对应的产品{}没有设置使用中状态匹配的数据点", device.getMac(), product.getName());
            orderDataFlowService.saveAbnormalData(orderBase, OrderDataFlowRoute.SERVER_TO_DEVICE, null
                    , OrderAbnormalReason.STATUS_COMMAND_NULL.getDescription());

            // 设置订单异常
            orderBaseService.handleAbnormalOrder(orderBase, OrderAbnormalReason.STATUS_COMMAND_NULL);

            // 执行退款
            refundApplyService.refund(orderBase);
            return;
        }
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        long controlTime = firstControlTime;
        long controlTimeOut = Long.parseLong(commonSystemConfig.getControlTimeOut()) * 1000;
        int currentTryTimes = 0;
        int limitTryTimes = Integer.parseInt(commonSystemConfig.getControlTryTimes());
        JSONObject commandJson =
                redisService.getProductStatusCommand(product.getGizwitsProductKey(), StatusCommandType.USING.getCode());
        Date preStatusTime = null;

        do {
            sleep(1000);
            // 判断设备上报的数据点是否匹配使用中状态
            JSONObject nowData = redisService.getDeviceCurrentStatus(product.getGizwitsProductKey(), device.getMac());

            if(nowData != null) {
                Date curStatusTime = nowData.getDate("status_time");

                if (curStatusTime == null || curStatusTime.getTime() <= firstControlTime) {
                    logger.info("====>>> 设备{}在服务器下发指令后，暂未上报数据", device.getMac());
                } else {
                    logger.info("====>>> 获取缓存中设备上报数据点，上报时间{}，内容:{}",
                            DateFormatUtils.format(curStatusTime, Constants.DEFAULT_DATE_PATTERN),nowData.toJSONString());

                    if (DeviceUtil.equalCommandAndRealTimeData(commandJson, nowData)) {
                        logger.info("====>>> 设备{}成功上报使用中状态，即将改变订单状态", device.getMac());
                        orderDataFlowService.saveUsingData(orderBase, nowData.toJSONString());

                        // 取水量
                        Double intakeWater = nowData.getDouble("a21QRcodePaymentupload_Consumption");
                        orderBase.setIntakeWater(intakeWater);

                        orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.USING.getCode());

                        if(device.getAbnormalTimes() > 0 || device.getLock()) {
                            // 解锁设备
                            deviceService.updateLockFlag(device.getSno(), 0, false);
                        }
                        return;
                    }
                    logger.info("====>>> 设备{}上报不符合使用中状态", device.getMac());

                    if(preStatusTime == null || curStatusTime.getTime() > preStatusTime.getTime()) {
                        orderDataFlowService.saveOtherData(orderBase, nowData.toJSONString());
                        preStatusTime = curStatusTime;
                    }
                }
            } else {
                logger.info("====>>> 设备{}在缓存中没有数据", device.getMac());
            }

            // 判断是否超时
            long now = System.currentTimeMillis();

            if (now - controlTime > controlTimeOut) {

                if (preStatusTime == null || preStatusTime.getTime() <= firstControlTime) {
                    logger.info("====>>> 设备{}在{}秒内没有上报过数据！！！", device.getMac(), controlTimeOut / 1000);

                    if (currentTryTimes >= limitTryTimes) {
                        logger.info("====>>> 订单{}达到重发上限{}次，处理为异常", orderBase.getOrderNo(), limitTryTimes);
                        orderDataFlowService.saveAbnormalData(orderBase, OrderDataFlowRoute.DEVICE_TO_SERVER, null
                                , OrderAbnormalReason.DEVICE_STATUS_TIMEOUT.getDescription());
                        handleDeviceStatusTimeOut(orderBase, device);
                        return;
                    }
                } else {
                    logger.info("====>>> 设备{}在{}秒内上报的数据不符合使用中状态的配置！！！", device.getMac(), controlTimeOut / 1000);

                    if (currentTryTimes >= limitTryTimes) {
                        logger.info("====>>> 订单{}达到重发上限{}次，处理为异常", orderBase.getOrderNo(), limitTryTimes);
                        orderDataFlowService.saveAbnormalData(orderBase, OrderDataFlowRoute.DEVICE_TO_SERVER, nowData.toJSONString()
                                , OrderAbnormalReason.DEVICE_STATUS_WRONG.getDescription());
                        handleDeviceStatusWrong(orderBase, device);
                        return;
                    }
                }
                currentTryTimes++;
                logger.info("====>>> 订单{}即将重发指令，当前重发次数：{}", orderBase.getOrderNo(), currentTryTimes);
                controlTime = System.currentTimeMillis();

                if (!sandCommand(orderBase)) {
                    return;
                }
            }
        } while (true);
    }

    /**
     * 场景二：下发后设备上报的数据点不匹配使用中状态，M分钟内重发N次后仍然不匹配，则立即退款，锁定设备，生成工单，解决后人工解锁。
     * @param orderBase
     * @param device
     */
    private void handleDeviceStatusWrong(OrderBase orderBase, Device device){
        // 设置订单异常
        orderBaseService.handleAbnormalOrder(orderBase, OrderAbnormalReason.DEVICE_STATUS_WRONG);
        // 生成工单
        // workOrderService.fromAbnormalOrder(orderBase);
        // 执行退款
        refundApplyService.refund(orderBase);
        // 锁定设备
        deviceService.updateLockFlag(device.getSno(), null, true);
    }

    /**
     *
     * 场景三：下发后M分钟内没有收到设备回复，下发查询状态的指令N次，如果还是没有回复，这时候设备还是之前的状态、订单是异常状态，
     * 用户可申请退款，人工审核后退款。如果设备在订单有效期内重新上报正确的数据，且此时只有一张异常订单，则恢复该订单。
     * 连续出现这种情况N次后，锁定设备，生成工单，解决后人工解锁。
     * @param orderBase
     * @param device
     */
    private void handleDeviceStatusTimeOut(OrderBase orderBase, Device device){
        // 设置订单异常
        orderBaseService.handleAbnormalOrder(orderBase, OrderAbnormalReason.DEVICE_STATUS_TIMEOUT);
        // 锁定设备
        Integer lockTimes = Integer.valueOf(SysConfigUtils.get(CommonSystemConfig.class).getControlLockTimes());
        int currentAbnormalTimes = device.getAbnormalTimes() + 1;
        logger.info("====>>> 设备{}连续{}次下单后没有上报过数据！！！！！！", device.getMac(),currentAbnormalTimes);
        if(currentAbnormalTimes>=lockTimes) {
            // 生成工单
            logger.info("====>>> 设备{}达到锁定上限{}，即将锁定设备！！！！！！", device.getMac(),lockTimes);
            // workOrderService.fromAbnormalOrder(orderBase);
            deviceService.updateLockFlag(device.getSno(), currentAbnormalTimes,true);
        }else {
            deviceService.updateLockFlag(device.getSno(), currentAbnormalTimes, null);
        }

    }

    private void sleep(long time) {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
