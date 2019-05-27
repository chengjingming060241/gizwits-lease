package com.gizwits.lease.product.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 添加品类
 *
 * @author lilh
 * @date 2017/7/18 11:46
 */
public class ProductCategoryForAddDto {

    /** 品类名称 */
    @NotBlank
    private String name;

    /** 父品类 */
    private Integer parentCategoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
