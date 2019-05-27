package com.gizwits.lease.product.service;




import com.gizwits.boot.base.RequestObject;
import com.gizwits.lease.product.entity.ProductServiceMode;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.product.dto.ProductServiceListQuerytDto;
import com.gizwits.lease.product.dto.ProductServiceModeForAddPageDto;
import com.gizwits.lease.product.dto.ProductServicecModeListDto;
import com.gizwits.lease.product.dto.ServiceTypeDto;

import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>
 * 产品(或者设备)服务方式 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
public interface ProductServiceModeService extends IService<ProductServiceMode> {


    /**
     * 通过产品id查询服务类型
     * @param ProductId
     * @return
     */
     List<ServiceTypeDto> getServiceTypeByProductId(Integer ProductId);

    /**
     * 通过id删除服务方式
     * @param ProductServiceModeIds
     * @return
     */
    String deleteProductServiceModeById(List<Integer> ProductServiceModeIds);

    /**
     * 获取收费模式的单位
     * @param serviceMode
     * @return
     */
    String getServiceModeUnit(ProductServiceMode serviceMode);


    /**
     * 添加收费模式页面数据
     */
    List<ProductServiceModeForAddPageDto> getAddServiceModePageData();

    /**
     * 判断该收费模式是否存在
     * @param serviceMode
     * @return
     */
     boolean judgeProductServiceModeIsExist(String serviceMode);

    /**
     *  更新收费模式
     * @param productServicecModeListDto
     * @param sysuser
     */
     void updateProductServiceMode(ProductServicecModeListDto productServicecModeListDto,SysUser sysuser);

    /**
     *  增加收费模式
     * @param sysUser
     * @param productServiceModeListDto
     */
     void addProductServiceMode(SysUser sysUser, ProductServicecModeListDto productServiceModeListDto);

    /**
     * 分页查询
     * @param pageable
     * @return
     */
     Page<ProductServicecModeListDto> getProductServiceModeListPage(Pageable<ProductServiceListQuerytDto> pageable);

    /***
     *
     * @param pageable
     * @return
     */
    Page<ProductServicecModeListDto> getProductServicecModeListDtoPage(Pageable<ProductServiceListQuerytDto> pageable);


    /**
     * 查询收费模式详情
     * ***/
    ProductServicecModeListDto getProductServiceModeDetail(Integer user_id, Integer serviceModeId);

    ProductServiceMode getProductServiceMode(Integer serviceModeId);
}
