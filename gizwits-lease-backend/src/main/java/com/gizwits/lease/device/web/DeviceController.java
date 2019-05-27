package com.gizwits.lease.device.web;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.*;
import com.gizwits.lease.device.service.DeviceAssignService;
import com.gizwits.lease.device.service.DeviceExtService;
import com.gizwits.lease.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * <p>
 * 设备表 前端控制器
 * </p>
 *
 * @author zhl
 * @since 2017-07-11
 */
@RestController
@EnableSwagger2
@Api(description = "设备接口")
@RequestMapping("/device/device")
public class DeviceController extends BaseController {
    protected Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");
    @Autowired
    private DeviceService deviceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceAssignService deviceAssignService;

    @Autowired
    private DeviceExtService deviceExtService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "设备列表", notes = "设备列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<DeviceShowDto>> list(@RequestBody @Valid RequestObject<Pageable<DeviceQueryDto>> requestObject) {

        Pageable<DeviceQueryDto> pageable = requestObject.getData();

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
        return success(deviceService.listPage(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseObject add(@RequestBody @Valid RequestObject<DeviceAddDto> requestObject) {
        DeviceAddDto deviceAddDto = requestObject.getData();

        logger.info("添加设备：" + deviceAddDto.getName());
        deviceService.addDevice(deviceAddDto);
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<DeviceForDetailDto> detail(@RequestBody RequestObject<String> requestObject) {
        return success(deviceService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", consumes = "application/json")
    @PostMapping("/update")
    public ResponseObject update(@RequestBody @Valid RequestObject<DeviceForUpdateDto> requestObject) {
        deviceService.update(requestObject.getData());
        return success();
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
    @ApiOperation(value = "获取分配目标", consumes = "application/json")
    @PostMapping("/preAssign")
    public ResponseObject<List<AssignDestinationDto>> preAssign(@RequestBody RequestObject<Object> requestObject) {
        return success(deviceAssignService.preAssign());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分配", consumes = "application/json")
    @PostMapping("/assign")
    public ResponseObject<Boolean> assign(@RequestBody @Valid RequestObject<DeviceForAssignDto> requestObject) {
        return success(deviceAssignService.assign(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "解绑", consumes = "application/json")
    @PostMapping("/unbind")
    public ResponseObject<Boolean> unbind(@RequestBody RequestObject<DeviceForUnbindDto> requestObject) {
        return success(deviceAssignService.unbind(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @PostMapping("/delete")
    public ResponseObject<String> delete(@RequestBody @Valid RequestObject<List<String>> requestObject) {

        return success(deviceService.deleteDevice(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "mac是否存在", notes = "mac是否存在", consumes = "application/json")
    @PostMapping("/macIsExist")
    public ResponseObject macIsExist(@RequestBody @Valid RequestObject<String> requestObject) {

        return success(deviceService.judgeMacIsExsit(requestObject.getData()));
    }

    @ApiOperation(value = "展示指令", notes = "展示指令", consumes = "application/json")
    @PostMapping("/show")
    public ResponseObject<List<DeviceProductShowDto>> show(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(deviceService.show(requestObject.getData()));
    }

    @ApiOperation(value = "解除锁定", notes = "解除锁定", consumes = "application/json")
    @PostMapping("/unlock")
    public ResponseObject unlock(@RequestBody @Valid RequestObject<String> requestObject) {
        deviceService.updateLockFlag(requestObject.getData(), 0, false);
        return success();
    }

    @ApiOperation(value = "分配收费模式或投放点", notes = "分配收费模式或投放点", consumes = "application/json")
    @PostMapping("/assignModeOrArea")
    public ResponseObject assignModeOrArea(@RequestBody @Valid RequestObject<DeviceAssignDto> requestObject) {
        deviceService.assingModeOrArea(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "将设备状态置为空闲", notes = "将设备状态置为空闲", consumes = "application/json")
    @PostMapping("/resetStatus")
    public ResponseObject resetStatus(@RequestBody @Valid RequestObject<String> requestObject) {

        return success(deviceExtService.resetStatus(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "改变设备运营状态", notes = "操作设备是否投入运营", consumes = "application/json")
    @PostMapping("/changeOperateStatus")
    public ResponseObject changeOperateStatus(@RequestBody @Valid RequestObject<DeviceForChangeOperateStatusDto> requestObject) {
        return success(deviceService.changeOperateStatus(requestObject.getData().getSno(),
                requestObject.getData().getOperateStatus()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "滤网寿命复位", notes = "重置滤网寿命", consumes = "application/json")
    @PostMapping("/resetFilter")
    public ResponseObject resetFilter(@RequestBody ResponseObject<String> responseObject) {
        return
                success(deviceService.resetFilter(responseObject.getData()));
    }
}
