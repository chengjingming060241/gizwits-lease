package com.gizwits.lease.product.dto;

/**
 * Dto - 数据点更新
 *
 * @author lilh
 * @date 2017/7/20 14:05
 */
public class ProductDataPointForUpdateDto {

    private Integer id;

    private Integer isMonit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsMonit() {
        return isMonit;
    }

    public void setIsMonit(Integer isMonit) {
        this.isMonit = isMonit;
    }
}
