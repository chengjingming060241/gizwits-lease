package com.gizwits.lease.constant;

/**
 * Created by rongmc on 2017/6/19.
 * 系统配置常量类,需要读取系统配置sys_config表中的常量,则要定义对应的key
 * 命名规范是,变量名称为变量值的大写,如 TEST_CONFIG = "test_config"
 */
public class SystemConfigKey {

    /*******************************************/
    /****** 微信公众号配置 -- 阳澄湖公众号**********/
    /*******************************************/
//    public static String WX_TOKEN = "cGyvBJQpoo";
//    public static String WX_ID = "gh_29ffd8a7f58c";
//    public static String WX_APPID = "wx8c1052b60933fb8a";
//    public static String WX_APPSCRERT = "9d0af31fa31735e83e5d4874a943147c";
//    public static String WX_SUBSCRIBE_MSG = "欢迎您关注租赁平台公众号。";
//    public static String WX_PARTNERID = "1230594302";
//    public static String WX_PARTNERSCRERT = "4fa2b180f8529f3911094ce03366f66a";
//    public static String WX_PAYBODY = "租赁消费";
    public static String WX_PAY_CALLBACK_URL = "";//微信支付回调
//    public static String WX_CERT_DIRECTORY_PATH = "";//微信证书的存放位置,此处仅仅到证书存放的目录,每个微信号的证书名以APPID命名


    /*******************************************/
    /**************** 系统配置 ******************/
    /*******************************************/
    public static String SYS_NGINXSUFFIX_AND_CONTEXTPATH = ""; //nginx映射和应用的ContextPath路径,主要用户一些URL的回调使用

    public static String GD_MAP_KEY = "faff5815fa133b94b68cc213b460b1fd";//高德地图Key

    /**
     * 扫码后访问路径的配置获取
     */
    public static String QECORD_WX_PATH = "/app/wx/init";
    public static String QRCODE_SUFFIX_PATH = "/app/scan/init";//设备二维码生成的后缀


   



}
