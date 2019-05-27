package com.gizwits.lease.app.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.SysUserForgetPasswordDto;
import com.gizwits.lease.app.utils.BrowserUtil;
import com.gizwits.lease.user.dto.*;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.*;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>
 * 用户表,不要前缀,因为用户模块计划抽象成通用功能 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@Api(value = "用户管理")
@RestController
@RequestMapping("/app/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @RequestMapping(value = "/getListByPage", method = RequestMethod.POST)
    public ResponseObject getListByPage(@RequestBody @Valid RequestObject<Page<User>> requestObject) {
        EntityWrapper<User> entityWrapper = new EntityWrapper<>();
        Page<User> page = requestObject.getData();
        entityWrapper.orderBy(page.getOrderByField(), page.isAsc());
        Page<User> selectPage = userService.selectPage(page, entityWrapper);
        return success(selectPage);
    }

    @ApiOperation(value = "用户登录", notes = "用户登录",consumes = "application/json")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public @ResponseBody ResponseObject login(@RequestBody @Valid RequestObject<UserLoginDto> requestObject ){
        return success(userService.login(requestObject.getData()));
    }

    @ApiOperation(value = "用户注册", notes = "用户注册",consumes = "application/json")
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public @ResponseBody ResponseObject registerUser(@RequestBody @Valid RequestObject<UserForRegisterDto> requestObject , HttpServletRequest request){
        userService.register(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "用户详情", notes = "用户详情",consumes = "application/json")
    @RequestMapping(value = "/get",method = RequestMethod.POST)
    public @ResponseBody ResponseObject<UserForDetailDto> getUser(@RequestBody @Valid RequestObject<UserDetailDto> requestObject ){
        return success(userService.detailByMobile(requestObject.getData().getOpenid()));
    }


    @ApiOperation(value = "修改用户名", notes = "修改用户名",consumes = "application/json")
    @RequestMapping(value = "/updateUsername",method = RequestMethod.POST)
    public @ResponseBody ResponseObject updateUsername(@RequestBody @Valid RequestObject<UserUpdateDto> requestObject ){
        userService.updateUsername(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "忘记密码", notes = "忘记密码",consumes = "application/json")
    @RequestMapping(value = "/forgetPassword",method = RequestMethod.POST)
    public @ResponseBody ResponseObject forgetPassword(@RequestBody @Valid RequestObject<SysUserForgetPasswordDto> requestObject ){
        userService.forgetPwd(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "短信验证码(注册）", notes = "短信验证码（注册）", consumes = "application/json")
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public @ResponseBody ResponseObject messageAuthenticationCode111(@RequestBody @Valid RequestObject<String> requestObject) {
        userService.messageCodeForRegister(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "短信验证码（忘记密码）", notes = "短信验证码（忘记密码）", consumes = "application/json")
    @RequestMapping(value = "/messageCode", method = RequestMethod.POST)
    public @ResponseBody ResponseObject messageCodeForApp(@RequestBody @Valid RequestObject<String> requestObject) {
        userService.messageCodeForForgetPassword(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "管理员短信验证码（忘记密码）", notes = "短信验证码（忘记密码）", consumes = "application/json")
    @RequestMapping(value = "/messageCodeForAdmin", method = RequestMethod.POST)
    public @ResponseBody ResponseObject messageCodeForAdmin(@RequestBody @Valid RequestObject<String> requestObject) {
        userService.messageCode(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "修改密码", consumes = "application/json")
    @PostMapping("/resetPwd")
    @ResponseBody
    public ResponseObject resetPassword(@RequestBody @Valid RequestObject<UserForUpdatePwdDto> requestObject) {
        userService.resetPwd(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "用户绑定", notes = "用户绑定",consumes = "application/json")
    @RequestMapping(value = "/bindUser",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject bindUser(@RequestBody @Valid RequestObject<UserForRegisterDto> requestObject){
        userService.bindMobile(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", consumes = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseObject update(@RequestBody @Valid RequestObject<UserForInfoDto> requestObject) {

        return success(userService.update(requestObject.getData()));
    }

    @ApiOperation(value = "更新用户手机号", notes = "更新用户手机号信息", consumes = "application/json")
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    public ResponseObject updateUserMobile(@RequestBody @Valid RequestObject<UserForUpdateMobileDto> requestObject) {

        return success(userService.updateUserMobile(requestObject.getData()));
    }
}
