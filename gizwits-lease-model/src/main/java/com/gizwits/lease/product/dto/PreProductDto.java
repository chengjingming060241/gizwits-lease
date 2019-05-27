package com.gizwits.lease.product.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dto - 产品录入的页面数据
 *
 * @author lilh
 * @date 2017/6/30 11:07
 */
public class PreProductDto {


    /** 品类下拉列表 */
    private List<ProductCategoryForPullDto> productCategories = new ArrayList<>();

    /** 厂商账号 */
    private List<ManufacturerUserDto> manufacturers = Collections.emptyList();


    public List<ProductCategoryForPullDto> getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(List<ProductCategoryForPullDto> productCategories) {
        this.productCategories = productCategories;
    }

    public List<ManufacturerUserDto> getManufacturers() {
        return manufacturers;
    }

    public void setManufacturers(List<ManufacturerUserDto> manufacturers) {
        this.manufacturers = manufacturers;
    }
}
