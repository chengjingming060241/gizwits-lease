package com.gizwits.lease.product.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.product.dto.ProductPropertiesDto;
import com.gizwits.lease.product.entity.ProductProperties;

/**
 * <p>
 * 产品属性定义表 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface ProductPropertiesService extends IService<ProductProperties> {

    /**
     * 添加
     */
    boolean add(ProductPropertiesDto productPropertiesDto);

    /**
     * 分页查询
     */
    Page<ProductPropertiesDto> getProductPropertiesByPage(Page<ProductProperties> page);

    /**
     * 查询
     */
    ProductPropertiesDto view(Integer id);

    /**
     * 根据产品类型查找
     */
    List<ProductPropertiesDto> getProductPropertiesByCategoryId(Integer categoryId);
}
