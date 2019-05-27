package com.gizwits.lease.app.web;

import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.constant.UserStatus;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.enums.MoveType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.user.service.UserWeixinService;
import com.gizwits.lease.util.WxUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhl on 2017/6/28.
 */
@EnableSwagger2
@Api(value = "微信回调,微信扫码")
@RestController
@RequestMapping("/app/wx")
public class WeixinController {

    @Autowired
    private UserWeixinService userWeixinService;
    @Autowired
    private DeviceService deviceService;

    /**
     * 校验微信消息合法性,微信公众号服务配置的时候会回调
     *
     * @param signature 微信加密签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串，需要原样返回
     */

    @ApiOperation(value = "校验微信消息合法", notes = "微信消息校验Note", response = Void.class)
    @RequestMapping(value = "weixin", method = RequestMethod.GET)
    public void weixinGet(
            @RequestParam(value = "signature", required = false) @ApiParam(value = "签名", required = false) String signature,
            @RequestParam(value = "timestamp", required = false) @ApiParam(value = "时间戳", required = false) String timestamp,
            @RequestParam(value = "nonce", required = false) @ApiParam(value = "随机数", required = false) String nonce,
            @RequestParam(value = "echostr", required = false) @ApiParam(value = "随机字符串,需要原样返回", required = false) String echostr, HttpServletResponse resp) {

        if (userWeixinService.verifySignature(signature, timestamp, nonce)) {
            WxUtil.out(echostr, resp);
        }
    }

    /**
     * 微信公众号操作的回调处理方法
     *
     * @param requestBody
     * @param resp
     */
    @ApiOperation(value = "微信公众号回调信息", notes = "处理微信回调", response = Void.class)
    @RequestMapping(value = "weixin", method = RequestMethod.POST, produces = "text/xml;charset=UTF-8")
    public void weixinPost(@RequestBody String requestBody, HttpServletResponse resp) {
        try {
            String xmlStr = userWeixinService.handleCallbackMsg(requestBody);
            xmlStr = xmlStr == null ? "" : xmlStr;
            WxUtil.out(xmlStr, resp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "扫描二维码打开页面和点击链接的页面,需要将DeviceId传递过来")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void wxPage(@RequestParam(value = "code", required = false) String code,
                       @RequestParam(value = "openid", required = false) String openid,
                       @RequestParam(value = "state", required = false) String state,
                       @RequestParam(value = "deviceId", required = false) String deviceId,
                       @RequestParam(value = "wxId", required = false) String wxId,
                       HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (StringUtils.isEmpty(openid)) {
            if (StringUtils.isEmpty(code)) {
                deviceId = StringUtils.isNotBlank(wxId) ? wxId : deviceId;
                userWeixinService.skipToGetCode(request, response, deviceId);
                return;
            } else {
                openid = userWeixinService.getOpenid(code, LeaseUtil.judgeWxId(state));
            }
        }
        if (StringUtils.isBlank(openid)) {
            return;
        }
        ResponseObject jssdkSignatureRsp = userWeixinService.createJSSDKSignature(request, openid, LeaseUtil.judgeWxId(state));//state参数为wxId或者deviceId
        if (jssdkSignatureRsp.getCode().equals("200")) {
            Map<String, Object> wxJsapiSignature = (Map<String, Object>) jssdkSignatureRsp.getData();
            Cookie cookie00 = new Cookie("isBind", wxJsapiSignature.get("isBind").toString());
            cookie00.setPath("/");
            response.addCookie(cookie00);
            Cookie mobileCookie = new Cookie("mobile", "");
            mobileCookie.setMaxAge(0);
            mobileCookie.setPath("/");
            response.addCookie(mobileCookie);
            if (wxJsapiSignature.containsKey("mobile") && !ParamUtil.isNullOrEmptyOrZero(wxJsapiSignature.get("mobile"))) {
                Cookie cookie2 = new Cookie("mobile", wxJsapiSignature.get("mobile").toString());
                cookie2.setPath("/");
                response.addCookie(cookie2);
            }

            Cookie userStatusCookie = new Cookie("isBlack", wxJsapiSignature.get("status").toString());
            userStatusCookie.setPath("/");
            response.addCookie(userStatusCookie);

            if (LeaseExceEnums.DEVICE_NOT_IN_OPERATOR.getCode().equals(jssdkSignatureRsp.getCode())) {
                Cookie cookie1 = new Cookie("isOperator", "0");
                cookie1.setPath("/");
                response.addCookie(cookie1);

                response.sendRedirect("/");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }else if(LeaseExceEnums.USER_IN_BLACK.getCode().equals(jssdkSignatureRsp.getCode())){
                Cookie blackCookie = new Cookie("isBlack", UserStatus.BLACK.getCode().toString());
                blackCookie.setPath("/");
                response.addCookie(blackCookie);

                response.sendRedirect("/");
                response.getWriter().flush();
                response.getWriter().close();
                return;
            }

        } else {
            Cookie cookie1 = new Cookie("errorMsg", jssdkSignatureRsp.getMessage());
            cookie1.setPath("/");
            response.addCookie(cookie1);

            response.sendRedirect("/");
            response.getWriter().flush();
            response.getWriter().close();
            return;
        }
        Map<String, Object> wxJsapiSignature = (Map<String, Object>) jssdkSignatureRsp.getData();
        //截取路径后面的跳转路径放进cookie
        Cookie cookiePageType = new Cookie("pageType", LeaseUtil.judgePageType(state));
        cookiePageType.setPath("/");
        response.addCookie(cookiePageType);

//        Cookie blackCookie = new Cookie("isBlack", "1");//覆盖黑名单
//        blackCookie.setPath("/");
//        response.addCookie(blackCookie);
        Cookie cookie0 = new Cookie("isOperator", "1");//是否投入运营
        cookie0.setPath("/");
        response.addCookie(cookie0);
        Cookie cookie00 = new Cookie("errorMsg", "");//清空cookie中的错误信息
        cookie00.setPath("/");
        response.addCookie(cookie00);
        Cookie cookie1 = new Cookie("timestamp", wxJsapiSignature.get("timestamp").toString());
        cookie1.setPath("/");
        response.addCookie(cookie1);
        Cookie cookie2 = new Cookie("nonceStr", wxJsapiSignature.get("noncestr").toString());
        cookie2.setPath("/");
        response.addCookie(cookie2);
        Cookie cookie3 = new Cookie("openid", openid);
        cookie3.setPath("/");
        response.addCookie(cookie3);
        Cookie cookie6 = new Cookie("signature", wxJsapiSignature.get("signature").toString());
        cookie6.setPath("/");
        response.addCookie(cookie6);
        Cookie cookie7 = new Cookie("appId", wxJsapiSignature.get("appId").toString());
        cookie7.setPath("/");
        response.addCookie(cookie7);
        String sno = null;
        if (!Objects.isNull(wxJsapiSignature.get("deviceId"))) {
            sno = wxJsapiSignature.get("deviceId").toString();
        } else {
            sno = deviceService.getSnoByOpenid(openid);
        }

        Cookie cookie8 = new Cookie("sno", sno);
        cookie8.setPath("/");
        if (StringUtils.isEmpty(sno)) {
            cookie8.setMaxAge(0);
        }
        response.addCookie(cookie8);
        response.sendRedirect("/");
        response.getWriter().flush();
        response.getWriter().close();

    }

}


