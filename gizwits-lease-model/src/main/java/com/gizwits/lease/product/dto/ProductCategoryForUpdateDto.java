package com.gizwits.lease.product.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 品类更新
 *
 * @author lilh
 * @date 2017/7/18 14:39
 */
public class ProductCategoryForUpdateDto {

    @NotNull
    private Integer id;

    @NotBlank
    private String name;

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
