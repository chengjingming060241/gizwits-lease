package com.gizwits.lease.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.utils.*;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.app.utils.XmlResp;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.BindType;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.constant.UserStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.UserDevice;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.service.UserDeviceService;
import com.gizwits.lease.device.vo.DeviceAuth;
import com.gizwits.lease.event.DeviceWeiXinUnBindEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.model.JSTicket;
import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserWxExt;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.user.service.UserWeixinService;
import com.gizwits.lease.user.service.UserWxExtService;
import com.gizwits.lease.util.WxUtil;
import com.gizwits.lease.weixin.util.News;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Created by zhl on 2017/6/28.
 */
@Service
public class UserWeixinServiceImpl implements UserWeixinService {


    private final static Logger logger = Logger.getLogger("WEIXIN_LOGGER");

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;
    @Autowired
    private ProductServiceDetailService productServiceDetailService;
    @Autowired
    private UserWxExtService userWxExtService;

    /**
     * 验证微信token
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public boolean verifySignature(String signature, String timestamp, String nonce) {
        List<SysUserExt> list = sysUserExtService.selectList(new EntityWrapper<>());
        if (list == null || list.size() <= 0) {
            return false;
        }
        try {
            //由于微信验证的回调无法确定是哪个公众号,因此需要一个一个的循环验证,只要有一个验证通过就可以
            for (SysUserExt sysUserExt : list) {
                if (StringUtils.isNotBlank(sysUserExt.getWxToken())
                        && signature.equals(SHA1.gen(sysUserExt.getWxToken(), timestamp, nonce))) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("===> 微信验证Token失败");
            return false;
        }
        return false;
    }

    /**
     * 获取Code请求页面
     *
     * @param request
     * @param param
     */
    public void skipToGetCode(HttpServletRequest request, HttpServletResponse response, String param) {
        if (org.springframework.util.StringUtils.isEmpty(param)) {
            logger.error("===> 未传递相应参数进来进来");
            return;
        } else {

            //先判断传递进来的参数是wxId
            String m = LeaseUtil.judgeWxId(param);
            if (redisService.containWxConfig(m)) {
                WxUtil.getCodeRedirectUrl(request, response, m, param);
                return;
            }
            //传递过来的参数是deviceId
            Device device = deviceService.selectById(param);
            if (device != null) {
                SysUserExt sysUseExt = deviceService.getWxConfigByDeviceId(param);
                if (sysUseExt != null) {
                    WxUtil.getCodeRedirectUrl(request, response, sysUseExt.getWxId(), param);
                    return;
                }
            }
        }
    }


    @Override
    public String authorizeDevice(List<DeviceAuth> deviceAuths, SysUserExt sysUserExt) {
        JSONObject json = new JSONObject();
        json.put("device_num", String.valueOf(deviceAuths.size()));
        json.put("op_type", 1);
        json.put("device_list", deviceAuths);
        return WxUtil.getAuthorizeDeviceUrl(json.toString(), sysUserExt);
    }

    /**
     * 根据Code获取openid
     *
     * @param code
     * @param state
     * @return
     */
    public String getOpenid(String code, String state) {
        //获取相应的微信配置信息
        SysUserExt sysUseExt = null;
        if (redisService.containWxConfig(state)) {
            sysUseExt = JSONObject.parseObject(redisService.getWxConfig(state), SysUserExt.class);
        }
        //传递过来的参数是deviceId
        Device device = deviceService.selectById(state);
        if (device != null && sysUseExt == null) {
            sysUseExt = deviceService.getWxConfigByDeviceId(state);
        }
        if (sysUseExt == null) {
            return null;
        }
        return WxUtil.getOpenid(code, sysUseExt);
    }

    /**
     * 用户扫码进入微信页面,需要生成H5页面调动微信JSSDK的所需参数
     *
     * @param req
     * @param openid
     * @param param  有可能是wxId,或者deviceId
     * @return
     */
    public ResponseObject createJSSDKSignature(HttpServletRequest req, String openid, String param) {
        logger.info("====>>>>> openid="+openid);
        if (StringUtils.isBlank(openid)) {
            logger.error("====>>>>> 用户扫码未获取到openid");
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_OPENID_IS_NULL);
        }

        logger.debug("createJSSDKSignature param:" + param);

        //获取相应的微信配置信息
        SysUserExt sysUseExt = deviceService.getWxConfigByDeviceId(param);

        String wxId = LeaseUtil.judgeWxId(param);
        if (redisService.containWxConfig(wxId)) {
            sysUseExt = JSONObject.parseObject(redisService.getWxConfig(wxId), SysUserExt.class);
        }

        //传递过来的参数是deviceId
        Device device = deviceService.selectById(param);
        if (device != null && sysUseExt == null) {
            if (!deviceService.checkDeviceIsInOperator(param)) {
                LeaseException.throwSystemException(LeaseExceEnums.DEVICE_DONT_EXISTS);
            }
            sysUseExt = deviceService.getWxConfigByDeviceId(param);
        }
        if (sysUseExt == null) {
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_IN_OPERATOR);
        }

        User user;
        //获取并保存微信用户信息
        synchronized (openid.intern()) {
            user = userService.getUserByIdOrOpenidOrMobile(openid);
            if (user == null) {//数据库中还未存储用户信息
                String wxUserinfo = WxUtil.getUserInfo(openid, sysUseExt);
                logger.info("H5页面新增微信用户：" + wxUserinfo);
                synchronized (openid.intern()) {
                    user = userService.addUserByWx(wxUserinfo, sysUseExt, deviceService.getDeviceOperatorSysuserid(param));
                }
                if (user == null) {
                    logger.error("=====>>> 无法获取并保存用户信息");
                    LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_OPENID_IS_NULL);
                }
            }
        }

//        //检查用户是否在黑名单
//        if (UserStatus.BLACK.getCode().equals(user.getStatus())) {
//            LeaseException.throwSystemException(LeaseExceEnums.USER_IN_BLACK);
//        }

        //获取JSJSTicket用户JSSDK的签名
        JSTicket jsapiTicket = WxUtil.getJSAPITicket(sysUseExt);
        logger.debug("获取JSJSTicket用户JSSDK的签名:"+jsapiTicket.getTicket());
        if (jsapiTicket == null || StringUtils.isBlank(jsapiTicket.getTicket())) {
            logger.error("====>>>> 获取微信JSTicket出错");
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_GET_JSTICKET_ERROR);
        }

        StringBuffer reqUri = req.getRequestURL();
        if (StringUtils.isNotEmpty(req.getQueryString())) {
            reqUri = StringUtils.isNotEmpty(req.getQueryString()) ? reqUri.append("?").append(req.getQueryString()) : reqUri;
        }
        String url = SysConfigUtils.get(CommonSystemConfig.class).getHostWithContext() + "/";
        logger.debug("获取JSJSTicket url:"+url);

        long timestamp = System.currentTimeMillis() / 1000;
        String noncestr = RandomStringUtils.randomAlphanumeric(16);
        try {
            String signature = SHA1.jsGen("jsapi_ticket=" + jsapiTicket.getTicket(),
                    "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url);

            Map<String, Object> result = new HashedMap();
            result.put("signature", signature);
            result.put("timestamp", timestamp);
            result.put("noncestr", noncestr);
            result.put("url", reqUri.toString());
            result.put("appId", sysUseExt.getWxAppid());
            result.put("isBind", ParamUtil.isNullOrEmptyOrZero(user.getMobile()) ? 0 : 1);
            result.put("mobile", ParamUtil.isNullOrEmptyOrZero(user.getMobile()) ? "" : user.getMobile());
            result.put("status",user.getStatus());
            if (device != null) {
                result.put("deviceId", device.getSno());
            }
            logger.debug("return result");
            return ResponseObject.ok(result);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 微信公众号回调处理
     *
     * @param message
     * @return
     */
    public String handleCallbackMsg(String message) {

        Map<String, String> reqMap = WxUtil.parseXml(message);

        if (reqMap == null)
            return null;
        return switchMessageTypeHandle(reqMap);
    }

    private String switchMessageTypeHandle(Map<String, String> reqMap) {
        String msgType = reqMap.get("MsgType");
        switch (msgType) {
            case "text": // 文本消息
            case "image": //媒体消息
            case "video":
            case "voice":
            case "shortvideo":
                return "";
            case "event":
                return handleEventMsg(reqMap); // 基础事件推送
            case "device_event":
                return handleDeviceEventMsg(reqMap); // 设备事件推送
            default:
                return "";
        }
    }

    private String handleDeviceEventMsg(Map<String, String> reqMap) {
        //获取微信里面的wxDid，用于获取设备
        String wxDid = reqMap.get("DeviceID");
        Device device = deviceService.selectOne(new EntityWrapper<Device>().eq("wx_did", wxDid));
        String openId = reqMap.get("OpenID");
        String wxId = reqMap.get("ToUserName");
        logger.info("微信推送设备信息==>openId:" + openId + ",微信did==>" + wxDid + ", wxId="+wxId);

        if (Objects.isNull(device)) {
            logger.error("设备不能通过" + wxDid + "获取,请查看数据库中设备是否存在");
            return "";
        }

        String event = reqMap.get("Event");
        logger.info("微信推送设备信息==>event: " + event);
        //如果是绑定设备的操作
        if ("bind".equals(event)) {
            //判断user是否已经存在于数据库（用openId和sysUserId判断），不存在就添加到数据库
            User dbUser = userService.getUserByIdOrOpenidOrMobile(openId);
            if (dbUser == null) {
                String userInfo = WxUtil.getUserInfoBySubscribe(openId, wxId);
                SysUserExt sysUserExt = sysUserExtService.selectOne(new EntityWrapper<SysUserExt>().eq("wx_id", wxId));
                logger.info("绑定设备新增微信用户："+userInfo);
                synchronized (openId.intern()) {
                    dbUser = userService.addUserByWx(userInfo, sysUserExt, device.getOwnerId());
                }
            }

            //先判断用户是否已有绑定设备，如果有先解绑
            SysUserExt sysUserExt = deviceService.getWxConfigByDeviceId(device.getSno());
            UserDevice exitDevice = userDeviceService.selectOne(new EntityWrapper<UserDevice>().eq("openid", openId).eq("wechat_device_id", wxDid).eq("is_bind",1).eq("sno",device.getSno()));
            if (!ParamUtil.isNullOrEmptyOrZero(exitDevice)){
                CommonEventPublisherUtils.publishEvent(new DeviceWeiXinUnBindEvent("").build(openId, wxDid, sysUserExt, exitDevice));
            }
            //用户绑定设备记录表
            UserDevice userDevice = userDeviceService.selectOne(new EntityWrapper<UserDevice>().eq("openid", openId).eq("wechat_device_id", wxDid));
            userDevice = userDevice == null ? userDevice = new UserDevice(new Date()) : userDevice;
            userDevice.setUtime(new Date());
            userDevice.setWechatDeviceId(wxDid);
            userDevice.setSno(device.getSno());
            userDevice.setIsBind(BindType.BIND.getCode());
            userDevice.setMac(device.getMac());
            userDevice.setOpenid(dbUser.getOpenid());
            userDevice.setUserId(dbUser.getId());
            userDeviceService.insertOrUpdate(userDevice);
            //解绑操作
            CommonEventPublisherUtils.publishEvent(new DeviceWeiXinUnBindEvent("").build(openId, wxDid, sysUserExt, userDevice));
            //判断当前用户是否是该设备的使用者
            Boolean usingFlag = deviceService.checkDeviceIfUsedByOpenid(device, openId);
            //推送消息
            sendMessage(reqMap.get("FromUserName"), openId, device, sysUserExt, usingFlag);
        } else if ("unbind".equals(event)) {
            //记录用户解绑信息（userDevice）
        } else {//其他事件
            //不管订阅和不订阅的情况
            if ("subscribe_status".equals(event) || "unsubscribe_status".equals(event)) {
                return null;
            }
        }
        return null;
    }

    private void sendMessage(String fromUserName, String toUserOpenId, Device device, SysUserExt sysUserExt, Boolean flag) {
        News news = new News();
        StringBuilder sb = new StringBuilder();
        //根据wxDid获取device
        DeviceLaunchArea deviceLaunch = deviceLaunchAreaService.selectById(device.getLaunchAreaId());
        sb.append("您扫描的设备位于：").append("\n");
        if (Objects.isNull(deviceLaunch) || StringUtils.isEmpty(deviceLaunch.getAddress())) {
            sb.append("地址不详").append("\n");
        } else {
            sb.append(deviceLaunch.getProvince() + deviceLaunch.getCity() + deviceLaunch.getArea() + deviceLaunch.getAddress()).append("\n");
        }
        //如果被租用中,故障，异常，离线
        if (device.getWorkStatus().equals(DeviceStatus.USING.getCode()) && flag) {
            sb.append("设备租用状态:").append("\n");
            sb.append("设备正在被您使用").append("\n");
            sb.append("点击阅读全文进行设备租赁").append("\n");
            news.setUrl(SysConfigUtils.get(CommonSystemConfig.class).getWxAccessUrl() + device.getSno());
        } else if (device.getWorkStatus().equals(DeviceStatus.USING.getCode()) || device.getWorkStatus().equals(DeviceStatus.FAULT.getCode())
                || device.getWorkStatus().equals(DeviceStatus.STOP.getCode())) {
            sb.append("设备租用状态:").append("\n");
            sb.append(getDeviceTipMsgForWx(device.getWorkStatus()));
            news.setUrl(SysConfigUtils.get(CommonSystemConfig.class).getWxTipUrl());
        } else if (device.getOnlineStatus().equals(DeviceStatus.OFFLINE.getCode())) {
            sb.append("设备租用状态:").append("\n");
            sb.append(getDeviceTipMsgForWx(device.getOnlineStatus()));
            news.setUrl(SysConfigUtils.get(CommonSystemConfig.class).getWxTipUrl());
        } else {
            sb.append("设备租用价格:").append("\n");
            List<ProductServiceDetail> productServiceDetails = productServiceDetailService.selectList(new EntityWrapper<ProductServiceDetail>().eq("service_mode_id", device.getServiceId()));
            for (int i = 0; i < productServiceDetails.size(); ++i) {
                ProductServiceDetail p = productServiceDetails.get(i);
                if (i != productServiceDetails.size() - 1) {
                    sb.append(p.getNum() + p.getUnit() + p.getPrice() + "元").append(",");
                } else {
                    sb.append(p.getNum() + p.getUnit() + p.getPrice() + "元").append("\n");
                }
            }
            sb.append("点击阅读全文进行设备租赁").append("\n");
            //TODO:设备H5路径还没确定
            news.setUrl(SysConfigUtils.get(CommonSystemConfig.class).getWxAccessUrl() + device.getSno());
        }
        news.setPicUrl(SysConfigUtils.get(CommonSystemConfig.class).getWxNewsPicUrl());
        news.setDescription(sb.toString());
        news.setTitle(SysConfigUtils.get(CommonSystemConfig.class).getWxWelcomeMsg());
        WxUtil.customSendNews(fromUserName, news, sysUserExt);
    }

    String getDeviceTipMsgForWx(Integer status) {
        switch (status) {
            case 2:
                return "设备已离线，请选择其他设备";
            case 3:
                return "设备已被租赁，请选择其他设备";
            case 5:
                return "设备已被禁用，请选择其他设备";
            case 6:
                return "设备出现故障，请选择其他设备";
            default:
                return StringUtils.isEmpty(DeviceStatus.getName(status)) ? "设备出现问题，请选择其他设备" : "设备状态:" + DeviceStatus.getName(status);
        }
    }

    /**
     * 关注公众号消息推送:
     * <xml>
     * <ToUserName><![CDATA[gh_3c719aa39cad]]></ToUserName>
     * <FromUserName><![CDATA[oTGqGwit7iDZ0uNb_MX9D0QnvfaY]]></FromUserName>
     * <CreateTime>1499825357</CreateTime>
     * <MsgType><![CDATA[event]]></MsgType>
     * <Event><![CDATA[subscribe]]></Event>
     * <EventKey><![CDATA[]]></EventKey>
     * </xml>
     *
     * @param reqMap
     * @return
     */
    private String handleEventMsg(Map<String, String> reqMap) {
        String event = reqMap.get("Event");
        // 关注公众号,save user info
        if ("subscribe".equals(event)) {
            String wxId = reqMap.get("ToUserName");
            String openid = reqMap.get("FromUserName");
            if (redisService.containWxConfig(wxId)) {//WxId在缓存中存在
                String wxConfig = redisService.getWxConfig(wxId);
                if (StringUtils.isBlank(wxConfig)) {
                    return null;
                }
                SysUserExt sysUserExt = JSONObject.parseObject(redisService.getWxConfig(wxId), SysUserExt.class);
                User dbUser = userService.getUserByIdOrOpenidOrMobile(openid);
                if (Objects.isNull(dbUser)) {
                    String wxUserinfo = WxUtil.getUserInfoBySubscribe(openid, sysUserExt);
                    //添加用户通过wxUserinfo
                    logger.info("关注公众号新增微信用户："+wxUserinfo);
                    synchronized (openid.intern()) {
                        userService.addUserByWx(wxUserinfo, sysUserExt, sysUserExt.getSysUserId());
                    }
                    return XmlResp.buildText(openid, wxId, StringUtils.isBlank(sysUserExt.getWxSubscribeMsg()) ? SysConfigUtils.get(CommonSystemConfig.class).getWxSubscribeMsg() : sysUserExt.getWxSubscribeMsg());
                }

                // 回复欢迎语
                return XmlResp.buildText(openid, wxId, SysConfigUtils.get(CommonSystemConfig.class).getWxSubscribeMsg());
            }

            //取消关注,do nothing
            if ("unsubscribe".equals(event)) {
                //String openId = reqMap.get("FromUserName");
                return "";
            }

            // 菜单点击事件
            if ("CLICK".equals(event)) {
                return "";
            }
            return "";
        }
        return "";
    }
}
