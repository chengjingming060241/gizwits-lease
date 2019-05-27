package com.gizwits.lease.product.service;

import com.gizwits.lease.enums.ProductOperateType;

/**
 * @author lilh
 * @date 2017/7/21 18:59
 */
public interface ProductDeviceChangeService {

    void publishChangeEvent(Integer productId, ProductOperateType operateType);

    void publishChangeEvent(String deviceSno, ProductOperateType operateType);
}
