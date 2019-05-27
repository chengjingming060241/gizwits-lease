package com.gizwits.lease.product.web;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.product.dto.GizwitsDataPointReqDto;
import com.gizwits.lease.product.dto.PreProductDto;
import com.gizwits.lease.product.dto.ProductDataPointForListDto;
import com.gizwits.lease.product.dto.ProductForAddDto;
import com.gizwits.lease.product.dto.ProductForDetailDto;
import com.gizwits.lease.product.dto.ProductForListDto;
import com.gizwits.lease.product.dto.ProductForPullDto;
import com.gizwits.lease.product.dto.ProductForUpdateDto;
import com.gizwits.lease.product.dto.ProductQueryDto;
import com.gizwits.lease.product.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 产品表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@RestController
@EnableSwagger2
@Api(description = "产品接口")
@RequestMapping("/product/product")
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @PostMapping(value = "/add")
    public ResponseObject add(@RequestBody @Valid RequestObject<ProductForAddDto> requestObject) {
        return success(productService.add(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "获取添加界面的数据", consumes = "application/json")
    @GetMapping(value = "/add")
    public ResponseObject<PreProductDto> add() {
        return success(productService.getAddProductPageData());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "查看", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<ProductForDetailDto> detail(@RequestBody RequestObject<Integer> requestObject) {
        return success(productService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @PostMapping(value = "/page")
    public ResponseObject<Page<ProductForListDto>> page(@RequestBody RequestObject<Pageable<ProductQueryDto>> requestObject) {
        Pageable<ProductQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new ProductQueryDto());
        }
        SysUser current = sysUserService.getCurrentUser();
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        Integer manufacturerAccountId = productService.resolveManufacturerAccount(current);
        if (Objects.nonNull(manufacturerAccountId)) {
            pageable.getQuery().setManufacturerAccountId(manufacturerAccountId);
        } else {
            pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(current));
        }
        return success(productService.page(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "产品下拉列表", consumes = "application/json")
    @PostMapping("/pull")
    public ResponseObject<List<ProductForPullDto>> pull() {
        return success(productService.getProductsWithPermission().stream().map(ProductForPullDto::new).collect(Collectors.toList()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "同步", consumes = "application/json")
    @PostMapping("/sync")
    public ResponseObject<ProductDataPointForListDto> sync(@RequestBody @Valid RequestObject<GizwitsDataPointReqDto> requestObject) {
        return success(productService.sync(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @PostMapping(value = "/delete")
    public ResponseObject<String> disable(@RequestBody RequestObject<List<Integer>> requestObject) {
        return success( productService.delete(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", consumes = "application/json")
    @PostMapping("/update")
    public ResponseObject update(@RequestBody @Valid RequestObject<ProductForUpdateDto> requestObject) {
        productService.update(requestObject.getData());
        return success();
    }
}
