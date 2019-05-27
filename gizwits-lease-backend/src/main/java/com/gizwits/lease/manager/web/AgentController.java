package com.gizwits.lease.manager.web;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.dto.SysUserExtForAddDto;
import com.gizwits.boot.dto.SysUserForPullDto;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.PasswordUtil;
import com.gizwits.lease.device.entity.dto.QueryForCreatorDto;
import com.gizwits.lease.manager.dto.*;
import com.gizwits.lease.manager.service.AgentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 代理商表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@RestController
@EnableSwagger2
@Api(description = "代理商接口")
@RequestMapping("/agent")
public class AgentController extends BaseController {

    @Autowired
    private AgentService agentService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @PostMapping(value = "/add")
    public ResponseObject<Boolean> add(@RequestBody @Valid RequestObject<AgentForAddDto> requestObject) {
        return success(agentService.add(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @PostMapping(value = "/list")
    public ResponseObject<Page<AgentForListDto>> list(@RequestBody RequestObject<Pageable<AgentForQueryDto>> requestObject) {
        Pageable<AgentForQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new AgentForQueryDto());
        }
        pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        return success(agentService.page(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "直接下级代理商", consumes = "application/json")
    @PostMapping("/children")
    public ResponseObject<Page<AgentForListDto>> children(@RequestBody RequestObject<Pageable<QueryForCreatorDto>> requestObject) {
        Pageable<QueryForCreatorDto> data = requestObject.getData();
        Pageable<AgentForQueryDto> pageable = new Pageable<>();
        BeanUtils.copyProperties(data, pageable, "query");
        pageable.setQuery(new AgentForQueryDto());
        if (Objects.isNull(data.getQuery())) {
            data.setQuery(new QueryForCreatorDto());
        }
        Integer creatorId = data.getQuery().getCreatorId();
        if (Objects.isNull(creatorId)) {
            //若前台没有传递代理商绑定的系统账号，则查询当前登录人直接创建的代理商
            creatorId = sysUserService.getCurrentUser().getId();
        }
        pageable.getQuery().setAccessableUserIds(Collections.singletonList(creatorId));
        return success(agentService.page(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<AgentForDetailDto> detail(@RequestBody RequestObject<Integer> requestObject) {
        return success(agentService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", notes = "更新", consumes = "application/json")
    @PostMapping(value = "/update")
    public ResponseObject<AgentForDetailDto> update(@RequestBody @Valid RequestObject<AgentForUpdateDto> requestObject) {
        AgentForUpdateDto dto = requestObject.getData();
        if (Objects.isNull(dto.getAccount())) {
            dto.setAccount(new AgentForUpdateDto.Account());
        }
        if (Objects.isNull(dto.getAccount().getExt())) {
            dto.getAccount().setExt(new SysUserExtForAddDto());
        }
        return success(agentService.update(dto));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "代理商重置密码", notes = "代理商重置密码", consumes = "application/json")
    @PostMapping(value = "/resetPwd")
    public ResponseObject resetPwd(@RequestBody RequestObject<AgentForResetPwd> requestObject) {
        AgentForResetPwd resetPwd = requestObject.getData();

        SysUser sysUser =
                sysUserService.selectById(resetPwd.getId());

        String pwd = resetPwd.getNewPwd();
        sysUser.setPassword(PasswordUtil.generate(pwd));
        sysUser.setUtime(new Date());

        sysUserService.updateById(sysUser);
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "状态切换", consumes = "application/json")
    @PostMapping("/switch")
    public ResponseObject change(@RequestBody RequestObject<Integer> requestObject) {
        return success(agentService.change(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @PostMapping(value = "/delete")
    public ResponseObject<String> delete(@RequestBody RequestObject<List<Integer>> requestObject) {

        return success(agentService.delete(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "绑定已有账号下拉列表", consumes = "application/json")
    @GetMapping("/bindingAccount")
    public ResponseObject<List<SysUserForPullDto>> existAccount() {
        return success(agentService.availableSysUserList());
    }
}
