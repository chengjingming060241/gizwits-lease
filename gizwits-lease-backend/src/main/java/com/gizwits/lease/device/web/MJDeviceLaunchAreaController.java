package com.gizwits.lease.device.web;

import java.util.List;

import javax.validation.Valid;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaForUnbindDto;
import com.gizwits.lease.device.entity.dto.MJDeviceLaunchAreaDetail;
import com.gizwits.lease.device.service.DeviceLaunchAreaAssignService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller - 投放点分配和收回
 *
 * @author lilh
 * @date 2017/9/4 14:46
 */
@Api(description = "投放点分配和收回")
@RestController
@RequestMapping("/device/deviceLaunchArea")
public class MJDeviceLaunchAreaController extends BaseController {

    @Autowired
    private DeviceLaunchAreaAssignService deviceLaunchAreaAssignService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "获取分配目标列表", consumes = "application/json")
    @PostMapping("/preAssign")
    public ResponseObject<List<AssignDestinationDto>> preAssign(@RequestBody RequestObject<Object> requestObject) {
        return success(deviceLaunchAreaAssignService.preAssign());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分配", consumes = "application/json")
    @PostMapping("/assign")
    public ResponseObject<Boolean> assign(@RequestBody @Valid RequestObject<DeviceLaunchAreaForAssignDto> requestObject) {
        return success(deviceLaunchAreaAssignService.assign(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "收回", consumes = "application/json")
    @PostMapping("/unbind")
    public ResponseObject<Boolean> unbind(@RequestBody @Valid RequestObject<DeviceLaunchAreaForUnbindDto> requestObject) {
        return success(deviceLaunchAreaAssignService.unbind(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = " 详情",notes = " 添详情加",consumes = "application/json")
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<MJDeviceLaunchAreaDetail> detail(@RequestBody @Valid RequestObject<Integer> requestObject){
        return success(deviceLaunchAreaService.detail(requestObject.getData()));
    }
}
