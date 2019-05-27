package com.gizwits.lease.product.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.product.dao.ProductPropertiesDao;
import com.gizwits.lease.product.dto.ProductPropertiesDto;
import com.gizwits.lease.product.entity.ProductProperties;
import com.gizwits.lease.product.entity.ProductPropertiesOptionValue;
import com.gizwits.lease.product.service.ProductPropertiesOptionValueService;
import com.gizwits.lease.product.service.ProductPropertiesService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * <p>
 * 产品属性定义表 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@Service
public class ProductPropertiesServiceImpl extends ServiceImpl<ProductPropertiesDao, ProductProperties> implements ProductPropertiesService {

    @Autowired
    private ProductPropertiesOptionValueService productPropertiesOptionValueService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ProductPropertiesDto productPropertiesDto) {
        preAdd(productPropertiesDto);
        return this.insert(copy(productPropertiesDto)) && addOptionValue(productPropertiesDto);
    }

    @Override
    public Page<ProductPropertiesDto> getProductPropertiesByPage(Page<ProductProperties> page) {
        EntityWrapper<ProductProperties> entityWrapper = new EntityWrapper<>();
        entityWrapper.orderBy(page.getOrderByField(), page.isAsc());
        return translate(this.selectPage(page, entityWrapper));
    }

    @Override
    public ProductPropertiesDto view(Integer id) {
        ProductProperties productProperties = super.selectById(id);
        Assert.notNull(productProperties);
        ProductPropertiesDto dto = new ProductPropertiesDto(productProperties);
        dto.setValues(getOptionValues(dto));
        return dto;
    }

    @Override
    public List<ProductPropertiesDto> getProductPropertiesByCategoryId(Integer categoryId) {
        return translate(selectList(new EntityWrapper<ProductProperties>().eq("category_id", categoryId)));
    }

    private Page<ProductPropertiesDto> translate(Page<ProductProperties> productPropertiesPage) {
        Page<ProductPropertiesDto> productPropertiesDtoPage = new Page<>();
        BeanUtils.copyProperties(productPropertiesPage, productPropertiesDtoPage);
        productPropertiesDtoPage.setRecords(translate(productPropertiesPage.getRecords()));
        return productPropertiesDtoPage;
    }

    private List<ProductPropertiesDto> translate(List<ProductProperties> productProperties) {
        List<ProductPropertiesDto> result = new ArrayList<>(productProperties.size());
        productProperties.forEach(item -> {
            ProductPropertiesDto dto = new ProductPropertiesDto(item);
            if (Objects.equals(dto.getIsSelectValue(), 1)) {
                dto.setValues(getOptionValues(dto));
            }
            result.add(dto);
        });

        return result;
    }

    private List<String> getOptionValues(ProductPropertiesDto dto) {
        List<ProductPropertiesOptionValue> optionValues = productPropertiesOptionValueService.selectList(new EntityWrapper<ProductPropertiesOptionValue>().eq("property_key", dto.getPropertyKey()));
        return optionValues.stream().map(ProductPropertiesOptionValue::getPropertyValue).collect(Collectors.toList());
    }

    private boolean addOptionValue(ProductPropertiesDto productPropertiesDto) {
        if (!hasOptionValues(productPropertiesDto)) {
            return true;
        }
        //构造保存product_properties_option_value
        List<ProductPropertiesOptionValue> optionValues = getProductPropertiesOptionValues(productPropertiesDto);
        return productPropertiesOptionValueService.insertBatch(optionValues);
    }

    private List<ProductPropertiesOptionValue> getProductPropertiesOptionValues(ProductPropertiesDto productPropertiesDto) {
        List<String> validValues = productPropertiesDto.getValues().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        List<ProductPropertiesOptionValue> optionValues = new ArrayList<>(validValues.size());
        validValues.forEach(value -> {
            ProductPropertiesOptionValue optionValue = new ProductPropertiesOptionValue();
            optionValue.setCtime(new Date());
            optionValue.setPropertyKey(productPropertiesDto.getPropertyKey());
            optionValue.setPropertyName(productPropertiesDto.getPropertyName());
            optionValue.setPropertyValue(value);
            optionValues.add(optionValue);
        });
        return optionValues;
    }

    private boolean hasOptionValues(ProductPropertiesDto productPropertiesDto) {
        return Objects.equals(productPropertiesDto.getIsSelectValue(), 1)
                && CollectionUtils.isNotEmpty(productPropertiesDto.getValues());
    }

    private void preAdd(ProductPropertiesDto productPropertiesDto) {
        productPropertiesDto.setCtime(new Date());
        productPropertiesDto.setUtime(productPropertiesDto.getCtime());
    }

    private ProductProperties copy(ProductPropertiesDto productPropertiesDto) {
        ProductProperties productProperties = new ProductProperties();
        BeanUtils.copyProperties(productPropertiesDto, productProperties);
        return productProperties;
    }

}
