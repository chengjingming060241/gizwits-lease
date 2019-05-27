package com.gizwits.lease.product.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 指令查询
 *
 * @author lilh
 * @date 2017/7/20 11:14
 */
public class ProductCommandConfigForQueryDto {

    /** 产品id */
    @NotNull
    private Integer productId;

    /** 指令类型 */
    @NotBlank
    private String commandType;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }
}
