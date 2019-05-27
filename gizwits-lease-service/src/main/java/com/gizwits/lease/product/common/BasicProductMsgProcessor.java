package com.gizwits.lease.product.common;

import java.util.Objects;

import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.gizwits.lease.product.dto.ProductDto;
import com.gizwits.lease.product.entity.Brand;
import com.gizwits.lease.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Processor - 基本信息
 *
 * @author lilh
 * @date 2017/7/3 14:18
 */
@Order(5)
@Component
public class BasicProductMsgProcessor implements ProductDetailProcessor {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ManufacturerService manufacturerService;

    @Override
    public void process(ProductDto productDto) {
        resolveBrandName(productDto);
        resolveManufacturerName(productDto);
    }

    private void resolveBrandName(ProductDto productDto) {
        if (Objects.nonNull(productDto.getBrandId())) {
            Brand brand = brandService.selectById(productDto.getBrandId());
            if (Objects.nonNull(brand)) {
                productDto.setBrandName(brand.getName());
            }
        }
    }

    private void resolveManufacturerName(ProductDto productDto) {
        if (Objects.nonNull(productDto.getManufacturerId())) {
            Manufacturer manufacturer = manufacturerService.selectById(productDto.getManufacturerId());
            if (Objects.nonNull(manufacturer)) {
                productDto.setManufacturerName(manufacturer.getName());
            }
        }
    }
}
