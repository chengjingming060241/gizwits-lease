package com.gizwits.lease.listener;

import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.device.entity.dto.DeviceAssignRecordDto;
import com.gizwits.lease.enums.ArchiveType;
import com.gizwits.lease.event.DeviceAssignEvent;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 解绑和分配时更新设备产生的订单状态，设置为归档
 * <p>
 * <p>
 *      在设备详情中的订单列表只查询未归档的订单
 * </p>
 *
 * @author lilh
 * @date 2017/8/25 18:29
 */
@Component
public class UpdateOrderAfterUnbindDeviceListener implements ApplicationListener<DeviceAssignEvent> {

    @Autowired
    private OrderBaseService orderBaseService;

    @Async
    @Override
    public void onApplicationEvent(DeviceAssignEvent event) {
        List<DeviceAssignRecordDto> records = event.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<String> snos = records.stream().map(DeviceAssignRecordDto::getSno).collect(Collectors.toList());
            List<OrderBase> orders = orderBaseService.selectList(new EntityWrapper<OrderBase>().in("sno", snos).eq("is_archive", ArchiveType.UNARCHIVE.getCode()));
            if (CollectionUtils.isNotEmpty(orders)) {
                orders.forEach(item -> item.setIsArchive(ArchiveType.ARCHIVED.getCode()));
                orderBaseService.updateBatchById(orders);
            }
        }
    }
}
