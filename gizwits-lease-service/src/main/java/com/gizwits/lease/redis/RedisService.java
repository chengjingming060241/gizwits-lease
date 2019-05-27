package com.gizwits.lease.redis;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Created by kufufu on 2017/4/1.
 */
@SuppressWarnings("unchecked")
@Service
public class RedisService {

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;


    private final static String CURRENT_ONLINE_STATUS_PREFIX = "_CURRENT_ONLINE_STATUS_";
    private final static String CURRENT_DATA_STATUS_PREFIX = "_CURRENT_DATA_STATUS_";
    private final static String CRRRENT_DATTA_STATUS_PORT ="_CURRENT_DATA_STATUS_";
    private final static String ORDER_STATUS_PREFIX = "_ORDER_STATUS_";
    private final static String PRODUCT_DATA_PREFIX = "_PRODUCT_DATA_";
    private final static String ACCESS_TOKEN_PREFIX = "_ACCESS_TOKEN_";
    private final static String WX_CONFIG_PREFIX = "_WX_CONFIG_";
    private final static String WX_USER_INFO_ACCESS_TOKEN_PREFIX = "_WX_USER_INFO_ACCESS_TOKEN_PREFIX_";//微信获取用户信息开放接口AccessToken,不同于微信其他接口的AccessToken
    private final static String WX_USER_INFO_REFRESH_TOKEN_PREFIX = "_WX_USER_INFO_REFRESH_TOKEN_PREFIX_";//微信获取用户信息开放接口AccessToken,用于获取新的AccessToken,有效期30天
    private final static String JSAPI_TICKET_PREFIX = "_JSAPI_TICKET_";
    private final static String GIZWITS_ACCESS_TOKEN_PREFIX = "_GIZWITS_ACCESS_TOKEN_PREFIX_";
    private final static String GIZWITS_USER_TOKEN_PREFIX = "_GIZWITS_USER_TOKEN_PREFIX_";//机智云UserToken
    private final static String DEVICE_LOCK_BY_ORDER = "_DEVICE_LOCK_BY_ORDER_";//用户下单锁定设备,防止其他人下单
    private final static String DEVICE_CONTROL_COMMAND_PREFIX = "_DEVICE_CONTROL_COMMAND_PREFIX_";//用户通过后台下发指令时,如果设备离线,暂时缓存下发的指令,等设备上线的时候立刻下发指令
    private final static String PRODUCT_STATUS_COMMAND_PREFIX = "_PRODUCT_STATUS_COMMAND_PREFIX_";//产品状态指令
    private final static String PRODUCT_MONIT_DATAPOINT_PREFIX = "_PRODUCT_MONIT_DATAPOINT_PREFIX_";//产品要监控变动的数据点
    private final static String USER_TOKEN = "_USER_TOKEN_";
    private final static String UID = "_UID_";

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private ProductDataPointService productDataPointService;

    /*********************************************************************/
    /************************ 产品监控变动数据点的缓存  *********************/
    /*********************************************************************/
    public void cacheProductMonitPoint(String productKey, String jsonObject) {
        if (StringUtils.isEmpty(productKey) || null == jsonObject)
            return;
        redisTemplate.opsForHash().put(PRODUCT_MONIT_DATAPOINT_PREFIX, productKey, jsonObject);
    }

    public String getProductMonitPoint(String productKey) {
        if (StringUtils.isEmpty(productKey)) {
            return null;
        }

        String result = null;
        if (redisTemplate.opsForHash().hasKey(PRODUCT_MONIT_DATAPOINT_PREFIX, productKey)) {
            result = redisTemplate.opsForHash().get(PRODUCT_MONIT_DATAPOINT_PREFIX, productKey).toString();
        }

        if(Objects.isNull(result)) {
            //从数据库中加载
            List<ProductDataPoint> pointList = productDataPointService.getMonitDataPoint(productKey);
            if(CollectionUtils.isNotEmpty(pointList)) {
                result = org.apache.commons.lang.StringUtils.join(pointList.stream().map(ProductDataPoint::getIdentityName).toArray(), ',');
                cacheProductMonitPoint(productKey, result);
            }
        }
        return result;
    }

    public boolean containProductMoint(String productKey) {
        if (StringUtils.isEmpty(productKey))
            return false;
        return redisTemplate.opsForHash().hasKey(PRODUCT_MONIT_DATAPOINT_PREFIX, productKey);
    }

    public boolean deleteProductMonit(String productKey) {
        if(!StringUtils.isEmpty(productKey)) {
            if(containProductMoint(productKey)) {
                return Boolean.valueOf(redisTemplate.opsForHash().delete(PRODUCT_MONIT_DATAPOINT_PREFIX, productKey).toString());
            }
        }
        return false;
    }

    /*****************************************************************/
    /************************ 产品相关状态指令的缓存  *******************/
    /*****************************************************************/
    public void cacheProductStatusCommand(String productKey, String status, String command) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(status) || StringUtils.isEmpty(command)) {
            return;
        }
        redisTemplate.opsForHash().put(PRODUCT_STATUS_COMMAND_PREFIX, productKey + "_" + status, command);
    }

    public JSONObject getProductStatusCommand(String productKey, String status) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(status))
            return null;
        JSONObject result = null;
        if (containsProductStatusCommand(productKey, status)) {
            result = JSONObject.parseObject(redisTemplate.opsForHash().get(PRODUCT_STATUS_COMMAND_PREFIX, productKey + "_" + status).toString());
        }
        if(Objects.isNull(result)) {
            //从数据库中查找
            Product product = productService.selectOne(new EntityWrapper<Product>().eq("gizwits_product_key", productKey).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
            if(Objects.nonNull(product)) {
                ProductCommandConfig commandConfig = productCommandConfigService.selectOne(new EntityWrapper<ProductCommandConfig>().eq("product_id", product.getId()).eq("status_command_type", status));
                if(Objects.nonNull(commandConfig)) {
                    result = JSONObject.parseObject(commandConfig.getCommand());
                    cacheProductStatusCommand(productKey, status, commandConfig.getCommand());
                }
            }
        }
        return result;
    }

    public boolean containsProductStatusCommand(String productKey, String status) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(status)) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(PRODUCT_STATUS_COMMAND_PREFIX, productKey + "_" + status);
    }

    public boolean deleteProductStatusCommand(String productKey, String status) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(status)) {
            return false;
        }
        if (containsProductStatusCommand(productKey, status)) {
            return Boolean.valueOf(redisTemplate.opsForHash().delete(PRODUCT_STATUS_COMMAND_PREFIX, productKey + "_" + status).toString());
        }
        return false;
    }


    /*****************************************************************/
    /****************** 设备离线时下发指令的缓存  ***************/
    /***********************************************************/
    public void cacheDeviceControlCommand(String productKey, String mac, String command) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(mac) || StringUtils.isEmpty(command))
            return;
        redisTemplate.opsForHash().put(DEVICE_CONTROL_COMMAND_PREFIX, productKey + "_" + mac, command);
    }

    public JSONObject getDeviceControlCommand(String productKey, String mac) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(mac))
            return null;
        if (redisTemplate.opsForHash().hasKey(DEVICE_CONTROL_COMMAND_PREFIX, productKey + "_" + mac)) {
            return JSONObject.parseObject(redisTemplate.opsForHash().get(DEVICE_CONTROL_COMMAND_PREFIX, productKey + "_" + mac).toString());
        }
        return null;
    }

    public boolean containsDeviceControlCommand(String productKey, String mac) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(mac))
            return false;
        return redisTemplate.opsForHash().hasKey(DEVICE_CONTROL_COMMAND_PREFIX, productKey + "_" + mac);
    }

    public boolean removeDeviceControlCommand(String productKey, String mac) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(mac))
            return false;
        if (redisTemplate.opsForHash().hasKey(DEVICE_CONTROL_COMMAND_PREFIX, productKey + "_" + mac)) {
            return Boolean.valueOf(redisTemplate.opsForHash().delete(DEVICE_CONTROL_COMMAND_PREFIX, productKey + "_" + mac).toString());
        }
        return false;
    }


    /*****************************************************************/
    /****************** 用户下单后会将设备锁定一段时间 ***************/
    /***********************************************************/
    public void cacheDeviceLockByOrder(String deviceId, String openid, Long lockSeconds) {
        if (StringUtils.isEmpty(deviceId) || StringUtils.isEmpty(openid) || lockSeconds == null) {
            return;
        }
        redisTemplate.opsForValue().set(DEVICE_LOCK_BY_ORDER + deviceId, openid, lockSeconds, TimeUnit.SECONDS);
    }

    public boolean containDeviceLockByOrder(String deviceId) {
        if (StringUtils.isEmpty(deviceId))
            return false;
        return redisTemplate.opsForValue().get(DEVICE_LOCK_BY_ORDER + deviceId) != null;
    }

    public String getDeviceLockByOpenidAndOrder(String deviceId) {
        if (StringUtils.isEmpty(deviceId))
            return null;
        if (redisTemplate.opsForValue().get(deviceId) != null) {
            return redisTemplate.opsForValue().get(DEVICE_LOCK_BY_ORDER + deviceId).toString();
        }
        return null;
    }

    public boolean removeDeviceFromLockByOrder(String deviceId) {
        if (StringUtils.isEmpty(deviceId))
            return false;
        if (redisTemplate.opsForValue().get(DEVICE_LOCK_BY_ORDER + deviceId) != null) {
            redisTemplate.delete(DEVICE_LOCK_BY_ORDER + deviceId);
            return true;
        }
        return false;
    }

    /***********************************************************/
    /****************** Gizwits UserToken 相关*****************/
    /***********************************************************/
    public void cacheGizwitsUserToken(String productKey, String token, long expire) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(token))
            return;
        redisTemplate.opsForValue().set(GIZWITS_USER_TOKEN_PREFIX + productKey, token, expire, TimeUnit.SECONDS);
    }

    public String getGizwitsUserToken(String productKey) {
        if (StringUtils.isEmpty(productKey))
            return null;
        Object result = redisTemplate.opsForValue().get(GIZWITS_USER_TOKEN_PREFIX + productKey);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }

    /**
     * 将匿名用户的userToken保存
     *
     * @param uid
     * @param token
     * @param expire
     */
    public void setUserToken(String productKey, String uid, String token, long expire) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token) || StringUtils.isEmpty(productKey)) {
            return;
        }
        redisTemplate.opsForValue().set(UID + productKey, uid, expire, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(USER_TOKEN + productKey, token, expire, TimeUnit.SECONDS);
    }

    public void setUserTokenByUsername(String username, String uid, String token, long expire) {
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(token) || StringUtils.isEmpty(username)) {
            return;
        }
        redisTemplate.opsForValue().set(UID + username, uid, expire, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(USER_TOKEN + username, token, expire-100, TimeUnit.SECONDS);
    }

    /**
     * 根据产品获取当前uid对应的userToken
     *
     * @param productKey
     * @return
     */
    public String getUserToken(String productKey) {
        if (StringUtils.isEmpty(productKey))
            return null;
        Object result = redisTemplate.opsForValue().get(USER_TOKEN + productKey);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }


    /**
     * 根据产品获取当前openid对应的userToken
     *
     * @param username
     * @return
     */
    public String getUserTokenByUserName(String username) {
        if (StringUtils.isEmpty(username))
            return null;
        Object result = redisTemplate.opsForValue().get(USER_TOKEN + username);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }

    /**
     * 根据productKey获取Uid
     *
     * @param productKey
     * @return
     */
    public String getUid(String productKey) {
        if (StringUtils.isEmpty(productKey))
            return null;
        Object result = redisTemplate.opsForValue().get(UID + productKey);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }


    /**
     * 根据openid获取Uid
     *
     * @param username
     * @return
     */
    public String getUidByUsername(String username) {
        if (StringUtils.isEmpty(username))
            return null;
        Object result = redisTemplate.opsForValue().get(UID + username);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }


    /***********************************************************/
    /****************** Gizwits AccessToken 相关*****************/
    /***********************************************************/
    public void cacheGizwitsAccessToken(String productKey, String token, long expire) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(token))
            return;
        redisTemplate.opsForValue().set(GIZWITS_ACCESS_TOKEN_PREFIX + productKey, token, expire, TimeUnit.SECONDS);
    }

    public String getGizwitsAccessTokenPrefix(String productKey) {
        if (StringUtils.isEmpty(productKey))
            return null;
        Object result = redisTemplate.opsForValue().get(GIZWITS_ACCESS_TOKEN_PREFIX + productKey);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }

    public boolean removeGizwitsAccessToken(String productKey) {
        if (StringUtils.isEmpty(productKey))
            return false;
        if (redisTemplate.opsForValue().get(GIZWITS_ACCESS_TOKEN_PREFIX + productKey) != null) {
            redisTemplate.delete(GIZWITS_ACCESS_TOKEN_PREFIX + productKey);
            return true;
        }
        return false;
    }


    /***********************************************************/
    /****************** JSAPITicket 相关*************************/
    /***********************************************************/
    public void cacheJSAPITicket(String wxId, String ticketObject, long expire) {
        if (StringUtils.isEmpty(wxId) || StringUtils.isEmpty(ticketObject))
            return;
        redisTemplate.opsForValue().set(JSAPI_TICKET_PREFIX + wxId, ticketObject, expire, TimeUnit.SECONDS);
    }

    public String getJSAPITicket(String wxId) {
        if (StringUtils.isEmpty(wxId))
            return null;
        Object result = redisTemplate.opsForValue().get(JSAPI_TICKET_PREFIX + wxId);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }

    public boolean containJSAPITicket(String wxId) {
        return !StringUtils.isEmpty(getJSAPITicket(wxId));
    }

    /***********************************************************/
    /****************** Wx AccessToken 相关*********************/
    /***********************************************************/
    public void cacheAccessToken(String wxId, String token, long expire) {
        if (StringUtils.isEmpty(wxId) || StringUtils.isEmpty(token))
            return;
        redisTemplate.opsForValue().set(ACCESS_TOKEN_PREFIX + wxId, token, expire, TimeUnit.SECONDS);
    }

    public String getAccessToken(String wxId) {
        if (StringUtils.isEmpty(wxId))
            return null;
        Object result = redisTemplate.opsForValue().get(ACCESS_TOKEN_PREFIX + wxId);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }

    /****************************************************************/
    /***************** WxUser Userinfo  AccessToken 相关******************/
    /***************** 获取微信用户基本信息AccessToken,不同于其他AccessToken ******************/
    /***********************************************************/
    public void cacheWxUserInfoAccessToken(String openid, String accessToken) {
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(accessToken))
            return;
        redisTemplate.opsForValue().set(WX_USER_INFO_ACCESS_TOKEN_PREFIX + openid, accessToken, 6800L, TimeUnit.SECONDS);
    }

    public String getWxUserInfoAccessToken(String openid) {
        if (StringUtils.isEmpty(openid))
            return null;
        Object result = redisTemplate.opsForValue().get(WX_USER_INFO_ACCESS_TOKEN_PREFIX + openid);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }

    public void cacheWxUserInfoRefreshToken(String openid, String refreshToken) {
        if (StringUtils.isEmpty(openid) || StringUtils.isEmpty(refreshToken))
            return;
        redisTemplate.opsForValue().set(WX_USER_INFO_REFRESH_TOKEN_PREFIX + openid, refreshToken, 29 * 24 * 60 * 60L, TimeUnit.SECONDS);//设置29天失效
    }

    public String getWxUserInfoRefreshToken(String openid) {
        if (StringUtils.isEmpty(openid))
            return null;
        Object result = redisTemplate.opsForValue().get(WX_USER_INFO_REFRESH_TOKEN_PREFIX + openid);
        if (StringUtils.isEmpty(result)) {
            return null;
        } else {
            return result.toString();
        }
    }


    /***********************************************************/
    /****************** Product 产品相关*************************/
    /***********************************************************/
    public void cacheProductData(String productKey, String jsonData) {
        if (StringUtils.isEmpty(productKey) || jsonData == null)
            return;
        redisTemplate.opsForHash().put(PRODUCT_DATA_PREFIX, productKey, jsonData);
    }

    public boolean containsProductData(String productKey) {
        if (StringUtils.isEmpty(productKey)) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(PRODUCT_DATA_PREFIX, productKey);
    }

    public String getProductData(String productKey) {
        if (StringUtils.isEmpty(productKey)) {
            return null;
        }
        String result = null;
        if (containsProductData(productKey)) {
            result = redisTemplate.opsForHash().get(PRODUCT_DATA_PREFIX, productKey).toString();
        }
        if (StringUtils.isEmpty(result)) {
            //从数据库取
            Product product = productService.selectOne(new EntityWrapper<Product>().eq("gizwits_product_key", productKey));
            if (Objects.nonNull(product)) {
                result = JSONObject.toJSONString(product);
                cacheProductData(productKey, result);
            }
        }
        return result;
    }

    public boolean deleteProduct(String productKey) {
        if(!StringUtils.isEmpty(productKey)) {
            if(containsProductData(productKey)) {
                return Boolean.valueOf(redisTemplate.opsForHash().delete(PRODUCT_DATA_PREFIX, productKey).toString());
            }
        }

        return false;
    }

    /***********************************************************/
    /******************Device 设备在线相关***********************/
    /***********************************************************/
    public void cacheDeviceOnlineStatus(String productKey, String mac, Boolean onOffLineStatus) {
        if (StringUtils.isEmpty(productKey) || StringUtils.isEmpty(mac) || onOffLineStatus == null)
            return;
        redisTemplate.opsForHash().put(CURRENT_ONLINE_STATUS_PREFIX, productKey + "_" + mac, onOffLineStatus);
    }

    public Boolean getDeviceOnlineStatus(String productKey, String mac) {
        if (StringUtils.isEmpty(mac) || StringUtils.isEmpty(productKey))
            return false;
        if (redisTemplate.opsForHash().hasKey(CURRENT_ONLINE_STATUS_PREFIX, productKey + "_" + mac)) {
            return Boolean.valueOf(redisTemplate.opsForHash().get(CURRENT_ONLINE_STATUS_PREFIX, productKey + "_" + mac).toString());
        }
        return false;
    }

    /***********************************************************/
    /******************Device 设备实时状态***********************/
    /***********************************************************/
    public boolean containsDeviceCurrentStatusData(String productKey, String mac) {
        if (StringUtils.isEmpty(mac) || StringUtils.isEmpty(productKey))
            return false;
        return redisTemplate.opsForHash().hasKey(CURRENT_DATA_STATUS_PREFIX, productKey + "_" + mac);
    }

    public void cacheDeviceCurrentStatus(String productKey, String mac, JSONObject jsonObject) {
        if (StringUtils.isEmpty(mac) || jsonObject == null || StringUtils.isEmpty(productKey))
            return;
        jsonObject.put("status_time", new Date());
        redisTemplate.opsForHash().put(CURRENT_DATA_STATUS_PREFIX, productKey + "_" + mac, jsonObject);
    }

    public void cacheDevicePortCurrentStatus(String productKey, String macPort, JSONObject jsonObject) {
        if (StringUtils.isEmpty(macPort) || jsonObject == null || StringUtils.isEmpty(productKey))
            return;
        redisTemplate.opsForHash().put(CRRRENT_DATTA_STATUS_PORT, productKey + "_" + macPort, jsonObject);
    }

    public JSONObject getDeviceCurrentStatus(String productKey, String mac) {
        if (redisTemplate.opsForHash().hasKey(CURRENT_DATA_STATUS_PREFIX, productKey + "_" + mac)) {
            return JSONObject.parseObject(redisTemplate.opsForHash().get(CURRENT_DATA_STATUS_PREFIX, productKey + "_" + mac).toString());
        }
        return null;
    }
    public JSONObject getDevicePortCurrentStatus(String productKey, String macPort) {
        if (redisTemplate.opsForHash().hasKey(CRRRENT_DATTA_STATUS_PORT, productKey + "_" + macPort)) {
            return JSONObject.parseObject(redisTemplate.opsForHash().get(CRRRENT_DATTA_STATUS_PORT, productKey + "_" + macPort).toString());
        }
        return null;
    }

    /***********************************************************/
    /******************Order 订单相关****************************/
    /***********************************************************/
    public void cacheUsingOrder(String mac, JSONObject jsonObject) {
        if (StringUtils.isEmpty(mac) || null == jsonObject)
            return;
        redisTemplate.opsForHash().put(ORDER_STATUS_PREFIX, mac, jsonObject);
    }

    public JSONObject getUsingOrder(String mac) {
        if (StringUtils.isEmpty(mac))
            return null;
        if (redisTemplate.opsForHash().hasKey(ORDER_STATUS_PREFIX, mac)) {
            return JSONObject.parseObject(redisTemplate.opsForHash().get(ORDER_STATUS_PREFIX, mac).toString());
        }
        return null;
    }

    public boolean deleteOrderId(String mac) {
        if (redisTemplate.opsForHash().hasKey(ORDER_STATUS_PREFIX, mac)) {
            return Boolean.valueOf(redisTemplate.opsForHash().delete(ORDER_STATUS_PREFIX, mac).toString());
        }
        return false;
    }

    /***********************************************************/
    /****************** Weixin 配置相关****************************/
    /***********************************************************/
    public void cacheWxConfig(String wxIdOrId, String jsonObject) {
        if (StringUtils.isEmpty(wxIdOrId) || null == jsonObject)
            return;
        redisTemplate.opsForHash().put(WX_CONFIG_PREFIX, wxIdOrId, jsonObject);
    }

    public String getWxConfig(String wxIdOrId) {
        if (StringUtils.isEmpty(wxIdOrId)) {
            return null;
        }
        Object value = redisTemplate.opsForHash().get(WX_CONFIG_PREFIX, wxIdOrId);
        String result = null;
        if (Objects.nonNull(value)) {
            result = value.toString();
        }
        if (StringUtils.isEmpty(result)) {
            Wrapper<SysUserExt> wrapper = new EntityWrapper<>();
            if (NumberUtils.isDigits(wxIdOrId)) {
                wrapper.eq("sys_user_id", Integer.valueOf(wxIdOrId));
            } else {
                wrapper.eq("wx_id", wxIdOrId);
            }
            wrapper.eq("is_deleted",DeleteStatus.NOT_DELETED.getCode());
            SysUserExt ext = sysUserExtService.selectOne(wrapper);
            if (Objects.nonNull(ext)) {
                result = JSONObject.toJSONString(ext);
                cacheWxConfig(ext.getWxId(), result);
                cacheWxConfig(String.valueOf(ext.getSysUserId()), result);
            }
        }

        return result;
    }

    public boolean containWxConfig(String wxIdOrId) {
        if (StringUtils.isEmpty(wxIdOrId))
            return false;
        return redisTemplate.opsForHash().hasKey(WX_CONFIG_PREFIX, wxIdOrId);
    }

    public boolean deleteWxConfig(String wxIdOrId) {
        if(StringUtils.isEmpty(wxIdOrId)) {
            return false;
        }
        if(containWxConfig(wxIdOrId)) {
            return Boolean.valueOf(redisTemplate.opsForHash().delete(WX_CONFIG_PREFIX, wxIdOrId).toString());
        }
        return false;
    }
}
