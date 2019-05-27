package com.gizwits.lease.product.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.product.dto.ProductOperationHistoryForListDto;
import com.gizwits.lease.product.dto.ProductOperationHistoryForQueryDto;
import com.gizwits.lease.product.service.ProductOperationHistoryService;
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
 * 产品操作记录 前端控制器
 * </p>
 *
 * @author lilh
 * @since 2017-07-20
 */
@Api("产品操作记录")
@EnableSwagger2
@RestController
@RequestMapping("/product/productOperationHistory")
public class ProductOperationHistoryController extends BaseController {

    @Autowired
    private ProductOperationHistoryService productOperationHistoryService;


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "查看", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<Page<ProductOperationHistoryForListDto>> list(@RequestBody RequestObject<Pageable<ProductOperationHistoryForQueryDto>> requestObject) {
        return success(productOperationHistoryService.list(requestObject.getData()));
    }
}
