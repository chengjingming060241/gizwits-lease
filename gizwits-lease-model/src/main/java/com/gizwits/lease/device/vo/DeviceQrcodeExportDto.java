package com.gizwits.lease.device.vo;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Dto - 设备二维码导出
 *
 * @author lilh
 * @date 2017/8/30 13:51
 */
public class DeviceQrcodeExportDto {

    /** 产品id */
    @NotNull
    private Integer productId;

    /** 数量 */
    @Min(1)
    @Max(1000)
    @NotNull
    private Integer count;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
