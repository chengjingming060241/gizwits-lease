package com.gizwits.lease.product.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.base.Constants;
import com.gizwits.lease.product.entity.Product;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 产品列表
 *
 * @author lilh
 * @date 2017/7/18 17:03
 */
public class ProductForListDto {

    /** 产品id */
    private Integer id;

    /** 产品名称 */
    private String name;

    /** 品类名称 */
    private String productCategoryName;

    /** product key */
    private String gizwitsProductKey;

    /** 设备数量 */
    private Integer deviceCount;

    /** 更新时间 */
    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    private Date utime;

    public ProductForListDto(Product product) {
        BeanUtils.copyProperties(product, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCategoryName() {
        return productCategoryName;
    }

    public void setProductCategoryName(String productCategoryName) {
        this.productCategoryName = productCategoryName;
    }

    public String getGizwitsProductKey() {
        return gizwitsProductKey;
    }

    public void setGizwitsProductKey(String gizwitsProductKey) {
        this.gizwitsProductKey = gizwitsProductKey;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }
}
