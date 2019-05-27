package com.gizwits.lease.product.service;

import com.gizwits.lease.product.dto.AppServiceModeDetailDto;
import com.gizwits.lease.product.dto.PriceAndNumDto;
import com.gizwits.lease.product.dto.ProductServiceDetailDto;
import com.gizwits.lease.product.entity.ProductServiceDetail;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.product.vo.AppProductServiceDetailVo;

import java.util.List;

/**
 * <p>
 *  产品(或者设备)收费价格详情服务
 * </p>
 *
 * @author yinhui
 * @since 2017-07-13
 */
public interface ProductServiceDetailService extends IService<ProductServiceDetail> {

    /**
     * 通过收费模式的id查询具体的收费详情
     * @param serviceModeId
     * @return
     */
    List<PriceAndNumDto> getProductPriceDetailByServiceModeId(Integer serviceModeId);

    List<ProductServiceDetail> getProductServiceDetailByServiceModelId(Integer serviceModeId);

    /**
     * 通过收费模式id删除
     * @param serviceModeId
     * @return
     */
     boolean deleteByServiceModeId(Integer serviceModeId);

    boolean deleteBatchByModeId(List<Integer> serviceModeIds);

    /**
     *  通过id删除收费详情
     * @param id
     * @return
     */
     boolean deleteByIds(List<Integer> id);

     boolean judgeProductServiceDetailIsExist(ProductServiceDetailDto productServiceDetailDto);

    /**
     * 获取热水最低单价
     * @param serviceModeId
     * @return
     */
    Double getMinPrice(Integer serviceModeId);

    /**
     * 获取常温的最低单价
     * @param serviceModeId
     * @return
     */
    Double getNormalMinPrice(Integer serviceModeId);

    /**
     * 获取冷水最低单价
     * @param serviceModeId
     * @return
     */
    Double getColdMinPrice(Integer serviceModeId);

    /**
     * 获取温开水最低单价
     * @param serviceModeId
     * @return
     */
    Double getWarmMinPrice(Integer serviceModeId);

    AppProductServiceDetailVo getListForApp(AppServiceModeDetailDto data);
}
