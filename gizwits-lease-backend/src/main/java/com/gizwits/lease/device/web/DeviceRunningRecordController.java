package com.gizwits.lease.device.web;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.device.service.DeviceRunningRecordService;
import com.gizwits.lease.device.vo.DeviceRunningRecordForListDto;
import com.gizwits.lease.device.vo.DeviceRunningRecordForOfflineListDto;
import com.gizwits.lease.device.vo.DeviceRunningRecordForQueryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 设备运行记录表 前端控制器
 * </p>
 *
 * @author zhl
 * @since 2017-07-12
 */
@Api("设备运行日志")
@EnableSwagger2
@RestController
@RequestMapping("/device/deviceRunningRecord")
public class DeviceRunningRecordController extends BaseController {

    @Autowired
    private DeviceRunningRecordService deviceRunningRecordService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<Page<DeviceRunningRecordForListDto>> list(@RequestBody @Valid RequestObject<Pageable<DeviceRunningRecordForQueryDto>> requestObject) {
        return success(deviceRunningRecordService.list(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "离线记录列表", consumes = "application/json")
    @PostMapping("/offlineList")
    public ResponseObject<Page<DeviceRunningRecordForOfflineListDto>> offlineList(@RequestBody @Valid RequestObject<Pageable<DeviceRunningRecordForQueryDto>> requestObject) {
        return success(deviceRunningRecordService.offlineList(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "未读离线记录数", consumes = "application/json")
    @GetMapping("/getUnreadOffline")
    public ResponseObject<Integer> getUnreadOffline() {
        return success(deviceRunningRecordService.getUnreadOffline());
    }

}
