package com.gizwits.lease.app.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.device.entity.dto.DeviceAlarmAppListDto;
import com.gizwits.lease.device.entity.dto.DeviceAlarmDetailDto;
import com.gizwits.lease.device.entity.dto.DeviceAlarmListPageQueryDto;
import com.gizwits.lease.device.service.DeviceAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * Created by xian on 2/9/2017.
 */
@EnableSwagger2
@Api(description = "提供设备故障接口")
@RestController
@RequestMapping("/app/deviceAlarm")
public class DeviceAlarmController extends BaseController {

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "App端分页列表", notes = "App端分页列表", consumes = "application/son")
    @RequestMapping(value = "/appList", method = RequestMethod.POST)
    public ResponseObject<Page> appList(@RequestBody @Valid RequestObject<DeviceAlarmAppListDto> requestObject) {
        Page page = deviceAlarmService.getAppPage(requestObject.getData());
        return success(page);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "App端故障详情", notes = "故障详情", consumes = "application/json")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseObject<DeviceAlarmDetailDto> deviceAlramDetail(
            @RequestBody @Valid ResponseObject<Pageable<DeviceAlarmListPageQueryDto>> responseObject) {
        Pageable<DeviceAlarmListPageQueryDto> pageable = responseObject.getData();
        DeviceAlarmListPageQueryDto deviceAlarmListPageDto = pageable.getQuery();
        Integer deviceAlarmId = deviceAlarmListPageDto.getDeviceAlarmId();
        return success(deviceAlarmService.getDeviceAlramInfoById(pageable, deviceAlarmId));
    }
}
