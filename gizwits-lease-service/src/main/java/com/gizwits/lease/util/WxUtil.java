package com.gizwits.lease.util;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.HttpUtil;
import com.gizwits.boot.utils.SignUtils;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.app.utils.ContextUtil;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.enums.ShareBenefitSheetStatusType;
import com.gizwits.lease.event.ShareBenefitPayRecordEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.model.AccessToken;
import com.gizwits.lease.model.JSTicket;
import com.gizwits.lease.model.OauthCode;
import com.gizwits.lease.model.WxMpPayCallback;
import com.gizwits.lease.model.WxMpPrepayIdResult;
import com.gizwits.lease.redis.RedisService;
import com.gizwits.lease.weixin.util.News;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhl on 2017/2/8.
 */
@Component
public class WxUtil {

    private static RedisService redisService;

    @Autowired(required = true)
    public void setRedisService(RedisService redisService) {
        WxUtil.redisService = redisService;
    }

    private static String CERT_PATH = "/mnt/data/mahjong_cert/apiclient_cert.p12";

    private static String GetAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    private static final String AccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APP_ID&secret=APP_SECRET";
    private static final String CustomSendUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";

    private static String SendTemplateUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    private static final String CreateMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
    private static final String QueryMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
    private static final String DeleteMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

    private static final String GetMediaUrl = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    private static final String UserInfoUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    private static final String UserInfoUrl_NEW = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
    private static final String UserInfoUrl_SUBSTRACT = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    private static final String JSAPIUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static final String OAUTH2URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=$APPID&redirect_uri=$REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=$STATE#wechat_redirect";
    private static final String AUTHORURL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&grant_type=authorization_code&code=";

    private static final String REFUND_JSAPI_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    private static String WXTransferURL = "https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers";

    private static String USERINFO_ACCESSTOKEN = "";
    /**
     * 微信设备
     */
    //二维码
    private static final String QRCODE_URL = "https://api.weixin.qq.com/device/getqrcode?access_token=ACCESS_TOKEN&product_id=PRODUCT_ID";
    //微信设备绑定
    private static final String AuthorizeUrl = "https://api.weixin.qq.com/device/authorize_device?access_token=ACCESS_TOKEN";
    //微信设备解绑
    private static final String UNBIND_DEV_URL = "https://api.weixin.qq.com/device/compel_unbind?access_token=ACCESS_TOKEN";
    //根据openId获取设备did
    private static final String GET_OPENID_DID_URL = "https://api.weixin.qq.com/device/get_bind_device?access_token=ACCESS_TOKEN&openid=OPENID";

    private static String SendWithManyOpenIdUrl = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token=ACCESS_TOKEN";


    /**
     * 获取访问凭证
     * <p>
     * 正常情况下access_token有效期为7200秒，重复获取将导致上次获取的access_token失效。
     * 由于获取access_token的api调用次数非常有限，需要全局存储与更新access_token <br/>
     * 文档位置：基础支持->获取access token
     */
    public static AccessToken getAccessToken() {
        String resultContent = HttpUtil.executeGet(GetAccessTokenUrl.replace("APPID",
                SysConfigUtils.get(CommonSystemConfig.class).getWxAppId()).replace("APPSECRET",
                SysConfigUtils.get(CommonSystemConfig.class).getWxAppSecret()));
        return AccessToken.fromJson(resultContent);
    }

    /**
     * 根据code获取用户openId
     *
     * @param code
     * @return
     */
    public static String getOpenid(String code, SysUserExt sysUserExt) {
        OauthCode oauth2Bean = oauthCode(code, sysUserExt);
        String openid = oauth2Bean.getOpenid();//获取相应的生产环境openid
        if (StringUtils.isNotBlank(oauth2Bean.getAccessToken())){
            redisService.cacheWxUserInfoAccessToken(openid,oauth2Bean.getAccessToken());
        }
        return openid;
    }

    /**
     * 获取JSAPITicket
     */
    public static JSTicket getJSAPITicket() {
        String realUrl = JSAPIUrl.replace("ACCESS_TOKEN", getAccessToken().getAccess_token());
        String rs = HttpUtil.executeGet(realUrl);
        return JSTicket.fromJson(rs);
    }

    public static JSTicket getJSAPITicket(String wxId) {
        if (StringUtils.isBlank(wxId)) {
            return null;
        }
        String wxConfig = redisService.getWxConfig(wxId);
        if (StringUtils.isBlank(wxConfig)) {
            return null;
        }
        SysUserExt sysUserExt = JSONObject.parseObject(redisService.getWxConfig(wxId), SysUserExt.class);
        return getJSAPITicket(sysUserExt);
    }

    public static JSTicket getJSAPITicket(SysUserExt sysUserExt) {
        if (redisService.containJSAPITicket(sysUserExt.getWxId())) {
            String ticketObject = redisService.getJSAPITicket(sysUserExt.getWxId());
            return JSONObject.parseObject(ticketObject, JSTicket.class);
        }
        String realUrl = JSAPIUrl.replace("ACCESS_TOKEN", getAccessToken(sysUserExt));
        String rs = HttpUtil.executeGet(realUrl);
        JSTicket jsTicket = JSTicket.fromJson(rs);
        if (jsTicket == null) {
            return null;
        }
        redisService.cacheJSAPITicket(sysUserExt.getWxId(), rs, jsTicket.getExpires_in() - 1000L);
        return jsTicket;
    }


    /**
     * 获取微信用户信息
     *
     * @param openid
     * @param wxId
     * @return
     */
    public static String getUserInfo(String openid, String wxId) {
        if (StringUtils.isBlank(wxId) || StringUtils.isBlank(openid)) {
            return null;
        }
        String wxConfig = redisService.getWxConfig(wxId);
        if (StringUtils.isBlank(wxConfig)) {
            return null;
        }
        SysUserExt sysUserExt = JSONObject.parseObject(redisService.getWxConfig(wxId), SysUserExt.class);
        return getUserInfo(openid, sysUserExt);
    }

    public static String getUserInfoBySubscribe(String openid, String wxId) {
        if (StringUtils.isBlank(wxId) || StringUtils.isBlank(openid)) {
            return null;
        }
        String wxConfig = redisService.getWxConfig(wxId);
        if (StringUtils.isBlank(wxConfig)) {
            return null;
        }
        SysUserExt sysUserExt = JSONObject.parseObject(redisService.getWxConfig(wxId), SysUserExt.class);
        return getUserInfoBySubscribe(openid, sysUserExt);
    }

    public static String getUserInfo(String openid, SysUserExt sysUserExt) {
        String url = UserInfoUrl_NEW.replace("OPENID", openid).replace("ACCESS_TOKEN", redisService.getWxUserInfoAccessToken(openid));
        System.out.println("====getUserInfo URL===:"+url);
        return HttpUtil.executeGet(url);
    }

    public static String getUserInfoBySubscribe(String openid, SysUserExt sysUserExt) {
        String url = UserInfoUrl_SUBSTRACT.replace("OPENID", openid).replace("ACCESS_TOKEN", getAccessToken(sysUserExt));
        return HttpUtil.executeGet(url);
    }


    /**
     * 根据传递的参数获取AccessToken
     *
     * @param sysUserExt
     * @return
     */
    public static String getAccessToken(SysUserExt sysUserExt) {
        if (sysUserExt != null) {
            if (StringUtils.isEmpty(sysUserExt.getWxAppid()) || StringUtils.isEmpty(sysUserExt.getWxAppSecret())) {
                return null;
            }
            if (Objects.nonNull(redisService.getAccessToken(sysUserExt.getWxId()))){
                return redisService.getAccessToken(sysUserExt.getWxId());
            }
            String resultContent = HttpUtil.executeGet(AccessTokenUrl.replace("APP_ID", sysUserExt.getWxAppid()).replace("APP_SECRET", sysUserExt.getWxAppSecret()));
            AccessToken accessToken = AccessToken.fromJson(resultContent);
            if (accessToken == null) {//获取失败再次尝试获取
                resultContent = HttpUtil.executeGet(AccessTokenUrl.replace("APP_ID", sysUserExt.getWxAppid()).replace("APP_SECRET", sysUserExt.getWxAppSecret()));
                accessToken = AccessToken.fromJson(resultContent);
                if (accessToken == null) {
                    return null;
                } else {
                    //缓存AccessToken,过期时间减去10分钟,
                    redisService.cacheAccessToken(sysUserExt.getWxId(), accessToken.getAccess_token(), accessToken.getExpires_in() / 2);
                }
            } else {
                //缓存AccessToken,过期时间减去10分钟,
                redisService.cacheAccessToken(sysUserExt.getWxId(), accessToken.getAccess_token(), accessToken.getExpires_in() / 2);
            }
            return accessToken.getAccess_token();
        }
        return null;
    }

    /**
     * response返回字符串,contentType: text/xml
     *
     * @param str
     * @param response
     */
    public static void out(String str, HttpServletResponse response) {
        Writer out = null;
        try {
            response.setContentType("text/xml;charset=UTF-8");
            out = response.getWriter();
            out.append(str);
            out.flush();
        } catch (IOException e) {
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }


    /**
     * 生成32位以内的下单ID = orderId(19位) + 时间戳(秒级别11位)
     *
     * @param orderId
     * @return
     */
    public static String generateMchId(String orderId) {
        if (orderId.length() > 19) {
            orderId = orderId.substring(0, 19);
        }
        return orderId + System.currentTimeMillis() / 1000;
    }

    /**
     * 返回Weixin响应
     *
     * @param response
     * @param code
     * @param msg
     * @throws Exception
     */
    public static void responseToWx(HttpServletResponse response, String code, String msg) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        sb.append("<return_code><![CDATA[" + code + "]]></return_code>");
        sb.append("<return_msg><![CDATA[" + msg + "]]></return_msg>");
        sb.append("</xml>");
        String repStr = sb.toString();
        PrintWriter out = response.getWriter();
        out.println(repStr);
        out.flush();
        out.close();
    }

    /**
     * 解析XML,仅限一级Element
     *
     * @param body
     * @return
     * @throws IOException
     */
    public static Map<String, String> parseXml(String body) {
        body = body.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        Map<String, String> map = new HashMap<String, String>();
        try {
            InputStream in = new ByteArrayInputStream(body.getBytes("UTF-8"));
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return map;
    }

    public static void getCodeRedirectUrl(HttpServletRequest req, HttpServletResponse response, String wxId, String state) {
        if (StringUtils.isBlank(wxId))
            return;
        String wxConfigJsonString = redisService.getWxConfig(wxId);
        if (StringUtils.isNotBlank(wxConfigJsonString)) {
            SysUserExt sysUserExt = JSONObject.parseObject(wxConfigJsonString, SysUserExt.class);
            String oauthPath = WxUtil.OAUTH2URL.replace("$APPID", sysUserExt.getWxAppid())
                    .replace("$REDIRECT_URI", ContextUtil.getHostWithContextPathAndUri(req))
                    .replace("$STATE", state == null ? "gizwits_rent" : state);
            try {
                response.sendRedirect(oauthPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取OauthCode网页授权
     *
     * @param code
     * @param sysUserExt
     * @return
     */
    public static OauthCode oauthCode(String code, SysUserExt sysUserExt) {
        String realUrl = AUTHORURL.replace("APPID", sysUserExt.getWxAppid()).replace("SECRET", sysUserExt.getWxAppSecret()) + code;
        String rs = HttpUtil.executeGet(realUrl);
        return OauthCode.fromJson(rs);
    }

    /**
     * 获取请求IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }


    /**
     * 将微信支付回调的XML转换为对象
     *
     * @param xmlData
     * @return
     */
    public static WxMpPayCallback getJSSDKCallbackData(String xmlData) {
        try {
            //将从API返回的XML数据映射到Java对象 getObjectFromXML(String xml, Class tClass)
            XStream xStreamForResponseData = new XStream();
            XStream.setupDefaultSecurity(xStreamForResponseData);
            xStreamForResponseData.allowTypes(new Class[]{WxUtil.class,WxMpPayCallback.class,WxMpPrepayIdResult.class});
            xStreamForResponseData.ignoreUnknownElements();//暂时忽略掉一些新增的字段
            xStreamForResponseData.alias("xml", WxMpPrepayIdResult.class);
            xStreamForResponseData.alias("xml", WxMpPayCallback.class);
            WxMpPayCallback wxMpCallback = (WxMpPayCallback) xStreamForResponseData.fromXML(xmlData);
            return wxMpCallback;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new WxMpPayCallback();
    }

    /**
     * 解析xml为Map
     *
     * @param body
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public static Map<String, String> parseXmlToMap(String body) {
        try {
            body = body.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
            InputStream in = new ByteArrayInputStream(body.getBytes("UTF-8"));
            Map<String, String> map = new HashMap();
            SAXReader reader = new SAXReader();
            Document document = reader.read(in);
            Element root = document.getRootElement();
            List<Element> elementList = root.elements();
            for (Element e : elementList) {
                map.put(e.getName(), e.getText());
            }
            return map;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * 发送模板消息
     *
     * @param body
     * @param sysUserExt
     */
    public static void sendTemplateNotice(String body, SysUserExt sysUserExt) {
        HttpUtil.executePost(SendTemplateUrl.replace("ACCESS_TOKEN", getAccessToken(sysUserExt)), body);
    }

    /**
     * 群发文本信息
     *
     * @param body
     * @param sysUserExt
     */
    public static void sendManyNotices(String body, SysUserExt sysUserExt) {
        HttpUtil.executePost(SendWithManyOpenIdUrl.replace("ACCESS_TOKEN", getAccessToken(sysUserExt)), body);
    }

    /**
     * 根据sysUserExt和product获取微信绑定设备的ticket
     *
     * @param accessToken
     * @param wxProductId
     * @return
     */
    public static String getQrcodeTicket(String accessToken, String wxProductId) {
        if (StringUtils.isEmpty(accessToken)) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_GET_TOKEN_ERROR);
        }
        if (StringUtils.isEmpty(wxProductId)) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_PRODUCT_ID);
        }
        //替换url中的参数
        String readUrl = QRCODE_URL.replace("ACCESS_TOKEN", accessToken).replace("PRODUCT_ID", wxProductId);
        //请求返回结果集
        String rs = HttpUtil.executeGet(readUrl);
        return rs;
    }

    public static String customSendNews(String fromUserName, News news, SysUserExt sysUserExt) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        sb.append(buildNews(news));
        sb.append("]");
        String body = "{\"touser\":\"" + fromUserName + "\",\"msgtype\":\"news\""
                + ",\"news\": {\"articles\":" + sb.toString() + "}}";
        return customSend(body, sysUserExt);
    }

    /**
     * 分润
     *
     * @param shareBenefitSheet
     * @param ip
     * @return
     * @throws IOException
     */
    public static String postShareBenefitOrder(ShareBenefitSheet shareBenefitSheet, String ip, SysUserExt sysUserExt, Integer actionUserId) throws Exception {
        Integer totalFee = new BigDecimal(shareBenefitSheet.getShareMoney()).multiply(new BigDecimal(100)).intValue();
        String nonce_str = System.currentTimeMillis() + "";

        SortedMap<String, String> packageParams = new TreeMap<>();
        packageParams.put("mch_appid", sysUserExt.getWxAppid());
        packageParams.put("mchid", sysUserExt.getWxParenterId());
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("partner_trade_no", shareBenefitSheet.getTradeNo());
        packageParams.put("openid", shareBenefitSheet.getReceiverOpenid());
        packageParams.put("re_user_name", shareBenefitSheet.getReceiverName());
        packageParams.put("check_name", "FORCE_CHECK");
        packageParams.put("amount", totalFee.toString());
        packageParams.put("desc", "分润");
        packageParams.put("spbill_create_ip", ip);

        String sign = SignUtils.createSign(packageParams, sysUserExt.getWxPartnerSecret());


        //将要提交给API的数据对象转换成XML格式数据Post给API
        String postDataXML = "<xml>" +
                "<mch_appid>" + sysUserExt.getWxAppid() + "</mch_appid>" +
                "<mchid>" + sysUserExt.getWxParenterId() + "</mchid>" +
                "<nonce_str>" + nonce_str + "</nonce_str>" +
                "<partner_trade_no>" + shareBenefitSheet.getTradeNo() + "</partner_trade_no>" +
                "<openid>" + shareBenefitSheet.getReceiverOpenid() + "</openid>" +
                "<check_name>FORCE_CHECK</check_name>" +
                "<re_user_name>" + shareBenefitSheet.getReceiverName() + "</re_user_name>" +
                "<amount>" + totalFee.toString() + "</amount>" +
                "<desc>" + "分润" + "</desc>" +
                "<spbill_create_ip>" + ip + "</spbill_create_ip>" +
                "<sign>" + sign + "</sign>" +
                "</xml>";
        //分润记录
        CommonEventPublisherUtils.publishEvent(new ShareBenefitPayRecordEvent("ExecutingShare", shareBenefitSheet, postDataXML, actionUserId));
        String resultStr = HttpUtil.httpPostWithCert(WXTransferURL, postDataXML, SysConfigUtils.get(CommonSystemConfig.class).getWxCertDirectoryPath().trim(), sysUserExt.getWxParenterId());
        CommonEventPublisherUtils.publishEvent(new ShareBenefitPayRecordEvent("ExecutedShare", shareBenefitSheet, resultStr, actionUserId));

        return resultStr;
    }

    public static int checkShareBenefitResult(String resultStr) {
        if (StringUtils.isBlank(resultStr)) {
            return ShareBenefitSheetStatusType.SHARE_FAILED.getCode();
        } else {
            Map<String, String> resultMap = WxUtil.parseXmlToMap(resultStr);
            if (resultMap.get("return_code").equals("SUCCESS")) {
                if (resultMap.containsKey("err_code")) {
                    //有错误代码,执行失败
                    if (StringUtils.isNotBlank(resultMap.get("err_code")) && resultMap.get("result_code").equals("FAIL")) {
                        return ShareBenefitSheetStatusType.SHARE_FAILED.getCode();
                    }
                }
                if (resultMap.get("result_code").equals("SUCCESS")) {
                    return ShareBenefitSheetStatusType.SHARE_SUCCESS.getCode();
                }
            } else {
                //微信提示,返回该错误码时,再次执行分润需要使用相同的TradNo,避免重复分润的操作
                if (resultMap.get("return_code").equals("SYSTEMERROR")) {
                    return ShareBenefitSheetStatusType.SHARE_FAILED_RETRY.getCode();
                }
                return ShareBenefitSheetStatusType.SHARE_FAILED.getCode();
            }
        }
        return ShareBenefitSheetStatusType.SHARE_FAILED.getCode();
    }

    /**
     * 发送客服消息
     *
     * @param body
     * @param sysUserExt
     */
    private static String customSend(String body, SysUserExt sysUserExt) {
        String url = CustomSendUrl.replace("ACCESS_TOKEN", WxUtil.getAccessToken(sysUserExt));
        return HttpUtil.executePost(url, body);
    }

    /**
     * 将News对象变成微信可以读取的格式
     *
     * @param news
     * @return
     */
    public static StringBuffer buildNews(News news) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\"title\":\"");
        sb.append(news.getTitle());
        sb.append("\",\"description\":\"");
        sb.append(news.getDescription());
        sb.append("\",\"url\":\"");
        sb.append(news.getUrl());
        sb.append("\",\"picurl\":\"");
        sb.append(news.getPicUrl());
        sb.append("\"}");
        return sb;
    }

    /**
     * 微信绑定设备，用于获取微信回调
     *
     * @param body
     * @param sysUserExt
     * @return 绑定后的结果
     */
    public static String getAuthorizeDeviceUrl(String body, SysUserExt sysUserExt) {
        String url = AuthorizeUrl.replace("ACCESS_TOKEN", WxUtil.getAccessToken(sysUserExt));
        return HttpUtil.executePost(url, body);
    }

    /**
     * *根据openId和设备wxDidi解绑设备
     *
     * @param openId
     * @param wxDid
     * @param sysUserExt
     * @return
     */
    public static String unBindDev(String openId, String wxDid, SysUserExt sysUserExt) {
        JSONObject json = new JSONObject();
        json.put("device_id", wxDid);
        json.put("openid", openId);
        String url = UNBIND_DEV_URL.replace("ACCESS_TOKEN", WxUtil.getAccessToken(sysUserExt));
        return HttpUtil.executePost(url, json.toString());
    }

    /**
     * 通过openid获取用户在当前devicetype下绑定的deviceid列表。
     *
     * @param openid
     * @return 设备的微信
     */
    public static String getBindDev(String openid, SysUserExt sysUserExt) {
        String url = GET_OPENID_DID_URL.replace("ACCESS_TOKEN", WxUtil.getAccessToken(sysUserExt)).replace("OPENID", openid);
        return HttpUtil.executeGet(url);
    }
}