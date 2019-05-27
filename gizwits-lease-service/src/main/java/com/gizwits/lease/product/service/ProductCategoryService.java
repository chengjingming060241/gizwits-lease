package com.gizwits.lease.product.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.product.dto.ProductCategoryForAddDto;
import com.gizwits.lease.product.dto.ProductCategoryForDetailDto;
import com.gizwits.lease.product.dto.ProductCategoryForListDto;
import com.gizwits.lease.product.dto.ProductCategoryForQueryDto;
import com.gizwits.lease.product.dto.ProductCategoryForUpdateDto;
import com.gizwits.lease.product.entity.ProductCategory;

/**
 * <p>
 * 产品类型 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface ProductCategoryService extends IService<ProductCategory> {

    /**
     * 添加
     */
    boolean add(ProductCategoryForAddDto dto);

    /**
     * 分布查询
     */
    Page<ProductCategoryForListDto> page(Pageable<ProductCategoryForQueryDto> queryPage);

    /**
     * 详情
     */
    ProductCategoryForDetailDto detail(Integer id);

    /**
     * 更新
     */
    boolean update(ProductCategoryForUpdateDto dto);

    /**
     * 品类列表
     */
    List<ProductCategory> list(SysUser current);

    String deleted(List<Integer> ids);
}
