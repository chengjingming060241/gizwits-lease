package com.gizwits.lease.xzy.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.SysUserForUpdatePwdDto;
import com.gizwits.boot.dto.SysUserForgetPasswordDto;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.PasswordUtil;
import com.gizwits.lease.user.dto.UserForInfoDto;
import com.gizwits.lease.user.dto.UserForRegisterDto;
import com.gizwits.lease.user.dto.UserForUpdatePwdDto;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/10.
 */
@RestController(value = "xyz")
@EnableSwagger2
@Api(description = "享智云用户接口")
@RequestMapping(value = "xiangzhiyun/xUser")
public class UserController extends BaseController{

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户绑定", notes = "用户绑定",consumes = "application/json")
    @RequestMapping(value = "/bindUser",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject bindUser(@RequestBody @Valid RequestObject<UserForRegisterDto> requestObject){
        userService.bindMobile(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "用户信息", notes = "用户信息", consumes = "application/json")
    @RequestMapping(value = "/userinfo", method = RequestMethod.POST)
    public ResponseObject<UserForInfoDto> userinfo(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(userService.userInfo(requestObject.getData()));
    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", consumes = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseObject update(@RequestBody @Valid RequestObject<UserForInfoDto> requestObject) {

        return success(userService.update(requestObject.getData()));
    }
}
