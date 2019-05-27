package com.gizwits.lease.majiang.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.china.service.ChinaAreaService;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaListDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaQueryDto;
import com.gizwits.lease.device.entity.dto.MJDeviceLaunchAreaDetail;
import com.gizwits.lease.device.entity.dto.QueryForCreatorDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/9/2.
 */
@RestController
@EnableSwagger2
@Api(description = "麻将机投放点")
@RequestMapping(value = "/majiang/deviceLaunchArea")
public class MDeviceLaunchAreaController extends BaseController {

    protected final static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private ChinaAreaService chinaAreaService;

      @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "未分配的投放点", consumes = "application/json")
    @PostMapping({"/unAllotArea"})
    public ResponseObject<Page<DeviceLaunchAreaListDto>> unAllotArea(@RequestBody RequestObject<Pageable<QueryForCreatorDto>> requestObject) {
        Pageable<QueryForCreatorDto> data = requestObject.getData();
        Pageable<DeviceLaunchAreaQueryDto> pageable = deviceLaunchAreaService.setParam(data);
        return success(deviceLaunchAreaService.getUnAllotDeviceLaunchAreaListPage(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = " 维护人员",notes = " 维护人员",consumes = "application/json")
    @RequestMapping(value = "/maintainer",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject maintainer(){
        logger.info("维护人员列表");
        return success(deviceLaunchAreaService.getDeviceMaintainerDtos());
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新",notes = " 更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject update(@RequestBody @Valid RequestObject<DeviceLaunchArea> requestObject){
        DeviceLaunchArea deviceLaunchArea = requestObject.getData();
        DeviceLaunchArea deviceLaunchArea1 = deviceLaunchAreaService.selectById(deviceLaunchArea.getId());
        if(Objects.isNull(deviceLaunchArea1)){
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_LAUNCH_AREA_NOT_EXIST);
        }
        logger.info("更新deviceLaunchArea,id="+deviceLaunchArea.getId());
        deviceLaunchAreaService.updateDeviceLaunchArea(deviceLaunchArea);
        return success();
    }


    @ApiOperation(value = " 投放点名称是否存在",notes = "投放点名称是否存在",consumes = "application/json")
    @RequestMapping(value = "/isExist",method = RequestMethod.POST)
    public ResponseObject isExist(@RequestBody @Valid RequestObject<String> requestObject){
        logger.info(" 投放点名称："+requestObject.getData());
        return success(deviceLaunchAreaService.deviceLaunchAreaIsExist(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = " 添加",notes = " 添加",consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject add(@RequestBody @Valid RequestObject<DeviceLaunchArea> requestObject){
        DeviceLaunchArea deviceLaunchArea = requestObject.getData();
        logger.info("添加deviceLaunchArea："+deviceLaunchArea.toString());
        deviceLaunchAreaService.addDeviceLaunchArea(deviceLaunchArea);
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = " 详情",notes = " 添详情加",consumes = "application/json")
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<MJDeviceLaunchAreaDetail> detail(@RequestBody @Valid RequestObject<Integer> requestObject){
        return success(deviceLaunchAreaService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "获得下一级地区",notes = "获得下一级地区",consumes = "application/json")
    @RequestMapping(value = "/area",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject area(@RequestBody @Valid RequestObject<Integer> code){
        return success(chinaAreaService.getAreas(code.getData()));
    }


}
