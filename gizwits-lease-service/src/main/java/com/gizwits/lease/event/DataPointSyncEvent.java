package com.gizwits.lease.event;

import java.util.List;

import com.gizwits.lease.product.entity.ProductDataPoint;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 数据点同步事件
 *
 * @author lilh
 * @date 2017/7/5 11:15
 */
public class DataPointSyncEvent extends ApplicationEvent {


    public DataPointSyncEvent(Object source) {
        super(source);
    }

    @SuppressWarnings("unchecked")
    public List<ProductDataPoint> getDataPoints() {
        return (List<ProductDataPoint>) getSource();
    }
}
