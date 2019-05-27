package com.gizwits.lease.listener;

import java.util.Date;

import com.gizwits.lease.event.ProductDeviceChangeEvent;
import com.gizwits.lease.product.entity.ProductCommandConfig;
import com.gizwits.lease.product.entity.ProductOperationHistory;
import com.gizwits.lease.product.service.ProductOperationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 产品信息修改记录
 *
 * @author lilh
 * @date 2017/7/20 17:31
 */
@Component
public class ProductChangeApplicationListener implements ApplicationListener<ProductDeviceChangeEvent> {

    @Autowired
    private ProductOperationHistoryService productOperationHistoryService;

    @Override
    @Async
    public void onApplicationEvent(ProductDeviceChangeEvent event) {
        ProductOperationHistory history = resolve(event);
        productOperationHistoryService.insert(history);
    }

    private ProductOperationHistory resolve(ProductDeviceChangeEvent event) {
        ProductOperationHistory history = new ProductOperationHistory();
        history.setCtime(new Date());
        history.setOperateType(event.getOperateType().getCode());
        history.setIp(event.getIp());
        history.setProductId(event.getProductId());
        history.setDeviceSno(event.getDeviceSno());
        history.setSysUserId(event.getOperator().getId());
        history.setSysUserName(event.getOperator().getUsername());
        return history;
    }

}
