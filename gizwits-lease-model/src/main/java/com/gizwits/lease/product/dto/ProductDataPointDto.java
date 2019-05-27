package com.gizwits.lease.product.dto;

import com.gizwits.lease.product.entity.ProductDataPoint;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 数据点
 *
 * @author lilh
 * @date 2017/7/19 14:28
 */
public class ProductDataPointDto {

    private Integer id;

    private String showName;

    private String identityName;

    private String remark;

    private String valueLimit;

    private String readWriteType;

    private String dataType;

    private Integer isMonit;


    public ProductDataPointDto(ProductDataPoint productDataPoint) {
        BeanUtils.copyProperties(productDataPoint, this);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getIdentityName() {
        return identityName;
    }

    public void setIdentityName(String identityName) {
        this.identityName = identityName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValueLimit() {
        return valueLimit;
    }

    public void setValueLimit(String valueLimit) {
        this.valueLimit = valueLimit;
    }

    public String getReadWriteType() {
        return readWriteType;
    }

    public void setReadWriteType(String readWriteType) {
        this.readWriteType = readWriteType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getIsMonit() {
        return isMonit;
    }

    public void setIsMonit(Integer isMonit) {
        this.isMonit = isMonit;
    }
}
