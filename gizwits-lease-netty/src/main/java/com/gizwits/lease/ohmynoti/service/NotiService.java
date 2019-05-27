package com.gizwits.lease.ohmynoti.service;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.api.SmsApi;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.config.bean.ProductUpdateNettyConfigEvent;
import com.gizwits.lease.config.bean.SnotiDeviceControlDTO;
import com.gizwits.lease.config.bean.SnotiDeviceControlEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.ohmynoti.handler.PushEventRouter;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.noti.noticlient.OhMyNotiClient;
import com.gizwits.noti.noticlient.OhMyNotiClientImpl;
import com.gizwits.noti.noticlient.bean.req.NotiReqPushEvents;
import com.gizwits.noti.noticlient.bean.req.body.AuthorizationData;
import com.gizwits.noti.noticlient.config.SnotiCallback;
import com.gizwits.noti.noticlient.config.SnotiConfig;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * noti Services
 *
 * @author Jcxcc
 * @date 17-11-24
 * @email jcliu@gizwits.com
 * @since 1.0
 */
@Slf4j
@Service
public class NotiService implements InitializingBean, ApplicationListener<ApplicationEvent> {

    ExecutorService executor = Executors.newSingleThreadExecutor();

    private final OhMyNotiClient client = new OhMyNotiClientImpl();

    @Value("${gizwits.noti.enable:true}")
    private boolean notiEnable;

    @Autowired
    private ProductService productService;

    @Autowired
    private PushEventRouter pushEventRouter;

    private void pushEventProcess(JSONObject json) {
        pushEventRouter.dispatch(json);
    }

    /**
     * 获取所有noti启动信息
     *
     * @return
     */
    private AuthorizationData[] getLoginAuthorizes() {
        List<Product> orgData = productService.getAllUseableProduct();
        if (CollectionUtils.isEmpty(orgData)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        return
                orgData.stream()
                        .map(NotiService::convertLoginAuthorize)
                        .toArray(AuthorizationData[]::new);
    }

    private static AuthorizationData convertLoginAuthorize(Product product) {
        return
                new AuthorizationData()
                        .setSubkey(product.getSubkey())
                        .setAuth_id(product.getAuthId())
                        //不订阅app2dev事件
                        .addEvents(Stream.of(NotiReqPushEvents.values()).filter(event -> !StringUtils.contains(event.getCode(), "app2dev")).toArray(NotiReqPushEvents[]::new))
                        .setAuth_secret(product.getAuthSecret())
                        .setProduct_key(product.getGizwitsProductKey());
    }

    /**
     * 启动noti
     */
    private void startup() {
        client
                .addLoginAuthorizes(getLoginAuthorizes())
                .setCallback(new SnotiCallback() {
                    @Override
                    public void startup() {
                        log.debug("snoti客户端启动成功.");
                    }

                    @Override
                    public void loginSuccessful() {
                        log.debug("snoti登陆成功.");
                    }

                    @Override
                    public void loginFailed(String errorMessage) {
                        log.error("snoti客户端登陆失败. 错误信息[{}]", errorMessage);
                    }

                    @Override
                    public void noDataForAWhile(Long minutes, ChannelHandlerContext ctx) {
                        log.warn("snoti客户端[{}]分钟没有接收到数据. 即将重新连接客户端 ", minutes);
                        ctx.channel().close();
                        sendWarningMessage(minutes+"分钟没有接收到数据");
                    }

                    @Override
                    public void disconnected() {
                        //TODO 需要的话，可以使用短信或者邮件提醒
                        log.warn("snoti连接断开.");
//                        sendWarningMessage("连接断开");
                    }

                    @Override
                    public void reload(AuthorizationData... authorizationData) {
                        log.warn("snoti客户端重新加载登陆信息. 登陆信息[{}]", authorizationData);
                    }

                    @Override
                    public void stop() {
                        log.warn("snoti客户端终止.");
                    }
                });

        client.doStart();

        executor.execute(() -> {
            while (true) {
                pushEventProcess(client.receiveMessage());
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (notiEnable) {
            log.info("初始化noti客户端...");
            SnotiConfig snotiConfig = new SnotiConfig();
            snotiConfig.setEnableCheckNoData(true);
            snotiConfig.setNoDataWarningMinutes(1);
            client.setSnotiConfig(snotiConfig);
            log.info("启动noti客户端...");
            startup();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (!notiEnable) {
            log.info("snoti不处理事件{}===>snoti未开启", event);
            return;
        }

        if (event instanceof ProductUpdateNettyConfigEvent) {
            log.info("======产品{}参数修改,Netty需要重新登录===");
            client.reload(getLoginAuthorizes());
        } else if (event instanceof SnotiDeviceControlEvent) {
            SnotiDeviceControlDTO snotiDeviceControlDTO = ((SnotiDeviceControlEvent) event).getSnotiDeviceControlDTO();

            String
                    did = snotiDeviceControlDTO.getDid(),
                    mac = snotiDeviceControlDTO.getMac(),
                    productKey = snotiDeviceControlDTO.getProductKey();
            JSONObject attrs = snotiDeviceControlDTO.getAttrs();

            client.control(productKey, mac, did, attrs);
            log.info("控制设备productKey[{}] mac[{}] did[{}] attrs[{}]", productKey, mac, did, attrs);
        }
    }

    private void sendWarningMessage(String msg){
        try {
            log.info("开始发送短信：" + msg);
            String tplValue = SysConfigUtils.get(CommonSystemConfig.class).getMessageDeviceAlarmTemplate();
            String apiKey = SysConfigUtils.get(CommonSystemConfig.class).getMessageApiKey();
            String templageId = SysConfigUtils.get(CommonSystemConfig.class).getMessageDeviceAlarmTemplateId();
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
            String mobiles = SysConfigUtils.get(CommonSystemConfig.class).getManagerMobile();
            for (String mobile : mobiles.trim().split(",")) {
                if(StringUtils.isBlank(mobile) || mobile.length() != 11) continue;
                log.info("发送给：" + mobile);
                String resp = SmsApi.sendAlarmMessage("Snoti客户端", msg, date, mobile, templageId, tplValue, apiKey);
                log.info("发送结果：" + resp);
            }
        } catch (Exception e) {
            log.error("sendWarningMessage", e);
        }
    }
}