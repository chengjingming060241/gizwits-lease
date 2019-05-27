package com.gizwits.lease.product.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.product.dto.ProductDto;
import com.gizwits.lease.product.entity.ProductToProperties;
import com.gizwits.lease.product.service.ProductToPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author lilh
 * @date 2017/7/3 14:37
 */
@Order(10)
@Component
public class ProductPropertiesMsgProcessor implements ProductDetailProcessor {

    @Autowired
    private ProductToPropertiesService productToPropertiesService;

    @Override
    public void process(ProductDto productDto) {
        resolveProperties(productDto);
    }

    private void resolveProperties(ProductDto productDto) {
        productDto.setProperties(productToPropertiesService.selectList(new EntityWrapper<ProductToProperties>().eq("product_id", productDto.getId())));
    }
}
