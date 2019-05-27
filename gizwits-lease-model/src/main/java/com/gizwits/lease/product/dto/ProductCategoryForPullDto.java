package com.gizwits.lease.product.dto;

import com.gizwits.lease.product.entity.ProductCategory;

/**
 * Dto - 品类下拉列表
 *
 * @author lilh
 * @date 2017/7/19 11:03
 */
public class ProductCategoryForPullDto {

    private Integer id;

    private String name;

    public ProductCategoryForPullDto(ProductCategory productCategory) {
        this.id = productCategory.getId();
        this.name = productCategory.getName();
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
}
