package com.gizwits.lease.product.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.product.entity.ProductToProperties;
import com.gizwits.lease.product.service.ProductToPropertiesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 产品属性关系表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-19
 */
@RestController
@RequestMapping("/product/productToProperties")
@Api(description = "产品属性值接口,配置到具体产品的属性")
public class ProductToPropertiesController extends BaseController{
    @Autowired
    private ProductToPropertiesService productToPropertiesService;

    @ApiOperation(value = "添加", notes = "添加",consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody ResponseObject add(@RequestBody @Valid RequestObject<ProductToProperties> requestObject){
        productToPropertiesService.insert(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "分页查询", notes = "分页查询",consumes = "application/json")
    @RequestMapping(value = "/getListByPage",method = RequestMethod.POST)
    public @ResponseBody ResponseObject getListByPage(@RequestBody  @Valid RequestObject<Page<ProductToProperties>> requestObject ){
        EntityWrapper<ProductToProperties> entityWrapper = new EntityWrapper<ProductToProperties>();
        Page<ProductToProperties>  page = requestObject.getData();
        entityWrapper.orderBy(page.getOrderByField(),page.isAsc());
        Page<ProductToProperties> selectPage= productToPropertiesService.selectPage(page,entityWrapper);
        return success(selectPage);
    }

    @ApiOperation(value = "更新", notes = "更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody ResponseObject update(@RequestBody ProductToProperties ProductToProperties){
        ProductToProperties.setUtime(new Date());
        productToPropertiesService.updateById(ProductToProperties);
        return success();
    }

    @ApiOperation(value = "删除", notes = "删除",consumes = "application/json")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public @ResponseBody ResponseObject delete(@RequestBody List<Integer> ids){
        productToPropertiesService.deleteBatchIds(ids);
        return success();
    }
}
