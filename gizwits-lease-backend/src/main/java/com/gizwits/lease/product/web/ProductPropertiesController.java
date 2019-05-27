package com.gizwits.lease.product.web;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.product.dto.ProductPropertiesDto;
import com.gizwits.lease.product.entity.ProductProperties;
import com.gizwits.lease.product.service.ProductPropertiesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 产品属性定义表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-19
 */
@RestController
@RequestMapping("/product/productProperties")
@Api(description = "产品属性接口,动态添加产品属性")
public class ProductPropertiesController extends BaseController {

    @Autowired
    private ProductPropertiesService productPropertiesService;

    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseObject add(@RequestBody @Valid RequestObject<ProductPropertiesDto> requestObject) {
        checkValid(requestObject.getData());
        productPropertiesService.add(requestObject.getData());
        return success();
    }


    @ApiOperation(value = "查看", notes = "查看", consumes = "application/json")
    @GetMapping("/{id}")
    public ResponseObject view(@PathVariable Integer id) {
        return success(productPropertiesService.view(id));
    }

    @ApiOperation(value = "根据产品类型获取属性", consumes = "application/json")
    @GetMapping("/category/{categoryId}")
    public ResponseObject list(@PathVariable Integer categoryId) {
        return success(productPropertiesService.getProductPropertiesByCategoryId(categoryId));
    }

    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @RequestMapping(value = "/getListByPage", method = RequestMethod.POST)
    public ResponseObject getListByPage(@RequestBody @Valid RequestObject<Page<ProductProperties>> requestObject) {
        return success(productPropertiesService.getProductPropertiesByPage(requestObject.getData()));
    }

    @ApiOperation(value = "更新", notes = "更新", consumes = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseObject update(@RequestBody ProductProperties ProductProperties) {
        ProductProperties.setUtime(new Date());
        productPropertiesService.updateById(ProductProperties);
        return success();
    }

    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseObject delete(@RequestBody List<Integer> ids) {
        productPropertiesService.deleteBatchIds(ids);
        return success();
    }

    private void checkValid(ProductPropertiesDto data) {
        if (Objects.equals(data.getIsSelectValue(), 1) && CollectionUtils.isEmpty(data.getValues())) {
            throw new IllegalArgumentException("请输入可选的值");
        }
    }
}
