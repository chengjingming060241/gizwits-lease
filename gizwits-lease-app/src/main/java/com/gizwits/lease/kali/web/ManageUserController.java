package com.gizwits.lease.kali.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.JwtAuthenticationDto;
import com.gizwits.boot.dto.LoginDto;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.device.entity.dto.DeviceQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceShowDto;
import com.gizwits.lease.device.service.DeviceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Objects;

/**
 * Created by zhl on 2017/8/15.
 */
@Api(value = "移动端管理员")
@RestController
@RequestMapping("/app/manage")
public class ManageUserController extends BaseController{

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private DeviceService deviceService;

    @ApiOperation(value = "登陆", notes = "登陆", consumes = "application/json")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseObject<JwtAuthenticationDto> login(@RequestBody @Valid RequestObject<LoginDto> requestObject) {
        LoginDto loginDto = requestObject.getData();
        JwtAuthenticationDto tokenDto = sysUserService.login(loginDto);
        return success(tokenDto);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "设备列表", notes = "设备列表", consumes = "application/json")
    @RequestMapping(value = "/device/list", method = RequestMethod.POST)
    public ResponseObject<Page<DeviceShowDto>> list(@RequestBody @Valid RequestObject<Pageable<DeviceQueryDto>> requestObject) {
        Pageable<DeviceQueryDto> pageable = requestObject.getData();
        if(Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new DeviceQueryDto());
        }
        if (Objects.nonNull(pageable.getQuery().getOperatorAccountId())) {
            pageable.getQuery().setAccessableOwnerIds(Collections.singletonList(pageable.getQuery().getOperatorAccountId()));
        } else {
            pageable.getQuery().setAccessableOwnerIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        }
        //防止前台查询已删除的数据
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        return success(deviceService.listPage(pageable));
    }
}
