package com.gizwits.lease.product.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 收费模式详情Dto
 * Created by yinhui on 2017/7/13.
 */
public class ProductServiceModeDetailDto implements Serializable{
    private  ProductServiceModeDto productServiceModeDto;
    private List<PriceAndNumDto> priceAndNumDtoList;

    public ProductServiceModeDto getProductServiceModeDto() {
        return productServiceModeDto;
    }

    public void setProductServiceModeDto(ProductServiceModeDto productServiceModeDto) {
        this.productServiceModeDto = productServiceModeDto;
    }

    public List<PriceAndNumDto> getPriceAndNumDtoList() {
        return priceAndNumDtoList;
    }

    public void setPriceAndNumDtoList(List<PriceAndNumDto> priceAndNumDtoList) {
        this.priceAndNumDtoList = priceAndNumDtoList;
    }
}
