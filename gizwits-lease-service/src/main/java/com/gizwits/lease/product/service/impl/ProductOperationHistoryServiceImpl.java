package com.gizwits.lease.product.service.impl;

import java.util.ArrayList;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.enums.ProductOperateType;
import com.gizwits.lease.product.dao.ProductOperationHistoryDao;
import com.gizwits.lease.product.dto.ProductOperationHistoryForListDto;
import com.gizwits.lease.product.dto.ProductOperationHistoryForQueryDto;
import com.gizwits.lease.product.entity.ProductOperationHistory;
import com.gizwits.lease.product.service.ProductOperationHistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品操作记录 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-07-20
 */
@Service
public class ProductOperationHistoryServiceImpl extends ServiceImpl<ProductOperationHistoryDao, ProductOperationHistory> implements ProductOperationHistoryService {

    @Override
    public Page<ProductOperationHistoryForListDto> list(Pageable<ProductOperationHistoryForQueryDto> query) {
        Page<ProductOperationHistory> page = new Page<>();
        BeanUtils.copyProperties(query, page);
        Wrapper<ProductOperationHistory> wrapper = new EntityWrapper<>();
        wrapper.orderBy("ctime", false);
        Page<ProductOperationHistory> selectPage = selectPage(page, QueryResolverUtils.parse(query.getQuery(), wrapper));
        Page<ProductOperationHistoryForListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
        selectPage.getRecords().forEach(item -> {
            ProductOperationHistoryForListDto dto = new ProductOperationHistoryForListDto(item);
            dto.setContent(ProductOperateType.getDesc(item.getOperateType()));
            result.getRecords().add(dto);
        });
        return result;
    }
}
