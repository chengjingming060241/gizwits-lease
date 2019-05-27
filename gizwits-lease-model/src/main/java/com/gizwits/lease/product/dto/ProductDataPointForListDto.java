package com.gizwits.lease.product.dto;

import java.util.List;

/**
 * Dto - 添加产品时数据点数据
 *
 * @author lilh
 * @date 2017/7/19 14:28
 */
public class ProductDataPointForListDto {

    private List<ProductDataPointDto> dataPoints;

    private List<ProductDataPointDto> writableDataPoints;

    public List<ProductDataPointDto> getDataPoints() {
        return dataPoints;
    }

    public void setDataPoints(List<ProductDataPointDto> dataPoints) {
        this.dataPoints = dataPoints;
    }

    public List<ProductDataPointDto> getWritableDataPoints() {
        return writableDataPoints;
    }

    public void setWritableDataPoints(List<ProductDataPointDto> writableDataPoints) {
        this.writableDataPoints = writableDataPoints;
    }
}
