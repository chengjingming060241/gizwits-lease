package com.gizwits.lease.device.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.entity.DeviceAlarm;

import com.gizwits.lease.device.entity.dto.*;

import com.gizwits.lease.device.entity.DeviceLaunchArea;

import com.gizwits.lease.device.entity.dto.DeviceAlarmListPageQueryDto;
import com.gizwits.lease.device.service.DeviceAlarmService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.device.service.UserDeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.product.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 设备故障(警告)记录表 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
@RestController
@EnableSwagger2
@Api(description = "设备故障接口")
@RequestMapping("/device/deviceAlarm")
public class DeviceAlarmController extends BaseController {
    protected final static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceAlarmService deviceAlarmService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private ProductService productService;


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = "分页列表", consumes = "application/son")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page> list(@RequestBody @Valid RequestObject<DeviceAlarmQueryDto> requestObject) {
        Page page = deviceAlarmService.getPage(requestObject.getData());
        return success(page);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "App端分页列表", notes = "App端分页列表", consumes = "application/son")
    @RequestMapping(value = "/appList", method = RequestMethod.POST)
    public ResponseObject<Page> appList(@RequestBody @Valid RequestObject<DeviceAlarmAppListDto> requestObject) {
        Page page = deviceAlarmService.getAppPage(requestObject.getData());
        return success(page);
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "故障详情", notes = "故障详情", consumes = "application/json")
    @RequestMapping(value = "/deviceAlramDetail", method = RequestMethod.POST)
    public ResponseObject<DeviceAlarmDetailDto> deviceAlramDetail(
            @RequestBody @Valid ResponseObject<Pageable<DeviceAlarmListPageQueryDto>> responseObject) {
        Pageable<DeviceAlarmListPageQueryDto> pageable = responseObject.getData();
        DeviceAlarmListPageQueryDto deviceAlarmListPageDto = pageable.getQuery();
        Integer deviceAlarmId = deviceAlarmListPageDto.getDeviceAlarmId();
        logger.info("故障详情：" + deviceAlarmId);
        return success(deviceAlarmService.getDeviceAlramInfoById(pageable, deviceAlarmId));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "故障详情故障列表",notes = "故障详情故障列表",consumes = "application/json")
    @PostMapping(value = "/detailList")
    public ResponseObject detailList(@RequestBody @Valid RequestObject<Pageable<DeviceAlarmListPageQueryDto>> requestObject){
        DeviceAlarmListPageQueryDto queryDto = requestObject.getData().getQuery();
        if(ParamUtil.isNullOrEmptyOrZero(queryDto)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        return success(deviceAlarmService.listPageByDeviceSon(requestObject.getData(),queryDto.getDeviceSno()));
    }



    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "故障名称", notes = "故障名称", consumes = "application/json")
    @RequestMapping(value = "/deviceAlarmName", method = RequestMethod.POST)
    public ResponseObject deviceAlarmName(@RequestBody @Valid RequestObject<Integer> requestObject) {
        List<String> deviceSnos = deviceAlarmService.getDeviceSnoByProductId(requestObject.getData());
        logger.info("设备故障名称——设备序列号：" + deviceSnos.toString());
        return success(deviceAlarmService.getDeviceAlarmNameBySno(deviceSnos));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "消息推送", notes = "消息推送", consumes = "application/json")
    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    public ResponseObject sendMessage(@RequestBody @Valid RequestObject<DeviceAlarm> requestObject) {
        DeviceAlarm deviceAlarm = requestObject.getData();
        deviceAlarmService.sendDeviceAlarmMessage(deviceAlarm);
        return success();
    }


}
