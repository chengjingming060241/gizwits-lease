package com.gizwits.lease.product.web;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.service.UserDeviceService;
import com.gizwits.lease.product.dto.ProductServiceListQuerytDto;
import com.gizwits.lease.product.dto.ProductServiceModeForAddPageDto;
import com.gizwits.lease.product.dto.ProductServicecModeListDto;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.product.service.ProductServiceDetailService;
import com.gizwits.lease.product.service.ProductServiceModeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 产品(或者设备)服务方式 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@RestController
@EnableSwagger2
@Api(description = "消费模式接口")
@RequestMapping("/product/productServiceMode")
public class ProductServiceModeController extends BaseController{
    private final static Logger logger = LoggerFactory.getLogger("PODUCT_LOGGER");

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductServiceDetailService productServiceDetailService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject<Page<ProductServicecModeListDto>> list(@RequestBody @Valid RequestObject<Pageable<ProductServiceListQuerytDto>> requestObject){
        SysUser sysUser = sysUserService.getCurrentUser();
        Integer userId = sysUser.getId();
        logger.info("sys_user_id"+userId+",查询条件：收费模式名称："+requestObject.getData());
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
        logger.info("user_id" + userId + ",查询条件：收费模式id：" + service_mode_id);
        if (ParamUtil.isNullOrEmptyOrZero(service_mode_id)) {
            return ResponseObject.ok(null);
        }

        return success(productServiceModeService.getProductServiceModeDetail(userId, service_mode_id));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加收费模式页面所需数据", notes = "添加收费模式页面所需数据", consumes = "application/json")
    @RequestMapping(value = "/AddServiceModePageData",method = RequestMethod.POST)
    public ResponseObject<List<ProductServiceModeForAddPageDto>> AddServiceModePageData() {
        logger.info("  添加收费模式所需数据");
        return success(productServiceModeService.getAddServiceModePageData());
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject add(@RequestBody @Valid RequestObject<ProductServicecModeListDto> requestObject){
        SysUser sysUser = sysUserService.getCurrentUser();

        ProductServicecModeListDto productServiceModeListDto = requestObject.getData();
        logger.info("添加收费模式：{}", productServiceModeListDto.toString());
        productServiceModeService.addProductServiceMode(sysUser, productServiceModeListDto);

        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<String> delete(@RequestBody @Valid RequestObject<List<Integer>> requestObject) {
        List<Integer> productServiceModeIds = requestObject.getData();
        logger.info("根据收费模式id批量删除：" + productServiceModeIds.toString());
       String str = productServiceModeService.deleteProductServiceModeById(productServiceModeIds);
        return success(str);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新",notes = "更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject update(@RequestBody @Valid RequestObject<ProductServicecModeListDto> requestObject){
        ProductServicecModeListDto productServicecModeListDto = requestObject.getData();
        SysUser sysUser = sysUserService.getCurrentUser();
        logger.info(" 更新收费模式：{}", productServicecModeListDto.toString());
        productServiceModeService.updateProductServiceMode(productServicecModeListDto, sysUser);
        return success();
    }



    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "收费模式是否存在",notes = "收费模式是否存在",consumes = "application/json")
    @RequestMapping(value = "/isExist",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject isExist(@RequestBody @Valid RequestObject<String> requestObject) {
        logger.info("收费模式名称："+requestObject.getData()+",用户id："+sysUserService.getCurrentUser().getId());
        return success(productServiceModeService.judgeProductServiceModeIsExist(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除价格",notes = "删除价格",consumes = "application/json")
    @RequestMapping(value = "/deletePrice",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject deletePrice(@RequestBody @Valid RequestObject<List<Integer>> requestObject) {
        logger.info("价格详情id："+requestObject.getData().toString());
        List<Integer> ids = requestObject.getData();
        productServiceDetailService.deleteByIds(ids);
        return success();
    }

}

