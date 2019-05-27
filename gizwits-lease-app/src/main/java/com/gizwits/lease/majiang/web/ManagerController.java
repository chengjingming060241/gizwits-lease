package com.gizwits.lease.majiang.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceShowDto;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.manager.dto.MJSonOperatorDto;
import com.gizwits.lease.manager.dto.OperatorAllotDeviceDto;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.xml.ws.Response;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/30.
 */
@RestController
@EnableSwagger2
@Api(description = "麻将机运营商／代理商")
@RequestMapping(value = "/majiang/manager")
public class ManagerController extends BaseController {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private AgentService agentService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "当前用户设备/投放点统计", notes = "当前用户设备统计", consumes = "application/json")
    @PostMapping(value = "/currentUser")
    public ResponseObject<MJSonOperatorDto> currentUser() {
        return success(operatorService.getOperatorDeviceCount());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "当前用户未分配设备/投放点统计", notes = "当前用户设备统计", consumes = "application/json")
    @PostMapping(value = "/UnAllot")
    public ResponseObject<MJSonOperatorDto> UnAllot() {
        return success(operatorService.getOperatorUnAllotDeviceCount());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "子级运营商设备/投放点统计", notes = "子级运营商设备统计", consumes = "application/json")
    @PostMapping(value = "/operator")
    public ResponseObject<Page<MJSonOperatorDto>> operator(@RequestBody @Valid RequestObject<Pageable<Integer>> requestObject) {
        return success(operatorService.getMJOperator(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "子级代理商设备/投放点统计", notes = "子级代理商设备统计", consumes = "application/json")
    @PostMapping(value = "/agent")
    public ResponseObject<Page<MJSonOperatorDto>> agent(@RequestBody @Valid RequestObject<Pageable<Integer>> requestObject) {
        return success(operatorService.getMJAgent(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "是否分配", notes = "是否分配", consumes = "application/json")
    @PostMapping(value = "/isAllot")
    public ResponseObject<Boolean> isAllot() {
        return success(operatorService.isAllot());
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "子运营商", notes = "子运营商", consumes = "application/json")
    @RequestMapping(value = "/sonOperators", method = RequestMethod.POST)
    public ResponseObject<List<Operator>> sonOperators(@RequestBody @Valid RequestObject<Integer> requestObject) {
        Integer creator;
        if (ParamUtil.isNullOrEmptyOrZero(requestObject) || ParamUtil.isNullOrEmptyOrZero(requestObject.getData())) {
            SysUser user = sysUserService.getCurrentUser();
            creator = user.getId();
        } else {
            creator = requestObject.getData();
        }

        return success(operatorService.getDirectOperatorByCreator(creator));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "子代理商", notes = "子代理商", consumes = "application/json")
    @RequestMapping(value = "/sonAgents", method = RequestMethod.POST)
    public ResponseObject<List<Agent>> sonAgents(@RequestBody @Valid RequestObject<Integer> requestObject) {
        Integer creator;
        if (ParamUtil.isNullOrEmptyOrZero(requestObject) || ParamUtil.isNullOrEmptyOrZero(requestObject.getData())) {
            SysUser user = sysUserService.getCurrentUser();
            creator = user.getId();
        } else {
            creator = requestObject.getData();
        }
        return success(agentService.getAgentByCreateId(creator));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分配设备", notes = "分配设备", consumes = "application/json")
    @PostMapping(value = "allot")
    public ResponseObject<List<String>> allot(@RequestBody @Valid RequestObject<OperatorAllotDeviceDto> requestObject) {

        return success(deviceService.deviceAllotOperator(requestObject.getData()));
    }


}
