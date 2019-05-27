package com.gizwits.lease.majiang.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.LoginDto;
import com.gizwits.boot.dto.SysUserForgetPasswordDto;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysRole;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysRoleService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.PasswordUtil;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
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

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/30.
 */
@RestController
@EnableSwagger2
@Api(description = "管理端")
@RequestMapping(value = "/majiang/user")
public class MUserController extends BaseController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private OperatorService operatorService;


    @ApiOperation(value = "登录(维护人员）", notes = "登录", consumes = "application/json")
    @PostMapping(value = "/login")
    public ResponseObject login(@RequestBody @Valid RequestObject<LoginDto> requestObject) {
        LoginDto loginDto = requestObject.getData();
        JudgeRole(loginDto);
        return success(sysUserService.login(requestObject.getData()));
    }


    @ApiOperation(value = "短信验证码", notes = "短信验证码", consumes = "application/json")
    @RequestMapping(value = "/messageAuthenticationCode", method = RequestMethod.POST)
    public ResponseObject messageAuthenticationCode(@RequestBody @Valid RequestObject<String> requestObject) {
        sysUserService.JudgeByMessage(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "忘记密码", notes = "忘记密码", consumes = "application/json")
    @PostMapping(value = "/forgetPwd")
    public ResponseObject forgetPwd(@RequestBody @Valid RequestObject<SysUserForgetPasswordDto> requestObject) {
        return success(sysUserService.forgetPwd(requestObject.getData()));
    }

    @ApiOperation(value = "当前用户角色(1运营商 2代理商）", notes = "当前用户角色", consumes = "application/json")
    @PostMapping(value = "/role")
    public ResponseObject<Integer> role() {
        Integer role = operatorService.getRole();
        return success(role);
    }

    private void JudgeRole(LoginDto loginDto) {
        EntityWrapper<SysUser> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("username", loginDto.getUsername());
        SysUser sysUser = sysUserService.selectOne(entityWrapper);
        if(ParamUtil.isNullOrEmptyOrZero(sysUser) || !PasswordUtil.verify(loginDto.getPassword(), sysUser.getPassword())){
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        List<SysRole> sysRoles = sysRoleService.getRolesByUserId(sysUser.getId());
        if(ParamUtil.isNullOrEmptyOrZero(sysRoles)){
            throw  new SystemException(LeaseExceEnums.USER_DONT_EXISTS.getCode(),"用户无权限登录");
        }
        CommonSystemConfig commonSystemConfig = SysConfigUtils.get(CommonSystemConfig.class);
        String manatiner = commonSystemConfig.getMaintenanceRole();
        boolean flag = false;
        for(SysRole role:sysRoles){
            if(role.getRoleName().indexOf(manatiner)>=0){
                flag = true;
            }
        }
        if(!flag){
            throw  new SystemException(LeaseExceEnums.USER_DONT_EXISTS.getCode(),"用户无权限登录");
        }
    }

}
