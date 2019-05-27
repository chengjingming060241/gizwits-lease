package com.gizwits.lease.listener;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.event.BindGizwitsDeviceEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.util.GizwitsUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Created by GaGi on 2017/8/5.
 */
@Component
public class BindGizwitsDeviceListener implements ApplicationListener<BindGizwitsDeviceEvent> {
    @Autowired
    private ProductService productService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RedisService redisService;

    protected static Logger logger = LoggerFactory.getLogger(BindGizwitsDeviceListener.class);

    @Override
    public void onApplicationEvent(BindGizwitsDeviceEvent event) {
        //根据产品id绑定该产品对应的设备
        if (ParamUtil.isNullOrZero(event.getProductId()) || StringUtils.isEmpty(event.getToken()) || StringUtils.isEmpty(event.getSno())||StringUtils.isEmpty(event.getUsername())) {
            logger.error("=============>设备绑定失败");
            return;
        }
        Product product = productService.selectById(event.getProductId());
        if (Objects.isNull(product)) {
            LeaseException.throwSystemException(LeaseExceEnums.PRODUCT_DONT_EXISTS);
        }
        Device device = deviceService.selectById(event.getSno());
        String res = GizwitsUtil.bindDevice(device.getMac(), product, event.getToken());
        if (res.contains("90")){
            String resForToken = GizwitsUtil.createUser(event.getUsername(), product.getGizwitsAppId());
            JSONObject json = JSONObject.parseObject(resForToken);
            redisService.setUserTokenByUsername(event.getUsername(), String.valueOf(json.get("uid")),
                    String.valueOf(json.get("token")),
                    Long.valueOf(String.valueOf(json.get("expire_at"))));
            String token = String.valueOf(json.get("token"));
            res=GizwitsUtil.bindDevice(device.getMac(), product, token);
        }
        JSONObject json = JSONObject.parseObject(res);
        String passCode = String.valueOf(json.get("passcode"));
        String host = String.valueOf(json.get("host"));
        String wsPort = String.valueOf(json.get("ws_port"));
        String wssPort = String.valueOf(json.get("wss_port"));
        String gizDid = String.valueOf(json.get("did"));
        if (StringUtils.isNotEmpty(passCode) && StringUtils.isNotEmpty(host) && StringUtils.isNotEmpty(wsPort) && StringUtils.isNotEmpty(wssPort) && StringUtils.isNotEmpty(gizDid)) {
            device.setGizPassCode(passCode);
            device.setGizHost(host);
            device.setGizWsPort(wsPort);
            device.setGizWssPort(wssPort);
            device.setGizDid(gizDid);
            deviceService.updateById(device);
        }
    }
}
