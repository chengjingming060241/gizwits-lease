package com.gizwits.lease.device.web;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.device.entity.dto.DeviceForAssignDto;
import com.gizwits.lease.device.entity.dto.DeviceForUnbindDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForAddDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForDetailDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForDeviceListQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForListDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceGroupForUpdateDto;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceShowDto;
import com.gizwits.lease.device.service.DeviceAssignService;
import com.gizwits.lease.device.service.DeviceGroupService;
import com.gizwits.lease.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 设备组 前端控制器
 * </p>
 *
 * @author lilh
 * @since 2017-08-15
 */
@Api(description = "设备组")
@RestController
@RequestMapping("/device/deviceGroup")
public class DeviceGroupController extends BaseController {

    @Autowired
    private DeviceGroupService deviceGroupService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceAssignService deviceAssignService;


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", consumes = "application/json")
    @PostMapping("/add")
    public ResponseObject<Boolean> add(@RequestBody @Valid RequestObject<DeviceGroupForAddDto> requestObject) {
        return success(deviceGroupService.add(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/page")
    public ResponseObject<Page<DeviceGroupForListDto>> page(@RequestBody RequestObject<Pageable<DeviceGroupForQueryDto>> requestObject) {
        return success(deviceGroupService.page(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<DeviceGroupForDetailDto> detail(@RequestBody RequestObject<Integer> requestObject) {
        return success(deviceGroupService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", consumes = "application/json")
    @PostMapping("/update")
    public ResponseObject<Boolean> update(@RequestBody @Valid RequestObject<DeviceGroupForUpdateDto> requestObject) {
        return success(deviceGroupService.update(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "全部设备", consumes = "application/json")
    @PostMapping("/deviceList")
    public ResponseObject<Page<DeviceShowDto>> deviceList(@RequestBody @Valid RequestObject<Pageable<DeviceGroupForDeviceListQueryDto>> requestObject) {
        Pageable<DeviceQueryDto> pageable = new Pageable<>();
        BeanUtils.copyProperties(requestObject.getData(), pageable, "query");
        pageable.setQuery(new DeviceQueryDto());
        //group_id为空的数据，即未分组的设备
        //pageable.getQuery().setDeviceGroupId(-1);
        if (Objects.nonNull(requestObject.getData().getQuery())) {
            pageable.getQuery().setProductId(requestObject.getData().getQuery().getProductId());
        }
        pageable.getQuery().setExcludeIds(deviceGroupService.resolveDeviceAlreadyInGroup());
        pageable.getQuery().setAccessableOwnerIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser(), false, true));
        return success(deviceService.listPage(pageable));
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
}
