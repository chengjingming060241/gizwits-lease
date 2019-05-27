package com.gizwits.lease.product.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.gizwits.lease.device.entity.dto.DeviceAlarmRankDto;
import com.gizwits.lease.product.dto.ProductDataPointQueryDto;
import com.gizwits.lease.product.dto.ProductdataPointUpdateDto;
import com.gizwits.lease.product.entity.ProductDataPoint;

/**
 * <p>
 * 产品数据点 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
public interface ProductDataPointService extends IService<ProductDataPoint> {

    Page<ProductDataPoint> getDataPointByPage(Integer productId, Page<ProductDataPoint> page);

    List<ProductDataPoint> getProdcutAllDataPoint(String productIdOrKey);

    Page<ProductDataPoint> sync(Integer productId);

    Page<DeviceAlarmRankDto> getDeviceAlarmRankDtoPage(Pageable<ProductDataPointQueryDto> pageable) ;

    ProductDataPoint getProductDataPointByProductIdAndIdentityName(Device device, DeviceAlarm deviceAlarm);

    List<ProductDataPoint> getMonitDataPoint(String productKey);

    /**
     * 列表
     */
    List<ProductDataPoint> list(Integer productId);

    /**
     * 更新数据点的告警级别
     * @param productdataPointUpdateDto
     */
    void updateProductDataPointByRank(ProductdataPointUpdateDto productdataPointUpdateDto);
}
