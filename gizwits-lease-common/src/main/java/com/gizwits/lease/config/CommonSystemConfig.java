package com.gizwits.lease.config;

import com.gizwits.boot.annotation.SysConfig;

/**
 * Config - 通用系统配置
 * <pre>
 *     目前只支持返回值为String的方法，后续再完善
 *     2017/9/7 返回类型可支持更多类型，具体可参考ConvertUtilsBean
 * </pre>
 *
 * @author lilh
 * @date 2017/7/3 19:51
 */
public interface CommonSystemConfig {

    @SysConfig(value = "http://enterpriseapi.gizwits.com", remark = "企业api地址")
    String getEnterpriseApiHost();

    @SysConfig(value = "/v1/products/{0}/access_token", remark = "获取token的uri")
    String getEnterpriseTokenUri();

    @SysConfig(value = "/v1/products/{0}/datapoint", remark = "获取产品数据点的uri")
    String getProductDataPointUri();

    @SysConfig(value = "3", remark = "厂商角色id")
    String getDefaultManufacturerRoleId();

    @SysConfig(value = "4", remark = "运营商角色id")
    Integer getDefaultOperatorRoleId();

    @SysConfig(value = "维护", remark = "维护角色")
    String getMaintenanceRole();

    @SysConfig(value = "9", remark = "维护角色id")
    Integer getMaintenanceRoleId();


    @SysConfig(value = "1,2,4,5,16,17", remark = "首页默认显示的项")
    Integer[] getDefaultDisplayPanelItem();

    @SysConfig(value = "device.status.kv,device.online,device.offline,device.attr_fault,device.attr_alert", remark = "发送给机智云的默认事件")
    String getDefaultGizwitsEvent();

    @SysConfig(value = "http://localhost:8090/", remark = "netty服务的地址,用于远程重启netty重连")
    String getNettyHostWithPort();

    @SysConfig(value = "o7zimwuSJVlAAJ4-Arr7r-sIQvMo", remark = "分润单生成成功通知的人openid")
    String getShareBenefitSuccessNotifyOpenids();


    /**
     * 短信配置
     */
    @SysConfig(value = "【意智云】#app#，您的验证码是#code#。如非本人操作，请忽略本短信。", remark = "短信验证码模板")
    String getMessageCodeTemplate();

    @SysConfig(value = "智慧分享", remark = "短信验证码中的app对应文字")
    String getMessageCodeParamApp();

    @SysConfig(value = "【卡励科技】故障通知，#device#，故障现象：#symptom#，故障时间：#time#，请及时处理。")
    String getMessageDeviceAlarmTemplate();

    @SysConfig(value = "aab30fd313bf3d37e814def81755d99b", remark = "云片APIKey")
    String getMessageApiKey();

    @SysConfig(value = "1934318", remark = "短信验证码的模板ID")
    String getMessageCodeTemplateId();

    @SysConfig(value = "1940172", remark = "故障推送消息模板ID")
    String getMessageDeviceAlarmTemplateId();

    @SysConfig(value = "[0-9A-Z]{10}", remark = "充值卡的正则表达式规则,默认数字和字母长度为10的组合")
    String getUserChargeCardRegexp();

    /**
     * 微信访问的路径
     */
    @SysConfig(value = "http://lease.iotsdk.com", remark = "服务器域名")
    String getHostWithContext();

    /**
     * 生成网页端的微信二维码时，需要根据域名来拼接qrcodeAccessUrl
     */
    @SysConfig(value = "/app/wx/init?deviceId=", remark = "二维码指向的路径")
    String getQrcodeAccessUrl();

    @SysConfig(value = "/data/lease/files/qrcode", remark = "二维码存放位置")
    String getQrcodePath();


    @SysConfig(value = "100000", remark = "默认导出的列表大小")
    Integer getDefaultExportSize();

    @SysConfig(value = "/app/scan/init?deviceId=", remark = "微信点击模板消息跳转的页面")
    String getWxAccessUrl();

    @SysConfig(value = " ", remark = "nginx映射和应用的ContextPath路径,主要用户一些URL的回调使用")
    String getNginxSuffixAndContextPath();

    @SysConfig(value = "faff5815fa133b94b68cc213b460b1fd", remark = "高德地图定位MapKey")
    String getGDMapLocationKey();

    @SysConfig(value = "5db620cb851d3026410368a466217c10", remark = "高德地图WebApi接口Key")
    String getGDMapWebApiKey();

    @SysConfig(value = "/app/wx/init", remark = "微信获取Code回调地址")
    String getWxCodeUrlPath();

    @SysConfig(value = " ", remark = "微信支付回调")
    String getWxPayCallBackUrlPath();

    /**
     * 支付宝相关
     */
    @SysConfig(value = "auth_user", remark = "授权获取用户信息范围")
    String getAlipayUserScope();

    @SysConfig(value = "http://lease.iotsdk.com/xiangzhiyun/alipay/userinfo", remark = "支付宝获取Code回调地址")
    String getAlipayCodeUrl();

    @SysConfig(value = "authorization_code", remark = "支付宝授权访问令牌的授权类型")
    String getAlipayGrantType();

    @SysConfig(value = "UTF-8", remark = "支付宝签名编码")
    String getAlipaySignCharset();

    @SysConfig(value = "RSA2", remark = "支付宝签名方式")
    String getAlipaySignType();

    @SysConfig(value = "https://openapi.alipay.com/gateway.do", remark = "支付宝网关入口")
    String getAlipayGetwayUrl();

    /**
     * 头像存放位置
     *
     * @return
     */
    @SysConfig(value = "/data/lease/files/avatar/", remark = "头像存放位置")
    String getAvatarPath();

    @SysConfig(value = "wx8c1052b60933fb8a", remark = "微信公众号APPID")
    String getWxAppId();

    @SysConfig(value = "4fa2b180f8529f3911094ce03366f66a", remark = "微信公众号APPSECRET")
    String getWxAppSecret();

    @SysConfig(value = "欢迎关注设备租赁", remark = "微信公众号关注推送")
    String getWxSubscribeMsg();


    @SysConfig(value = "/home/lease/tomcat-app-3/webapps/ROOT/apiclient_cert.p12", remark = "微信公众号证书")
    String getWxCertDirectoryPath();


    @SysConfig(value = "/data/lease/files/images/", remark = "问题反馈图片路径")
    String getFeedBackImagePath();


    @SysConfig(value = "欢迎使用卡励空气净化器", remark = "微信公众号欢迎语句")
    String getWxWelcomeMsg();


    @SysConfig(value = "/device_template.xls", remark = "设备导入模板文件")
    String getDefaultDeviceExcelTemplateFile();

    @SysConfig(value = "设备mac,二维码", remark = "模板文件的title")
    String getDefaultDeviceExcelTemplateTitle();

    @SysConfig(value = "6", remark = "充值设定的数量")
    Integer getDefaultChargeSettingCount();

    @SysConfig(value = "/mnt/workspace/ifresh/tomcat-app/files/images/afrpic.png", remark = "微信公众号news的图片")
    String getWxNewsPicUrl();

    @SysConfig(value = "http://www.ifreshkj.com/", remark = "微信公众号返回的地址")
    String getWxTipUrl();

    @SysConfig(value = "UtSRD9LA1InyJbEGYsCCx78qMbraHCZNvtNSx-5t5Sg", remark = "微信公众号租赁模版消息id")
    String getWxTipTemplateId();

    @SysConfig(value = "JMJhdUWgC-aRD9LUVoDo2l-DNMQ4TafqeyfxJ9qB_ms", remark = "麻将机CallOut公众号模板ID")
    String getMahjongCallTemplateId();

    @SysConfig(value = "投放点管理,运营商管理,运营商列表", remark = "运营商未开放投放点时不能展示的菜单")
    String getMahjongOperatorAllotNames();

    /**
     * 控制设备
     */
    @SysConfig(value = "120",remark = "设备多久没上报数据，或者上报的状态没有符合预期，则重发指令（单位：秒）")
    String getControlTimeOut();
    @SysConfig(value = "3",remark = "重发指令次数")
    String getControlTryTimes();
    @SysConfig(value = "3",remark = "同一个设备，连续出现多少次下单后没上报数据，则锁定设备")
    String getControlLockTimes();

    @SysConfig(value = "18520207641",remark = "管理员手机号（系统故障通知）")
    String getManagerMobile();
}
