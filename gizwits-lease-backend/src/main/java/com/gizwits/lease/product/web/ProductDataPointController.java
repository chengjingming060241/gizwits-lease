package com.gizwits.lease.product.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.PageDto;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.device.entity.dto.DeviceAlarmRankDto;
import com.gizwits.lease.product.dto.ProductDataPointQueryDto;
import com.gizwits.lease.product.dto.ProductdataPointUpdateDto;
import com.gizwits.lease.product.entity.ProductDataPoint;
import com.gizwits.lease.product.service.ProductDataPointService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * <p>
 * 产品数据点 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@EnableSwagger2
@Api(description = "数据点接口")
@RestController
@RequestMapping("/product/productDataPoint")
public class ProductDataPointController extends BaseController {

    @Autowired
    private ProductDataPointService productDataPointService;




    @ApiOperation(value = "产品数据点", consumes = "application/json")
    @PostMapping("/list/{productId}")
    public ResponseObject list(@PathVariable Integer productId, @RequestBody PageDto pageDto) {
        Page<ProductDataPoint> page = new Page<>();
        BeanUtils.copyProperties(pageDto, page);
        page.setCondition(pageDto.getCondition());
        return success(productDataPointService.getDataPointByPage(productId, page));
    }

    @ApiOperation(value = "同步数据点", consumes = "application/json")
    @GetMapping("/sync/{productId}")
    public ResponseObject sync(@PathVariable Integer productId) {
        return success(productDataPointService.sync(productId));
    }

    @ApiOperation(value = "测试系统配置", consumes = "application/json")
    @GetMapping("/test")
    public ResponseObject test() {
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        return success(commonSystemConfig.getEnterpriseApiHost());
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "告警级别分页列表",notes = "告警级别分页列表",consumes = "application/json")
    @RequestMapping(value = "/listPage",method = RequestMethod.POST)
    public ResponseObject listPage(@RequestBody @Valid RequestObject<Pageable<ProductDataPointQueryDto>> requestObject){
        Pageable<ProductDataPointQueryDto> pageable = requestObject.getData();
        Page<DeviceAlarmRankDto> result = productDataPointService.getDeviceAlarmRankDtoPage(pageable);
        return success(result );
    }


    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新告警级别",notes = "",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseObject update(@RequestBody @Valid RequestObject<ProductdataPointUpdateDto> requestObject){
        ProductdataPointUpdateDto productdataPointUpdateDto = requestObject.getData();
        productDataPointService.updateProductDataPointByRank(productdataPointUpdateDto);
        return success();
    }



}
