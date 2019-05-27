package com.gizwits.lease.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gizwits.boot.model.HttpRespObject;
import com.gizwits.boot.utils.HttpUtil;
import com.gizwits.boot.utils.MD5Kit;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.model.DeviceAddressModel;
import com.gizwits.lease.model.LocationPoint;
import com.gizwits.lease.model.MapDataEntity;
import com.gizwits.lease.model.PlatformAccessToken;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.redis.RedisService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by zhl on 2017/2/10.
 */
@Component
public class DeviceControlAPI {

    private static RedisService redisService;

    @Autowired(required = true)
    public void setRedisService(RedisService redisService) {
        DeviceControlAPI.redisService = redisService;
    }

    private static Logger logger = LoggerFactory.getLogger("LEASE_LOG");

    /**
     * 高德地图经纬度URL
     */
    private static String GD_GET_COORDINATE_URL = "http://apilocate.amap.com/position?accesstype=0&imei={imei}&cdma=0&bts={mcc},{mnc},{lac1},{cellid1},{signal1}&nearbts={mcc},{mnc},{lac2},{cellid2},{signal2}{|}{mcc},{mnc},{lac3},{cellid3},{signal3}&output=json&key=";

    private static String GD_DISTANCE_CALCULATE = "http://restapi.amap.com/v3/distance";

    /**
     * 获取平台access_token的url
     */
    public static String platformAccessToken = "http://enterpriseapi.gizwits.com/v1/products/PRODUCTKEY/access_token";

    /**
     * 机智云用户request_token
     */
    public static String GizwitsUserToken = "http://api.gizwits.com/app/request_token";


    public static String REMOTECONTROL = "http://enterpriseapi.gizwits.com/v1/products/PRODUCTKEY/devices/{did}/control";

    public static String GETADDRESSURL = "http://enterpriseapi.gizwits.com/dataapi/location/bs/dids/v1?product_key={productKey}&dids={dids}";

    /**
     * 获取机智云UserToken
     *
     * @param productKey
     * @return
     */
    public static String getGizwitsUserToken(String productKey, boolean isForce) {
        Product productEntity = JSONObject.parseObject(redisService.getProductData(productKey), Product.class);
        if (productEntity == null) {
            logger.error("===> 系统中不存在productKey[" + productKey + "]的产品信息");
            return null;
        }

        String userToken = redisService.getGizwitsUserToken(productKey);
        if (!StringUtils.isBlank(userToken) && !isForce) {//不为空
            return userToken;
        }

        Map headers = new HashedMap();
        headers.put("X-Gizwits-Application-Id", productEntity.getGizwitsAppId());
        headers.put("X-Gizwits-Application-Auth", MD5Kit.encode(productEntity.getGizwitsAppId() + productEntity.getGizwitsAppSecret()).toLowerCase());

        HttpRespObject rs = HttpUtil.sendPost(GizwitsUserToken, "", headers);
        JSONObject jsonObject = JSON.parseObject(rs.getContent().toString());
        if (jsonObject.containsKey("error_code") || jsonObject.getIntValue("error_code") == 5003
                || jsonObject.getIntValue("error_code") == 5004 || jsonObject.getIntValue("error_code") == 5005
                || jsonObject.getIntValue("error_code") == 5009 || jsonObject.getIntValue("error_code") == 5011) {
            rs = HttpUtil.sendPost(GizwitsUserToken, null, headers);
            jsonObject = JSON.parseObject(rs.getContent().toString());

            if (jsonObject.containsKey("error_code") || jsonObject.getIntValue("error_code") == 5003
                    || jsonObject.getIntValue("error_code") == 5004 || jsonObject.getIntValue("error_code") == 5005
                    || jsonObject.getIntValue("error_code") == 5009 || jsonObject.getIntValue("error_code") == 5011) {
                return null;
            }
        }
        redisService.cacheGizwitsUserToken(productKey, jsonObject.getString("token"), 50 * 60L);//机智云返回token的有效期是1小时,此处缓存50分钟
        return jsonObject.getString("token");
    }

    /**
     * 根据不同的Key,加载AccessToken
     *
     * @param productKey
     * @return
     */
    public static String getPlatformAccessToken(String productKey) {

        Product productEntity = JSONObject.parseObject(redisService.getProductData(productKey), Product.class);
        if (productEntity == null) {
            logger.error("===> 系统中不存在productKey[" + productKey + "]的产品信息");
            return null;
        }

        String gizwitsAccessToken = redisService.getGizwitsAccessTokenPrefix(productKey);
        if (!StringUtils.isBlank(gizwitsAccessToken)) {//不为空
            return gizwitsAccessToken;
        }


        JSONObject jsonParam = new JSONObject();
        jsonParam.put("enterprise_id", productEntity.getGizwitsEnterpriseId());
        jsonParam.put("enterprise_secret", productEntity.getGizwitsEnterpriseSecret());
        jsonParam.put("product_secret", productEntity.getGizwitsProductSecret());

        HttpRespObject rs = HttpUtil.sendPost(platformAccessToken.replace("PRODUCTKEY", productEntity.getGizwitsProductKey()), jsonParam.toString(), null);
        JSONObject jsonObject = JSON.parseObject(rs.getContent().toString());

        if (jsonObject.containsKey("error_code") || jsonObject.getIntValue("error_code") == 5003
                || jsonObject.getIntValue("error_code") == 5004 || jsonObject.getIntValue("error_code") == 5005
                || jsonObject.getIntValue("error_code") == 5009 || jsonObject.getIntValue("error_code") == 5011) {

            rs = HttpUtil.sendPost(platformAccessToken.replace("PRODUCTKEY", productKey), jsonParam.toString(), null);
            jsonObject = JSON.parseObject(rs.getContent().toString());

            if (jsonObject.containsKey("error_code") || jsonObject.getIntValue("error_code") == 5003
                    || jsonObject.getIntValue("error_code") == 5004 || jsonObject.getIntValue("error_code") == 5005
                    || jsonObject.getIntValue("error_code") == 5009 || jsonObject.getIntValue("error_code") == 5011) {
                return null;
            }
        }
        PlatformAccessToken accessToken = PlatformAccessToken.fromJson(rs.getContent());
        //缓存AccessToken
        redisService.cacheGizwitsAccessToken(productKey, accessToken.getToken(), 5 * 24 * 60 * 60L);
        return accessToken.getToken();
    }

    /**
     * 设备控制
     *
     * @param productKey
     * @param did
     * @param attrs
     * @return
     */
    public static boolean remoteControl(String productKey, String did, JSONObject attrs) {
        String accessToken = getPlatformAccessToken(productKey);
        if (StringUtils.isBlank(accessToken)) {
            logger.error("===> 控制设备失败!!!获取平台AccessToken失败,productKey[" + productKey + "]");
            return false;
        }

        JSONObject jsonParam = new JSONObject();
        jsonParam.put("attrs", attrs);

        logger.info("=====device control content: {},gizDid :{},pk:{}" , attrs.toJSONString(),did,productKey);
        HttpRespObject rs = HttpUtil.sendPost(REMOTECONTROL.replace("PRODUCTKEY", productKey).replace("{did}", did), jsonParam.toString(),
                getHeaders(accessToken));
        Object content = rs.getContent();
        if (ParamUtil.isNullOrEmptyOrZero(content)) {
            logger.warn("设备控制失败原因：{} , content = {}", rs.getStatusCode(), content);
            return false;
        }
        JSONObject jsonObject = JSON.parseObject(content.toString());
        logger.info("=====device controle result :" + jsonObject);
        if (ParamUtil.isNullOrEmptyOrZero(jsonObject)) {
            logger.warn("设备控制失败原因：" + rs.getStatusCode() + ", content=" + rs.getContent());
            return false;
        }
        if (jsonObject.containsKey("error_code")) {
            if (jsonObject.getIntValue("error_code") == 5009 || jsonObject.getIntValue("error_code") == 5011) {
                redisService.removeGizwitsAccessToken(productKey);//Token失效,从缓存中清除
                rs = HttpUtil.sendPost(REMOTECONTROL.replace("PRODUCTKEY", productKey).replace("{did}", did), jsonParam.toString(),
                        getHeaders(getPlatformAccessToken(productKey)));
            }
        }
        logger.info("设备控制结果：" + rs.isSuccess());
        if (rs.isSuccess()) {
            return true;
        } else {
            logger.warn("设备控制失败原因：" + rs.getStatusCode() + ", content=" + rs.getContent());
            return false;
        }
    }

    public static boolean remoteControl(String productKey, String did, String command) {
        return remoteControl(productKey, did, JSONObject.parseObject(command));
    }

    /**
     * 获取设备实时状态
     *
     * @param did
     * @param gizwitsAppId
     * @return
     */
    public static String deviceNowTimeData(String did, String gizwitsAppId) {
        String result = "";
        Map<String, String> headers = new HashMap<String, String>(1);
        headers.put("X-Gizwits-Application-Id", gizwitsAppId);

        String url = "http://api.gizwits.com/app/devdata/" + did + "/latest";
        HttpRespObject httpRespObject = HttpUtil.sendGet(url,
                headers);
        if (!httpRespObject.isSuccess()) {
            logger.info("访问失败" + httpRespObject.getStatusCode());
        } else {
            result = httpRespObject.getContent().toString();
        }
        logger.info("机智云获取设备信息:" + result);
        return result;
    }

    /**
     * 下发设备状态查询指令,设备在2s内通过Netty上报所有的数据点
     *
     * @param did
     * @param productKey
     * @param username
     * @return
     */

    public static boolean sendDataQueryCommand(String did, String productKey, String username) {
        Product productEntity = JSONObject.parseObject(redisService.getProductData(productKey), Product.class);
        if (productEntity == null) {
            logger.error("===> 系统中不存在productKey[" + productKey + "]的产品信息");
            return false;
        }

        String result = "";
        Map<String, String> headers = new HashMap<String, String>(2);
        //  TODO:      需要修改
        headers.put("X-Gizwits-Application-Id", productEntity.getGizwitsAppId());
        headers.put("X-Gizwits-User-token", getGizwitsUserToken(productKey, true));

        String url = "http://api.gizwits.com/app/control/" + did;
        HttpRespObject httpRespObject = HttpUtil.sendPost(url, "{\"raw\": [5,255,255,255,255]}",
                headers);
        if (!httpRespObject.isSuccess()) {
            logger.info("访问失败" + httpRespObject.getStatusCode());
            return false;
        } else {

            return true;
        }
    }

    public static boolean sendDataQueryCommand(String did, String productKey) {
        String accessToken = getPlatformAccessToken(productKey);
        if (StringUtils.isBlank(accessToken)) {
            logger.error("===> 控制设备失败!!!获取平台AccessToken失败,productKey[" + productKey + "]");
            return false;
        }

        HttpRespObject rs = HttpUtil.sendPost(REMOTECONTROL.replace("PRODUCTKEY", productKey).replace("{did}", did), "{\"raw\": [18,255,255,255,255]}",
                getHeaders(accessToken));
        JSONObject jsonObject = JSON.parseObject(rs.getContent().toString());

        if (jsonObject.containsKey("error_code")) {
            if (jsonObject.getIntValue("error_code") == 5009 || jsonObject.getIntValue("error_code") == 5011) {
                redisService.removeGizwitsAccessToken(productKey);//Token失效,从缓存中清除
                rs = HttpUtil.sendPost(REMOTECONTROL.replace("PRODUCTKEY", productKey).replace("{did}", did), "{\"raw\": [18,255,255,255,255]}",
                        getHeaders(getPlatformAccessToken(productKey)));
            }
        }

        return rs.isSuccess();
    }

    /**
     * 单数据点设备控制
     *
     * @param productKey
     * @param did
     * @param key
     * @param value
     * @return
     */
    public static boolean remoteControl(String productKey, String did, String key, Object value) {
        if (StringUtils.isBlank(productKey) || StringUtils.isBlank(did) || StringUtils.isBlank(key) || StringUtils.isBlank(value.toString())) {
            logger.error("====> 传递参数错误,控制设备失败,productKey[" + productKey + "] did[" + did + "] key[" + key + "] value[" + value.toString() + "]");
            return false;
        }
        JSONObject attrs = new JSONObject();
        attrs.put(key, value);
        return remoteControl(productKey, did, attrs);
    }

    /**
     * 获取设备的经纬度信息
     *
     * @param productKey
     * @param did
     * @return
     */
    public static DeviceAddressModel getDeviceAddress(String productKey, String did) {
        Product productEntity = JSONObject.parseObject(redisService.getProductData(productKey), Product.class);
        if (productEntity == null) {
            logger.error("===> 系统中不存在productKey[" + productKey + "]的产品信息");
            return null;
        }

        Map<String, String> header = new HashMap<String, String>(3);
        header.put("X-Gizwits-ProductKey", productKey);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        header.put("X-Gizwits-Timestamp", timestamp);
        header.put("X-Gizwits-Signature", MD5Kit.encode(productEntity.getGizwitsProductSecret() + timestamp).toLowerCase());


        HttpRespObject rs = HttpUtil.sendGet(GETADDRESSURL.replace("{productKey}", productKey).replace("{dids}", did), header);
        if (rs.getStatusCode() == 200) {
            logger.info("===机智云获取设备位置结果code:{},content:{}====", rs.getStatusCode() + "", rs.getContent().toString());
            if (rs.getContent().toString().equals("{}")) {
                return null;
            }
            JSONArray jsonArray = JSON.parseArray(rs.getContent().toString());
            if (jsonArray != null && jsonArray.size() > 0) {
                JSONObject jsonObject1 = JSON.parseObject(jsonArray.get(0).toString());
                if (jsonObject1 != null) {
                    String provinceStr = jsonObject1.getString("province");
                    String cityStr = jsonObject1.getString("city");
                    String districtStr = jsonObject1.getString("district");
                    String roadStr = jsonObject1.getString("road");
                    String streetStr = jsonObject1.getString("street");
                    BigDecimal longitude = jsonObject1.getBigDecimal("longitude");
                    BigDecimal latitude = jsonObject1.getBigDecimal("latitude");
                    String address = provinceStr + cityStr + districtStr + roadStr + streetStr;

                    DeviceAddressModel deviceAddressDTO = new DeviceAddressModel(address, longitude, latitude);
                    deviceAddressDTO.setProvince(provinceStr);
                    deviceAddressDTO.setCity(cityStr);
                    deviceAddressDTO.setRoad(roadStr);
                    return deviceAddressDTO;
                }
            }
        }
        return null;
    }

    /**
     * 根据高德接口获取经纬度及详细地址信息
     *
     * @param mapDataEntity
     * @return
     */
    public static DeviceAddressModel getDeviceAddressByGDMap(MapDataEntity mapDataEntity) {
        try {
            String url = GD_GET_COORDINATE_URL.replace("{imei}", mapDataEntity.getImei()).replace("{mcc}", mapDataEntity.getMcc()).replace("{mnc}", mapDataEntity.getMnc()).replace("{lac1}", mapDataEntity.getLac1()).replace("{cellid1}", mapDataEntity.getCellid1()).replace("{signal1}", mapDataEntity.getRssi1()).replace("{lac2}", mapDataEntity.getLac2()).replace("{cellid2}", mapDataEntity.getCellid2()).replace("{signal2}", mapDataEntity.getRssi2()).replace("{lac3}", mapDataEntity.getLac3()).replace("{cellid3}", mapDataEntity.getCellid3()).replace("{signal3}", mapDataEntity.getRssi3()).replace("{|}", java.net.URLEncoder.encode("|", "GBK")) + SysConfigUtils.get(CommonSystemConfig.class).getGDMapLocationKey();
            String rs = HttpUtil.executeGet(url);
            logger.info("====高德地图获取,,调用高德返回结果:{}======" + rs);
            JSONObject jsonObject = JSON.parseObject(rs);
            if (jsonObject.getBoolean("status") && jsonObject.get("result") != null) {
                JSONObject result = JSONObject.parseObject(jsonObject.get("result").toString());
                DeviceAddressModel deviceAddressModel = new DeviceAddressModel();
                deviceAddressModel.setAddress(result.getString("desc"));
                deviceAddressModel.setCountry(result.getString("country"));
                deviceAddressModel.setProvince(result.getString("province"));
                deviceAddressModel.setCity(result.getString("city"));
                deviceAddressModel.setCityCode(result.getInteger("citycode"));
                deviceAddressModel.setAdcode(result.getString("adcode"));
                deviceAddressModel.setRoad(result.getString("road"));
                if (result.containsKey("location")) {
                    String[] location = result.getString("location").split(",");
                    deviceAddressModel.setLongitude(new BigDecimal(location[0]));
                    deviceAddressModel.setLatitude(new BigDecimal(location[1]));
                    return deviceAddressModel;
                }
                return null;
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Long calculatePointDistance(LocationPoint sourcePoint, LocationPoint targetPoint) {
        if (Objects.isNull(sourcePoint) || Objects.isNull(targetPoint)) {
            logger.error("======获取两点位置出错,参数出现空值===");
            return null;
        }

        StringBuffer sb = new StringBuffer(GD_DISTANCE_CALCULATE);
        sb.append("?").append("origins=").append(sourcePoint.getLongitude()).append(",").append(sourcePoint.getLatitude()).append("&");
        sb.append("destination=").append(targetPoint.getLongitude()).append(",").append(targetPoint.getLatitude()).append("&");
        sb.append("output=json").append("&");
        sb.append("key=").append(SysConfigUtils.get(CommonSystemConfig.class).getGDMapWebApiKey());

        String rs = HttpUtil.executeGet(sb.toString());
        logger.info("====高德地图计算两点位置,,调用高德返回结果:{}======" + rs);
        if (StringUtils.isNotBlank(rs)) {
            JSONObject jsonObject = JSON.parseObject(rs);
            if (jsonObject.containsKey("status") && jsonObject.getInteger("status").equals(1)
                    && jsonObject.containsKey("info") && jsonObject.getString("info").equals("OK")) {
                JSONArray jsonArray = jsonObject.getJSONArray("results");
                if (Objects.nonNull(jsonArray) && jsonArray.size() > 0) {
                    JSONObject resultJson = jsonArray.getJSONObject(0);
                    if (Objects.nonNull(resultJson) && resultJson.containsKey("distance")) {
                        return resultJson.getLong("distance");
                    }
                }
            }
        }

        return null;
    }


    /**
     * 拼装请求头
     *
     * @param accessToken
     * @return
     */
    public static Map<String, String> getHeaders(String accessToken) {
        Map<String, String> header = new HashMap<String, String>(2);
        header.put("Accept", "application/json");
        header.put("authorization", "token " + accessToken);

        return header;
    }
}
