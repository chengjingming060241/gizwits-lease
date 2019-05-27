package com.gizwits.lease.product.web;

import java.util.List;

import javax.validation.Valid;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.product.dto.ProductCommandConfigForAddDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForDetailDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForQueryDto;
import com.gizwits.lease.product.dto.ProductCommandConfigForUpdateDto;
import com.gizwits.lease.product.service.ProductCommandConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author zhl
 * @since 2017-07-14
 */
@EnableSwagger2
@Api("指令")
@RestController
@RequestMapping("/product/productCommandConfig")
public class ProductCommandConfigController extends BaseController {

    @Autowired
    private ProductCommandConfigService productCommandConfigService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "查询", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<List<ProductCommandConfigForDetailDto>> list(@RequestBody @Valid RequestObject<ProductCommandConfigForQueryDto> requestObject) {
        return success(productCommandConfigService.list(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", consumes = "application/json")
    @PostMapping("/delete")
    public ResponseObject delete(@RequestBody @Valid RequestObject<Integer> requestObject) {
        productCommandConfigService.delete(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", consumes = "application/json")
    @PostMapping("/update")
    public ResponseObject update(@RequestBody @Valid RequestObject<ProductCommandConfigForUpdateDto> requestObject) {
        return success(productCommandConfigService.update(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", consumes = "application/json")
    @PostMapping("/add")
    public ResponseObject<ProductCommandConfigForDetailDto> add(@RequestBody RequestObject<ProductCommandConfigForAddDto> requestObject) {
        return success(new ProductCommandConfigForDetailDto(productCommandConfigService.add(requestObject.getData())));
    }
}
