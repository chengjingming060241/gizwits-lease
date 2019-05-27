package com.gizwits.lease.device.web;

import java.util.List;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.device.service.DeviceMapService;
import com.gizwits.lease.device.vo.DeviceMapDetailDto;
import com.gizwits.lease.device.vo.DeviceMapDto;
import com.gizwits.lease.device.vo.DeviceMapQueryDto;
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
 * Controller - 设备地图
 *
 * @author lilh
 * @date 2017/7/27 15:31
 */
@Api("设备地图")
@EnableSwagger2
@RestController
@RequestMapping("/device/map")
public class DeviceMapController extends BaseController {

    @Autowired
    private DeviceMapService deviceMapService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<List<DeviceMapDto>> list(@RequestBody RequestObject<DeviceMapQueryDto> requestObject) {
        return success(deviceMapService.list(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<DeviceMapDetailDto> detail(@RequestBody RequestObject<Integer> requestObject) {
        return success(deviceMapService.detail(requestObject.getData()));
    }
}
