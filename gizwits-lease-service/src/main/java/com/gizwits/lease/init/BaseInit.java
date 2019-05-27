package com.gizwits.lease.init;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.vo.ProductCommandVO;
import com.gizwits.lease.redis.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 系统启动时加载最新数据到缓存中
 * Created by zhl on 2017/7/11.
 */
@Component
public class BaseInit implements ApplicationListener<ApplicationReadyEvent> {

    private final static Logger logger = LoggerFactory.getLogger("WEIXIN_LOGGER");

    @Autowired
    private ProductService productService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private ProductDataPointService productDataPointService;

    @Autowired
    private RedisService redisService;


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("应用启动完成,初始数据到缓存当中");
        loadAndCacheProduct();
        loadAndCacheWxConfig();
        loadAndCacheCommandConfig();
        loadAndCacheProductMonitPoint();
    }

    // TODO: 2017/7/12 用户修改产品配置或者微信配置,需要立马更新缓存中相应的配置项

    /**
     * 缓存产品
     */
    private void loadAndCacheProduct(){
        List<Product> list = productService.getAllUseableProduct();
        if(list!=null&&list.size()>0){
            for(Product product:list){
                redisService.cacheProductData(product.getGizwitsProductKey(), JSONObject.toJSONString(product));
            }
        }
    }

    /**
     * 缓存微信公众号相关
     */
    private void loadAndCacheWxConfig(){
        List<SysUserExt> list = sysUserExtService.selectList(new EntityWrapper<>());
        if (list!=null&&list.size()>0){
            for (SysUserExt sysUserExt:list){
                redisService.cacheWxConfig(sysUserExt.getWxId(),JSONObject.toJSONString(sysUserExt));
                redisService.cacheWxConfig(sysUserExt.getSysUserId()+"",JSONObject.toJSONString(sysUserExt));
            }
        }
    }

    /**
     * 缓存所有的产品状态指令
     */
    private void loadAndCacheCommandConfig(){
        List<ProductCommandVO> list = productCommandConfigService.getAllUseableConfig();
        if(list!=null&&list.size()>0){
            for(ProductCommandVO commandConfig:list){
                redisService.cacheProductStatusCommand(commandConfig.getProductKey(),commandConfig.getStatusCommandType(),commandConfig.getCommand());
            }
        }
    }

    /**
     * 缓存产品监控的数据点
     */
    private void loadAndCacheProductMonitPoint(){
        List<Product> productList = productService.getAllUseableProduct();
        if(productList!=null && productList.size()>0){
            for(Product product:productList){
                List<ProductDataPoint> pointList = productDataPointService.getMonitDataPoint(product.getGizwitsProductKey());
                if(pointList!=null&&pointList.size()>0){
                    StringBuffer cachePointIdentify = new StringBuffer();
                    for(ProductDataPoint point:pointList){
                        cachePointIdentify.append(point.getIdentityName()).append(",");
                    }
                    redisService.cacheProductMonitPoint(product.getGizwitsProductKey(),cachePointIdentify.toString());
                }
            }
        }

    }
}
