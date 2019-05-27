package com.gizwits.lease.ohmynoti.handler.impl;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.constant.StatusCommandType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceExt;
import com.gizwits.lease.device.service.DeviceExtService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.ohmynoti.handler.PushEventHandler;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.service.OrderDataFlowService;
import com.gizwits.lease.order.service.OrderExtPortService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.util.DeviceUtil;
import com.gizwits.noti.noticlient.bean.resp.body.StatusKvEventBody;
import com.gizwits.noti.noticlient.util.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @author Jcxcc
 * @date 3/29/18
 * @email jcliu@gizwits.com
 * @since 1.0
 */
@Slf4j
@Component
public class DeviceStatusKvHandler implements PushEventHandler {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private OrderBaseService orderBaseService;
    @Autowired
    private DeviceExtService deviceExtService;
    @Autowired
    private OrderExtPortService orderExtPortService;
    @Autowired
    private OrderDataFlowService orderDataFlowService;

    static final String PORT = "a20QRcodePaymentupload_Outletnumber";

    @Override
    public void processPushEventMessage(JSONObject json) {
        StatusKvEventBody data = CommandUtils.parsePushEvent(json, StatusKvEventBody.class);
        String
                did = data.getDid(),
                mac = data.getMac(),
                productKey = data.getProductKey();
        Long createdAt = data.getCreatedAt();
        JSONObject kv = data.getData();
        String leaseType = kv.getString("a7Operationmode");
        log.info("设备数据点===>create_at:{} pk:{} did:{} mac:{} kv:{} ",  createdAt, productKey, did, mac, kv.toJSONString());

        // 得到设备出水口号
        Integer port = kv.getInteger(PORT);

        if (ParamUtil.isNullOrEmptyOrZero(port)) {
            log.error("====> 设备出水口在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]，port[" + port + "]");
        }

        // 缓存该设备上报数据点，便于在后台设备详情中获取上报信息
        redisService.cacheDevicePortCurrentStatus(productKey, mac, kv);
        JSONObject existDevicePort = redisService.getDevicePortCurrentStatus(productKey, mac + port);

        // 数据库设备在线状态
        deviceService.updateDeviceOnOffLineStatus(mac, productKey, did, true, leaseType);

        // Redis中还未缓存设备上报数据
        if (existDevicePort == null) {

            // 缓存上报数据点
            redisService.cacheDevicePortCurrentStatus(productKey, mac + port, kv);//缓存上报数据点，记录设备与出水口的对应关系
            redisService.cacheDeviceOnlineStatus(productKey, mac, true);//在线状态
            // redisService.cacheDeviceOnlineStatus(productKey, mac, true);//在线状态

            Device dbDevice = deviceService.getDeviceByMacAndPk(mac, productKey);

            if (dbDevice == null) {
                log.error("====> 设备在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]");
                return;
            }

            // 设备出水口
            DeviceExt deviceExt = deviceExtService.selectBySnoAndPort(dbDevice.getSno(), port);

            if (deviceExt == null) {
                log.error("====> 设备出水口在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]，port[" + port + "]");
                return;
            }

            // 根据上报数据库与产品配置的状态点,判断设备出水口状态
            changeDevicePortStatusByRealTimeData(productKey, deviceExt, existDevicePort);
        } else {
            // 比较Redis中缓存数据与上报数据,只有不相同的时候才做处理
            if (!equalRealTimeDataAndCacheData(productKey, mac, kv, existDevicePort)) {
                Device dbDevice = deviceService.getDeviceByMacAndPk(mac, productKey);

                if (dbDevice == null) {
                    log.error("====> 设备在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]");
                    return;
                }

                // 设备出水口
                DeviceExt deviceExt = deviceExtService.selectBySnoAndPort(dbDevice.getSno(), port);

                if (deviceExt == null) {
                    log.error("====> 设备出水口在系统中不存在,mac[" + mac + "],productKey[" + productKey + "]，port[" + port + "]");
                    return;
                }
                // 根据上报数据库与产品配置的状态点,判断设备出水口状态
                changeDevicePortStatusByRealTimeData(productKey, deviceExt, kv);
            }
        }

    }


    /**
     * 比较上报数据点和缓存的数据点是否有差异
     *
     * @param productKey
     * @param realTimeData
     * @param cacheData
     * @return
     */
    private boolean equalRealTimeDataAndCacheData(String productKey, String mac, JSONObject realTimeData, JSONObject cacheData) {
        if (!redisService.containProductMoint(productKey)) {
            log.info("=====Product: {}, Device:{} do not have monit datapoint in redis cache.====== ", productKey, mac);
            return false;
        }

        String monitPoints = redisService.getProductMonitPoint(productKey);
        if (StringUtils.isBlank(monitPoints)) {
            log.info("=====Product: {}, Device:{} monit datapoint is Blank.====== ", productKey, mac);
            return false;
        }
        String[] points = monitPoints.split(",");
        if (points == null || points.length <= 0) {
            log.info("=====Product: {}, Device:{} monit datapoint count is zero.====== ", productKey, mac);
            return false;
        }

        boolean resultFlag = true;
        // 判断是否是定长的数据点
        for (String point : points) {
            //有的设备可能是不定长上报,因此只检测实时上报中的数据点
            if (StringUtils.isNotBlank(point) && realTimeData.containsKey(point)) {
                if (cacheData.containsKey(point)) {
                    if (!realTimeData.get(point).equals(cacheData.get(point))) {//相同数据点的值不同
                        resultFlag = false;
                        break;
                    }
                } else {//要监控的数据点在缓存中不存在,需要缓存
                    resultFlag = false;
                    break;
                }
            }
        }

        //由于变长数据点的原因,需要将上报数据点循环覆盖缓存中的数据点
        Iterator it = realTimeData.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            cacheData.put(key, realTimeData.get(key));
        }

        //监控数据点有变动,将修改后的缓存数据再放到缓存中
        if (!resultFlag) {
            log.info("====Product: {}, mac: {} upload value is not equal last time, update new value into redis cache.======", productKey, mac);
        }
        // 因为需要通过上报时间来判断设备网络通信是否正常，从而执行退款逻辑，所以这里无论数据点是否有变化，都要重新缓存一遍，更新一下时间
        redisService.cacheDeviceCurrentStatus(productKey, mac, cacheData);//缓存上报数据点与缓存数据点的合并

        return resultFlag;
    }


    /**
     * 根据上报的数据点与产品中配置的状态指令进行对比,判断设备的状态
     *
     * @param productKey
     * @param device
     * @param jsonData
     * @return
     */
    /**
     * 根据上报的数据点与产品中配置的状态指令进行对比,判断设备出水口的状态
     *
     * @param productKey
     * @param deviceExt
     * @param jsonData
     * @return
     */
    private boolean changeDevicePortStatusByRealTimeData(String productKey, DeviceExt deviceExt, JSONObject jsonData) {
        String sno = deviceExt.getSno();
        Integer port = deviceExt.getPort();
        Device device = deviceService.getDeviceInfoBySno(sno);
        //使用完成,要修改订单状态和设备出水口状态
        if (redisService.containsProductStatusCommand(productKey, StatusCommandType.FINISH.getCode())) {
            JSONObject commandJson = redisService.getProductStatusCommand(productKey, StatusCommandType.FINISH.getCode());
            if (DeviceUtil.equalCommandAndRealTimeData(commandJson, jsonData)) {
                log.info("====Product:{} ,Device sno:{},port:{} upload data is equals FINISH command ,start finish order and update device status.===", productKey, sno, port);
                OrderBase orderBase = orderExtPortService.getOrderBaseBySnoAndPort(sno, port, OrderStatus.USING.getCode());
                if (orderBase != null) {
                    log.info("====Start Finish Order:{}======", orderBase.getOrderNo());
                    orderDataFlowService.saveFinishData(orderBase, jsonData.toJSONString());
                    //结束订单改变状态和推送消息
                    orderBaseService.closeOrder(orderBase.getOrderNo());
                } else {
                    log.info("=====Device sno: {},port:{} do not have USING order===", sno, port);
                }

                deviceExt.setStatus(DeviceStatus.FREE.getCode());
                deviceExt.setUtime(new Date());
                deviceExtService.updateById(deviceExt);
                //如果设备所有出水口都为空闲则设备为空闲
                int num = deviceExtService.countDeviceByStatus(sno, DeviceStatus.FREE.getCode());
                log.info("====Product:{} ,Device sno:{},FREE status num:{} ", productKey, sno, num);
                device.setUtime(new Date());
                if (num == 6) {
                    device.setWorkStatus(DeviceStatus.FREE.getCode());

                } else {
                    device.setWorkStatus(DeviceStatus.USING.getCode());
                }
                deviceService.updateById(device);

                return true;
            }
        }

        //空闲状态
        if (redisService.containsProductStatusCommand(productKey, StatusCommandType.FREE.getCode())) {
            JSONObject commandJson = redisService.getProductStatusCommand(productKey, StatusCommandType.FREE.getCode());
            if (DeviceUtil.equalCommandAndRealTimeData(commandJson, jsonData)) {
                log.info("====Product:{} ,Device:{} ,port:{} upload data is equals FREE command .===", productKey, sno, port);
                deviceExt.setStatus(DeviceStatus.FREE.getCode());
                deviceExt.setUtime(new Date());
                deviceExtService.updateById(deviceExt);
                //如果设备所有出水口都为空闲则设备为空闲
                int num = deviceExtService.countDeviceByStatus(sno, DeviceStatus.FREE.getCode());
                log.info("====Product:{} ,Device sno:{},FREE status num:{} ", productKey, sno, num);
                device.setUtime(new Date());
                if (num == 6) {
                    device.setWorkStatus(DeviceStatus.FREE.getCode());

                } else {
                    device.setWorkStatus(DeviceStatus.USING.getCode());
                }
                deviceService.updateById(device);
                return true;
            }
        }

        //使用中
        if (redisService.containsProductStatusCommand(productKey, StatusCommandType.USING.getCode())) {
            JSONObject commandJson = redisService.getProductStatusCommand(productKey, StatusCommandType.USING.getCode());
            if (DeviceUtil.equalCommandAndRealTimeData(commandJson, jsonData)) {
                log.info("====Product:{} ,Device sno:{} port:{} upload data is equals USING command .===", productKey, sno, port);
                deviceExt.setStatus(DeviceStatus.USING.getCode());
                deviceExt.setUtime(new Date());
                deviceExtService.updateById(deviceExt);
                //如果设备所有出水口都为空闲则设备为空闲
                int num = deviceExtService.countDeviceByStatus(sno, DeviceStatus.FREE.getCode());
                log.info("====Product:{} ,Device sno:{},FREE status num:{} ", productKey, sno, num);
                device.setUtime(new Date());
                if (num == 6) {
                    device.setWorkStatus(DeviceStatus.FREE.getCode());

                } else {
                    device.setWorkStatus(DeviceStatus.USING.getCode());
                }
                deviceService.updateById(device);
                return true;
            }
        }

        return false;
    }



  /*  private void resolveSendMessage(Device device, JSONObject jsonObject) {
        MessageTemplate messageTemplate = null;
        List<String> triggers = messageTemplateService.getTriggerByProduct(device.getProductId());
        for (String command : triggers) {
            JSONObject commandJson = JSONObject.parseObject(command);
            messageTemplate = messageTemplateService.selectOne(new EntityWrapper<MessageTemplate>().eq("command", command).eq("product_id", device.getProductId()).eq("is_deleted", 0));
            //判断是否已经推送
            if (ParamUtil.isNullOrEmptyOrZero(messageTemplate)){
                continue;
            }
            SysMessage
                    sysMessage = sysMessageService.selectOne(new EntityWrapper<SysMessage>().eq("title", messageTemplate.getTitle()).eq("content", messageTemplate.getContent()).eq("mac", device.getMac()).eq("is_deleted", 0));
            boolean flag = false;
            if (DeviceUtil.equalCommandAndRealTimeData(commandJson, jsonObject)) {
                if (ParamUtil.isNullOrEmptyOrZero(sysMessage)) {
                    flag = true;
                } else if (sysMessage.getIsFixed().compareTo(DeleteStatus.DELETED.getCode()) == 0) {
                    flag = true;
                }
                if (flag) {
                    //插入系统消息
                    messageTemplateService.sendSysMessage(device, messageTemplate);
                }
            }else {
                if (!ParamUtil.isNullOrEmptyOrZero(sysMessage)){
                    sysMessage.setUtime(new Date());
                    sysMessage.setIsFixed(1);
                    sysMessageService.updateById(sysMessage);
                }
            }
        }
    }
*/

    /**
     * 将数据点的卡号转换成数据库里面保存的卡号
     * 上报卡号数据点中前10位为卡号,因此需要截取
     */
    private String getCardNumFromDeviceData(String originCardNum) {
        if (StringUtils.isEmpty(originCardNum)) {
            log.error("卡号为空");
        }
        if (originCardNum.length() < 10) {
            log.error("卡号");
        }
        String cardNum = originCardNum.substring(0, 10);
        return cardNum.toUpperCase();
    }
}