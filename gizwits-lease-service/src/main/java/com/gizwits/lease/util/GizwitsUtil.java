package com.gizwits.lease.util;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.model.HttpRespObject;
import com.gizwits.boot.utils.HttpUtil;
import com.gizwits.boot.utils.MD5Kit;
import com.gizwits.lease.product.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GaGi on 2017/8/5.
 */
public class GizwitsUtil {
    public static String ALL_BIND_DEVICE_PHONE_ID = "";
    private static String GIZWITS_APPLICATION_ID = "X-Gizwits-Application-Id";
    private static String GIZWITS_USER_TOKEN = "X-Gizwits-User-token";

    private static String GIZWITS_TIMESTAMP= "X-Gizwits-Timestamp";
    private static String GIZWITS_SIGNATURE = "X-Gizwits-Signature";
    private static String BIND_MAC_URL = "http://api.gizwits.com/app/bind_mac";
    private static String CREATE_USER_URL = "http://api.gizwits.com/app/users";
    protected static Logger logger = LoggerFactory.getLogger(GizwitsUtil.class);

    /**
     * 根据phoneId创建匿名用户
     *
     * @param phoneId
     * @return
     */
    public static String createUser(String phoneId, String appId) {
        String res = "";
        Map<String, String> headers = new HashMap<>(1);
        headers.put(GIZWITS_APPLICATION_ID, appId);
        String data = "{\"phone_id\": \"" + phoneId + "\"}";
        HttpRespObject httpRespObject = HttpUtil.sendPost(CREATE_USER_URL, data, headers);
        if (!httpRespObject.isSuccess()) {
            logger.error("==========>访问创建机智云创建用户接口失败" + httpRespObject.getStatusCode());
        } else {
            res = httpRespObject.getContent().toString();
        }
        logger.info("=========>创建机智云用户成功" + res);
        return res;
    }

    /**
     * 根据token和appid,productKey,设备mac绑定设备
     * @param mac
     * @param product
     * @param userToken
     * @return
     */

    public static String bindDevice(String mac, Product product, String userToken) {
        String res = "";
        String timeStamp = String.valueOf(System.currentTimeMillis()/1000);
        Map<String,String> headers = new HashMap<>(4);
        headers.put(GIZWITS_APPLICATION_ID,product.getGizwitsAppId());
        headers.put(GIZWITS_USER_TOKEN,userToken);
        headers.put(GIZWITS_TIMESTAMP,timeStamp);
        headers.put(GIZWITS_SIGNATURE, MD5Kit.encode(product.getGizwitsProductSecret()+timeStamp).toLowerCase());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("product_key",product.getGizwitsProductKey());
        jsonObject.put("mac", mac);
        jsonObject.put("remark","");
        jsonObject.put("dev_alias","");
        HttpRespObject httpRespObject = HttpUtil.sendPost(BIND_MAC_URL, jsonObject.toString(), headers);
        if (!httpRespObject.isSuccess()){
            logger.error("================>绑定产品"+product.getGizwitsProductKey()+"的设备"+ mac+"失败"+httpRespObject.getStatusCode());
        }
        res = httpRespObject.getContent().toString();
        logger.info("===========>绑定产品"+product.getGizwitsProductKey()+"设备mac为"+ mac+res);
        if (StringUtils.isEmpty(res)){
            logger.error("============>绑定设备失败");
        }
        return res;
    }


}
