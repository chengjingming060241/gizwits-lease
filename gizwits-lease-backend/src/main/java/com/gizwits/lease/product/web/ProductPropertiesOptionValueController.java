package com.gizwits.lease.product.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.product.entity.ProductPropertiesOptionValue;
import com.gizwits.lease.product.service.ProductPropertiesOptionValueService;

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
 * 产品属性值表,提供选择 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-19
 */
@RestController
@RequestMapping("/product/productPropertiesOptionValue")
@Api(description = "产品可选属性值接口,提供选择的产品属性值")
public class ProductPropertiesOptionValueController extends BaseController {

    @Autowired
    private ProductPropertiesOptionValueService productPropertiesOptionValueService;

    @ApiOperation(value = "添加", notes = "添加",consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject add(@RequestBody @Valid RequestObject<ProductPropertiesOptionValue> requestObject){
        productPropertiesOptionValueService.insert(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "分页查询", notes = "分页查询",consumes = "application/json")
    @RequestMapping(value = "/getListByPage",method = RequestMethod.POST)
    public @ResponseBody ResponseObject getListByPage(@RequestBody  @Valid RequestObject<Page<ProductPropertiesOptionValue>> requestObject ){
        EntityWrapper<ProductPropertiesOptionValue> entityWrapper = new EntityWrapper<ProductPropertiesOptionValue>();
        Page<ProductPropertiesOptionValue>  page = requestObject.getData();
        entityWrapper.orderBy(page.getOrderByField(),page.isAsc());
        Page<ProductPropertiesOptionValue> selectPage= productPropertiesOptionValueService.selectPage(page,entityWrapper);
        return success(selectPage);
    }

    @ApiOperation(value = "更新", notes = "更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody ResponseObject update(@RequestBody ProductPropertiesOptionValue ProductPropertiesOptionValue){
        ProductPropertiesOptionValue.setUtime(new Date());
        productPropertiesOptionValueService.updateById(ProductPropertiesOptionValue);
        return success();
    }

    @ApiOperation(value = "删除", notes = "删除",consumes = "application/json")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public @ResponseBody ResponseObject delete(@RequestBody List<Integer> ids){
        productPropertiesOptionValueService.deleteBatchIds(ids);
        return success();
    }
}
