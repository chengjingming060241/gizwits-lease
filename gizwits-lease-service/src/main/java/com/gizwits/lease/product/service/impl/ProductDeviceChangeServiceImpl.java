package com.gizwits.lease.product.service.impl;

import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.enums.ProductOperateType;
import com.gizwits.lease.event.ProductDeviceChangeEvent;
import com.gizwits.lease.product.service.ProductDeviceChangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.gizwits.boot.utils.CommonEventPublisherUtils.publishEvent;

/**
 * Service - 产品和设备操作事件
 *
 * @author lilh
 * @date 2017/7/21 19:00
 */
@Service
public class ProductDeviceChangeServiceImpl implements ProductDeviceChangeService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void publishChangeEvent(Integer productId, ProductOperateType operateType) {
        publish(productId, null, operateType);
    }

    @Override
    public void publishChangeEvent(String deviceSno, ProductOperateType operateType) {
        publish(null, deviceSno, operateType);
    }

    private void publish(Integer productId, String deviceSno, ProductOperateType operateType) {
        ProductDeviceChangeEvent.Builder builder = ProductDeviceChangeEvent.Builder.create();
        builder.productId(productId)
                .deviceSno(deviceSno)
                .productOperateType(operateType)
                .operator(sysUserService.getCurrentUser());
        publishEvent(builder.build());
    }
}
