package com.gizwits.lease.product.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.product.dto.ProductOperationHistoryForListDto;
import com.gizwits.lease.product.dto.ProductOperationHistoryForQueryDto;
import com.gizwits.lease.product.entity.ProductOperationHistory;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 产品操作记录 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-07-20
 */
public interface ProductOperationHistoryService extends IService<ProductOperationHistory> {

    /**
     * 列表
     */
    Page<ProductOperationHistoryForListDto> list(Pageable<ProductOperationHistoryForQueryDto> query);
}
