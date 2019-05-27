package com.gizwits.lease.product.common;

import java.util.ArrayList;
import java.util.List;

import com.gizwits.boot.common.Translator;
import com.gizwits.lease.product.dto.ProductDto;
import com.gizwits.lease.product.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Translator - 产品详情
 *
 * @author lilh
 * @date 2017/6/30 18:15
 */
@Component
public class ProductDetailTranslator implements Translator<Product, ProductDto> {

    @Autowired
    private List<ProductDetailProcessor> productDetailProcessors = new ArrayList<>();

    @Override
    public ProductDto translate(Product product) {
        ProductDto productDto = new ProductDto(product);
        productDetailProcessors.forEach(processor -> processor.process(productDto));
        return productDto;
    }
}
