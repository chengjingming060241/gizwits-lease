package com.gizwits.lease.product.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.CommonEventPublisherUtils;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.product.dao.ProductCategoryDao;
import com.gizwits.lease.product.dto.ProductCategoryForAddDto;
import com.gizwits.lease.product.dto.ProductCategoryForDetailDto;
import com.gizwits.lease.product.dto.ProductCategoryForListDto;
import com.gizwits.lease.product.dto.ProductCategoryForQueryDto;
import com.gizwits.lease.product.dto.ProductCategoryForUpdateDto;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.entity.ProductCategory;
import com.gizwits.lease.product.service.ProductCategoryService;
import com.gizwits.lease.product.service.ProductService;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 产品类型 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@Service
public class ProductCategoryServiceImpl extends ServiceImpl<ProductCategoryDao, ProductCategory> implements ProductCategoryService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductService productService;

    @Override
    public boolean add(ProductCategoryForAddDto dto) {
        SysUser current = sysUserService.getCurrentUser();
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCtime(new Date());
        productCategory.setUtime(productCategory.getCtime());
        productCategory.setName(dto.getName());
        productCategory.setParentCategoryId((Integer) ObjectUtils.defaultIfNull(dto.getParentCategoryId(), 0));
        productCategory.setSysUserId(current.getId());
        productCategory.setSysUserName(current.getUsername());
        return insert(productCategory);
    }

    @Override
    public Page<ProductCategoryForListDto> page(Pageable<ProductCategoryForQueryDto> queryPage) {
        Page<ProductCategory> page = new Page<>();
        queryPage.setOrderByField("'ctime'");
        queryPage.setAsc(false);
        BeanUtils.copyProperties(queryPage, page);
        Page<ProductCategory> selectPage = selectPage(page, QueryResolverUtils.parse(queryPage.getQuery(), new EntityWrapper<>()));
        Page<ProductCategoryForListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
        selectPage.getRecords().forEach(item -> {
            ProductCategoryForListDto tmp = new ProductCategoryForListDto(item);
            tmp.setProductCount(getProductByCategoryId(item.getId()));
            result.getRecords().add(tmp);
        });
        return result;
    }

    @Override
    public ProductCategoryForDetailDto detail(Integer id) {
        ProductCategory productCategory = resolveProductCategory(id);
        if (Objects.nonNull(productCategory)) {
            ProductCategoryForDetailDto result = new ProductCategoryForDetailDto(productCategory);
            result.setProductCount(getProductByCategoryId(id));
            return result;
        }
        return null;
    }

    private ProductCategory resolveProductCategory(Integer id) {
        Wrapper<ProductCategory> wrapper = new EntityWrapper<>();
        wrapper.in("sys_user_id", sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        wrapper.eq("id", id);
        return selectOne(wrapper);
    }

    @Override
    public boolean update(ProductCategoryForUpdateDto dto) {
        ProductCategory productCategory = resolveProductCategory(dto.getId());
        if (Objects.nonNull(productCategory)) {
            String oldName = productCategory.getName();
            if (!ParamUtil.isNullOrEmptyOrZero(dto.getName()) && !StringUtils.equals(dto.getName(), productCategory.getName())) {
                productCategory.setName(dto.getName());
                NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(productCategory, productCategory.getId(), oldName, dto.getName());
               publishEvent(nameModifyEvent);
                return updateById(productCategory);
            }
        }
        return false;
    }

    private void publishEvent(NameModifyEvent<Integer> nameModifyEvent) {
        CommonEventPublisherUtils.publishEvent(nameModifyEvent);
    }

    @Override
    public List<ProductCategory> list(SysUser current) {
        return selectList(new EntityWrapper<ProductCategory>().
                in("sys_user_id", sysUserService.resolveAccessableUserIds(current)));
    }


    private Integer getProductByCategoryId(Integer id) {
        return productService.selectCount(new EntityWrapper<Product>().eq("category_id", id).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
    }

    @Override
    public String deleted(List<Integer> ids) {
        SysUser current = sysUserService.getCurrentUser();
        List<Integer> userIds = sysUserService.resolveOwnerAccessableUserIds(current);
        List<String> fails = new LinkedList<>();
        StringBuilder sb = new StringBuilder();
        List<ProductCategory> productCategorys = selectList(new EntityWrapper<ProductCategory>().in("id", ids).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()));
        if (!ParamUtil.isNullOrEmptyOrZero(productCategorys)) {
            for (ProductCategory productCategory : productCategorys) {
                if (userIds.contains(productCategory.getSysUserId())) {
                    if (productService.selectCount(new EntityWrapper<Product>().eq("category_id", productCategory.getId()).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode())) <= 0) {
                        productCategory.setUtime(new Date());
                        productCategory.setIsDeleted(DeleteStatus.DELETED.getCode());
                        updateById(productCategory);
                    } else {
                        fails.add(productCategory.getName());
                    }
                } else {
                    fails.add(productCategory.getName());
                }
            }
        }
        switch (fails.size()) {
            case 0:
                sb.append("删除成功");
                break;
            case 1:
                sb.append("您欲删除的品类[" + fails.get(0) + "]已存在产品，请先删除该品类下的所有产品。");
                break;
            case 2:
                sb.append("您欲删除的品类[" + fails.get(0) + "],[" + fails.get(1) + "]已存在产品，请先删除该品类下的所有产品。");
                break;
            default:
                sb.append("您欲删除的品类[" + fails.get(0) + "],[" + fails.get(1) + "]等已存在产品，请先删除该品类下的所有产品。");
                break;
        }
        return sb.toString();
    }

}
