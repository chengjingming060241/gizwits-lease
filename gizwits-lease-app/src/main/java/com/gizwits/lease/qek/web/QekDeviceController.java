package com.gizwits.lease.qek.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.device.service.DeviceExtService;
import com.gizwits.lease.device.vo.DevicePortVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/24.
 */
@RestController
@EnableSwagger2
@Api(description = "沁尔康设备页面")
@RequestMapping(value = "/app/qek/device")
public class QekDeviceController extends BaseController{

    @Autowired
    private DeviceExtService deviceExtService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "设备", consumes = "application/json")
    @PostMapping("/port")
    public ResponseObject<DevicePortVo> port(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(deviceExtService.listDevicePort(requestObject.getData()));
    }
}
