package com.gizwits.lease.product.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.enums.ProductOperateType;
import com.gizwits.lease.product.dto.GizwitsDataPointReqDto;
import com.gizwits.lease.product.dto.PreProductDto;
import com.gizwits.lease.product.dto.ProductDataPointForListDto;
import com.gizwits.lease.product.dto.ProductDto;
import com.gizwits.lease.product.dto.ProductForAddDto;
import com.gizwits.lease.product.dto.ProductForDetailDto;
import com.gizwits.lease.product.dto.ProductForListDto;
import com.gizwits.lease.product.dto.ProductForUpdateDto;
import com.gizwits.lease.product.dto.ProductQueryDto;
import com.gizwits.lease.product.entity.Product;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface ProductService extends IService<Product> {

    /**
     * 添加
     */
    boolean add(ProductDto productDto);

    /**
     * 添加
     */
    Integer add(ProductForAddDto dto);

    /**
     * 获取录入产品页面的数据
     */
    PreProductDto getAddProductPageData();

    /**
     * 详情
     */
    ProductForDetailDto detail(Integer id);

    /**
     * 逻辑删除
     */
    String delete(List<Integer> ids);


    Product getProductByProductId(Integer productId);

    /**
     * 获取所有可用的产品
     */
    List<Product> getAllUseableProduct();


    /**
     * 获取有权限的产品
     */
    List<Product> getProductsWithPermission();

    /**
     * 列表
     */
    Page<ProductForListDto> page(Pageable<ProductQueryDto> pageable);

    /**
     * 数据点同步
     */
    ProductDataPointForListDto sync(GizwitsDataPointReqDto req);


    /**
     * 更新
     */
    boolean update(ProductForUpdateDto dto);


    /**
     * 发布修改事件
     */
    void publishChangeEvent(Integer productId, ProductOperateType operateType);

    Product getProductByDeviceSno(String sno);


    Integer resolveManufacturerAccount(SysUser current);
}
