package com.gizwits.lease.app.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.product.dto.AppServiceModeDetailDto;
import com.gizwits.lease.product.dto.ProductServiceListQuerytDto;
import com.gizwits.lease.product.dto.ProductServiceModeForAddPageDto;
import com.gizwits.lease.product.dto.ProductServicecModeListDto;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import com.gizwits.lease.product.vo.AppProductServiceDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;
import java.util.Objects;

/**
 * Created by GaGi on 2017/8/3.
 */
@EnableSwagger2
@Api(description = "提供设备收费模式")
@RestController
@RequestMapping("/app/product/service/mode")
public class ProductServiceModeController extends BaseController {

    @Autowired
    private ProductServiceDetailService productServiceDetailService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @ApiOperation(value = "提供设备对应的收费模式详情", notes = "获取收费模式详情")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/get", method = RequestMethod.POST)

    public    @ResponseBody   ResponseObject<AppProductServiceDetailVo> get(@RequestBody @Valid RequestObject<AppServiceModeDetailDto> requestObject) {
    
        AppProductServiceDetailVo vo = productServiceDetailService.getListForApp(requestObject.getData());
        return success(vo);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject<Page<ProductServicecModeListDto>> list(@RequestBody @Valid RequestObject<Pageable<ProductServiceListQuerytDto>> requestObject){
        SysUser sysUser = sysUserService.getCurrentUser();
        Integer userId = sysUser.getId();
        Pageable<ProductServiceListQuerytDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new ProductServiceListQuerytDto());
        }
        pageable.getQuery().setCreatorId(userId);
        Page<ProductServicecModeListDto> result = productServiceModeService.getProductServiceModeListPage(pageable);
        return success(result);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "收费模式详情", notes = "收费模式详情", consumes = "application/json")
    @RequestMapping(value = "/detailInfo",method = RequestMethod.POST)
    public ResponseObject<ProductServicecModeListDto> detail(@RequestBody @Valid RequestObject<Integer> requestObject){
        Integer service_mode_id = requestObject.getData();
        SysUser sysUser = sysUserService.getCurrentUser();
        Integer userId = sysUser.getId();
        if (ParamUtil.isNullOrEmptyOrZero(service_mode_id)) {
            return ResponseObject.ok(null);
        }

        return ResponseObject.ok(productServiceModeService.getProductServiceModeDetail(userId, service_mode_id));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加收费模式页面所需数据", notes = "添加收费模式页面所需数据", consumes = "application/json")
    @RequestMapping(value = "/AddServiceModePageData",method = RequestMethod.POST)
    public ResponseObject<List<ProductServiceModeForAddPageDto>> AddServiceModePageData() {
        return success(productServiceModeService.getAddServiceModePageData());
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject add(@RequestBody @Valid RequestObject<ProductServicecModeListDto> requestObject){
        SysUser sysUser = sysUserService.getCurrentUser();

        ProductServicecModeListDto productServiceModeListDto = requestObject.getData();
        productServiceModeService.addProductServiceMode(sysUser, productServiceModeListDto);

        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新",notes = "更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject update(@RequestBody @Valid RequestObject<ProductServicecModeListDto> requestObject){
        ProductServicecModeListDto productServicecModeListDto = requestObject.getData();
        SysUser sysUser = sysUserService.getCurrentUser();
        productServiceModeService.updateProductServiceMode(productServicecModeListDto, sysUser);
        return success();
    }
}
