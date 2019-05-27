package com.gizwits.lease.listener;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.lease.event.ProductServiceCommandUpdateEvent;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.gizwits.lease.product.entity.ProductServiceMode;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by zhl on 2017/9/15.
 */
@Component
public class ProductServiceCommandUpdateListener implements ApplicationListener<ProductServiceCommandUpdateEvent> {

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @Autowired
    private ProductServiceDetailService productServiceDetailService;

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @Override
    public void onApplicationEvent(ProductServiceCommandUpdateEvent productServiceCommandUpdateEvent) {
        ProductCommandConfig productCommandConfig = productServiceCommandUpdateEvent.getCommandConfig();
        if (!productCommandConfig.getIsDeleted().equals(DeleteStatus.DELETED.getCode())){
            transforExistsServiceMode(productCommandConfig);
        }
    }

    private void transforExistsServiceMode(ProductCommandConfig commandConfig){
        List<ProductServiceMode> serviceModeList = productServiceModeService.selectList(new EntityWrapper<ProductServiceMode>().eq("product_id",commandConfig.getProductId()).eq("is_deleted",DeleteStatus.NOT_DELETED.getCode()));
        if(CollectionUtils.isNotEmpty(serviceModeList)){
            for(ProductServiceMode serviceMode:serviceModeList){
                serviceMode.setCommand(productCommandConfigService.getDeviceModelCommand(commandConfig));
                serviceMode.setUtime(new Date());
                productServiceModeService.updateById(serviceMode);
                transforExistsServiceDetail(serviceMode,commandConfig);
            }
        }
    }

    private void transforExistsServiceDetail(ProductServiceMode serviceMode, ProductCommandConfig commandConfig){
        List<ProductServiceDetail> detailList = productServiceDetailService.selectList(new EntityWrapper<ProductServiceDetail>());
        if(CollectionUtils.isNotEmpty(detailList)){
            for(ProductServiceDetail detail:detailList){
                detail.setCommand(productCommandConfigService.getCommandByConfig(commandConfig, detail.getNum()));//设置指令
                detail.setUtime(new Date());
                productServiceDetailService.updateById(detail);
            }
        }
    }
}
