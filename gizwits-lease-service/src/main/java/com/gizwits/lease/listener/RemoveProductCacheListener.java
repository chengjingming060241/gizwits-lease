package com.gizwits.lease.listener;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.gizwits.lease.constant.StatusCommandType;
import com.gizwits.lease.event.ProductUpdatedEvent;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductDataPointService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.vo.ProductCommandVO;
import com.gizwits.lease.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 清除产品的缓存数据
 *
 * @author lilh
 * @date 2017/8/16 17:31
 */
@Component
public class RemoveProductCacheListener implements ApplicationListener<ProductUpdatedEvent> {

    @Autowired
    private RedisService redisService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Autowired
    private ProductDataPointService productDataPointService;

    @Async
    @Override
    public void onApplicationEvent(ProductUpdatedEvent event) {
        redisService.deleteProduct(event.getProductKey());
        redisService.deleteProductMonit(event.getProductKey());
        Arrays.stream(StatusCommandType.values()).forEach(item -> redisService.deleteProductStatusCommand(event.getProductKey(), item.getCode()));

        loadAndCacheProduct();
        loadAndCacheCommandConfig();
        loadAndCacheProductMonitPoint();
    }

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
