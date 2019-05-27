package com.gizwits.lease.majiang.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.device.entity.DeviceExtForMajiang;
import com.gizwits.lease.device.entity.dto.DeviceForFireDto;
import com.gizwits.lease.device.entity.dto.DeviceForUpdateDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaAssociatedOperatorDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaListDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceShowDto;
import com.gizwits.lease.device.entity.dto.DeviceWithLaunchArea;
import com.gizwits.lease.device.entity.dto.ListDeviceForFireDto;
import com.gizwits.lease.device.entity.dto.MJDeviceDetailDto;
import com.gizwits.lease.device.entity.dto.MJDeviceLaunchAreaDetail;
import com.gizwits.lease.device.entity.dto.QueryForCreatorDto;
import com.gizwits.lease.device.service.DeviceExtForMajiangService;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.product.dto.ProductServiceListQuerytDto;
import com.gizwits.lease.product.dto.ProductServicecModeListDto;
import com.gizwits.lease.product.service.ProductServiceModeService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/30.
 */
@RestController
@EnableSwagger2
@Api(description = "麻将机设备")
@RequestMapping(value = "/majiang/device")
public class MDeviceController extends BaseController {
    protected final static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductServiceModeService productServiceModeService;

    @Autowired
    private DeviceExtForMajiangService deviceExtByMajiangService;


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "设备更新", notes = "设备更新", consumes = "application/json")
    @PostMapping(value = "updateDevice")
    public ResponseObject updateDevice(@RequestBody @Valid RequestObject<DeviceForUpdateDto> requestObject) {
        deviceService.update(requestObject.getData());
        return success();
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "麻将机详情", consumes = "application/json")
    @PostMapping(value = "/detail")
    public ResponseObject<MJDeviceDetailDto> detail(@RequestBody @Valid RequestObject<String> requestObject) {
        logger.info("麻将机详情：sno=" + requestObject.getData() + "，当前用户id=" + sysUserService.getCurrentUser().getId());
        return success(deviceService.deviceDetailForMahjong(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "设备列表", notes = "设备列表", consumes = "application/json")
    @PostMapping(value = "/device")
    public ResponseObject<Page<DeviceShowDto>> device(@RequestBody @Valid RequestObject<Pageable<DeviceQueryDto>> requestObject) {
        Pageable<DeviceQueryDto> pageable = requestObject.getData();
//        setParam(pageable);
        return success(deviceService.listPage(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "当前用户设备列表（1未分配 2已分配投放点）", notes = "当前用户设备列表", consumes = "application/json")
    @PostMapping(value = "/currentDevice")
    public ResponseObject<Page<DeviceShowDto>> currentDevice(@RequestBody @Valid RequestObject<Pageable<DeviceQueryDto>> requestObject) {
        Pageable<DeviceQueryDto> pageable = requestObject.getData();
        setParam(pageable);
        return success(deviceService.currentListPage(requestObject.getData(),requestObject.getData().getQuery().getType()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "运营商或代理商直接创建的投放点", consumes = "application/json")
    @PostMapping({"/areaList"})
    public ResponseObject<Page<DeviceLaunchAreaListDto>> areaList(@RequestBody RequestObject<Pageable<QueryForCreatorDto>> requestObject) {
        Pageable<QueryForCreatorDto> data = requestObject.getData();
        Pageable<DeviceLaunchAreaQueryDto> pageable = deviceLaunchAreaService.setParam(data);
        return success(deviceLaunchAreaService.getDeviceLaunchAreaListPage(pageable));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "运营商或代理商直接创建的收费模式", notes = "运营商或代理商直接创建的收费模式", consumes = "application/json")
    @RequestMapping(value = "/modelist", method = RequestMethod.POST)
    public ResponseObject<Page<ProductServicecModeListDto>> modelist(@RequestBody @Valid RequestObject<Pageable<ProductServiceListQuerytDto>> requestObject) {
        Page<ProductServicecModeListDto> result = productServiceModeService.getProductServicecModeListDtoPage(requestObject.getData());
        return success(result);
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "保存游戏程序", consumes = "application/json")
    @PostMapping(value = "/updateGame")
    public ResponseObject updateGame(@RequestBody @Valid RequestObject<DeviceExtForMajiang> requestObject) {
        deviceExtByMajiangService.setGame(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "游戏程序", consumes = "application/json")
    @PostMapping(value = "/game")
    public ResponseObject<DeviceExtForMajiang> game(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(deviceExtByMajiangService.select(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "关联运营商", notes = "关联运营商", consumes = "application/json")
    @RequestMapping(value = "/associatedOperator", method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<List<Integer>> associatedOperator(@RequestBody @Valid RequestObject<DeviceLaunchAreaAssociatedOperatorDto> requestObject) {
        DeviceLaunchAreaAssociatedOperatorDto deviceLaunchAreaAssociatedOperatorDto = requestObject.getData();
        List<Integer> ids = deviceLaunchAreaService.associatedOperator(deviceLaunchAreaAssociatedOperatorDto);
        logger.info("关联运营商信息，投放点Id：" + deviceLaunchAreaAssociatedOperatorDto.getDeviceLaunchAreaIds() + ",运营商信息id：" +
                deviceLaunchAreaAssociatedOperatorDto.getOperatorAccountId() + ",名称：" + deviceLaunchAreaAssociatedOperatorDto.getOperatorName());
        return success(ids);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "投放点详情", consumes = "application/json")
    @PostMapping(value = "/areaDetail")
    public ResponseObject<MJDeviceLaunchAreaDetail> areaDetail(@RequestBody @Valid RequestObject<Integer> requestObject) {
        logger.info("投放点详情：id=" + requestObject.getData() + "，当前用户id=" + sysUserService.getCurrentUser().getId());
        return success(deviceLaunchAreaService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "发送控制指令", consumes = "application/json")
    @PostMapping("/fire")
    public ResponseObject<Boolean> fire(@RequestBody @Valid RequestObject<DeviceForFireDto> requestObject) {
        DeviceForFireDto dto = requestObject.getData();
        if (ParamUtil.isNullOrEmptyOrZero(requestObject.getData().getAttrs())) {
            return success(deviceService.remoteDeviceControl(dto.getSno(), dto.getName(), dto.getValue()));
        } else {
            return success(deviceService.remoteDeviceControl(dto.getSno(), dto.getAttrs()));
        }
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "投放点删除设备", notes = "投放点删除设备", consumes = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<List<String>> delete(@RequestBody @Valid RequestObject<DeviceWithLaunchArea> requestObject) {

        return success(deviceService.deviceDeleteLaunchArea(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "投放点分配设备", notes = "投放点分配设备", consumes = "application/json")
    @RequestMapping(value = "/distribution", method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<List<String>> distribution(@RequestBody @Valid RequestObject<DeviceWithLaunchArea> requestObject) {

        return success(deviceService.deviceWithLaunchArea(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "批量发送控制指令", consumes = "application/json")
    @PostMapping("/fireList")
    public ResponseObject<List<String>> fireList(@RequestBody @Valid RequestObject<ListDeviceForFireDto> requestObject) {
        ListDeviceForFireDto dto = requestObject.getData();
        List<String> snos = deviceService.getFireFailSnos(dto);
        return success(snos);
    }


    private void setParam(Pageable<DeviceQueryDto> pageable) {
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new DeviceQueryDto());
        }
        Integer creatorId = pageable.getQuery().getOperatorAccountId();
        if (Objects.isNull(creatorId)) {
            creatorId = pageable.getQuery().getCreatorId();
        }
        if (Objects.nonNull(creatorId)) {
            pageable.getQuery().setAccessableOwnerIds(Collections.singletonList(creatorId));
        } else {
            pageable.getQuery().setAccessableOwnerIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        }

        //防止前台查询已删除的数据
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
    }

}
