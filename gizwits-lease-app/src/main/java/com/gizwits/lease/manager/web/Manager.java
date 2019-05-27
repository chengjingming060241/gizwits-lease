package com.gizwits.lease.manager.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.vo.DeviceAlarmListVo;
import com.gizwits.lease.device.vo.DeviceLaunchAreaCountVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/9/6.
 */
@RestController
@EnableSwagger2
@Api(description = "产品管理端接口")
@RequestMapping(value = "/app/manager/manager")
public class Manager extends BaseController{

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "投放点列表",notes = "投放点列表",consumes = "application/json")
    @PostMapping("/launchList")
    public ResponseObject<List<DeviceLaunchAreaCountVo>> launchList(){
        return success(deviceLaunchAreaService.areaListManager());
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "故障列表",notes = "故障列表",consumes = "application/json")
    @PostMapping("/alarmList")
    public ResponseObject<Page<DeviceAlarmListVo>> alarmList(@RequestBody @Valid RequestObject<Pageable<String>> requestObject){

        return success(deviceAlarmService.deviceAlarmListManager(requestObject.getData()));
    }
}
