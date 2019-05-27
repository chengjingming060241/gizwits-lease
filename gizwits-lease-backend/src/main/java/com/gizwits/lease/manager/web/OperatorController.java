package com.gizwits.lease.manager.web;

import java.util.*;

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
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.manager.dto.*;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.OperatorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 运营商表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@RestController
@EnableSwagger2
@Api(description = "运营商接口")
@RequestMapping("/operator")
public class OperatorController extends BaseController {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加", notes = "添加", consumes = "application/json")
    @PostMapping(value = "/add")
    public ResponseObject add(@RequestBody @Valid RequestObject<OperatorForAddDto> requestObject) {
        operatorService.add(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页查询", notes = "分页查询", consumes = "application/json")
    @PostMapping(value = "/list")
    public ResponseObject<Page<OperatorForListDto>> list(@RequestBody RequestObject<Pageable<OperatorForQueryDto>> requestObject) {
        Pageable<OperatorForQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new OperatorForQueryDto());
        }
        pageable.getQuery().setUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        return success(operatorService.page(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "直接下级运营商", consumes = "application/json")
    @PostMapping({"/children", "listByOperator"})
    public ResponseObject<Page<OperatorForListDto>> children(@RequestBody RequestObject<Pageable<QueryForCreatorDto>> requestObject) {
        Pageable<QueryForCreatorDto> data = requestObject.getData();
        Pageable<OperatorForQueryDto> pageable = new Pageable<>();
        BeanUtils.copyProperties(data, pageable, "query");
        pageable.setQuery(new OperatorForQueryDto());
        if (Objects.isNull(data.getQuery())) {
            data.setQuery(new QueryForCreatorDto());
        }
        Integer creatorId = data.getQuery().getCreatorId();
        if (Objects.isNull(creatorId)) {
            creatorId = sysUserService.getCurrentUser().getId();
        }
        pageable.getQuery().setUserIds(Collections.singletonList(creatorId));
        return success(operatorService.page(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "可被分配的运营商列表", consumes = "application/json")
    @PostMapping("/assignableOperator")
    public ResponseObject<Page<OperatorForListDto>> operatorForAssign(@RequestBody RequestObject<Pageable<Objects>> requestObject) {
        Pageable<OperatorForQueryDto> pageable = new Pageable<>();
        BeanUtils.copyProperties(requestObject.getData(), pageable, "query");
        pageable.setQuery(new OperatorForQueryDto());
        pageable.getQuery().setUserIds(Collections.singletonList(sysUserService.getCurrentUser().getId()));
        //只查询待分配和正常的运营商
        pageable.getQuery().setStatuses(Arrays.asList(StatusType.NEED_TO_ASSIGN.getCode(), StatusType.OPERATING.getCode()));
        return success(operatorService.page(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<OperatorForDetailDto> detail(@RequestBody RequestObject<Integer> requestObject) {
        return success(operatorService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", notes = "更新", consumes = "application/json")
    @PostMapping(value = "/update")
    public ResponseObject<OperatorForDetailDto> update(@RequestBody @Valid RequestObject<OperatorForUpdateDto> requestObject) {
        OperatorForUpdateDto dto = requestObject.getData();
        if (Objects.isNull(dto.getAccount())) {
            dto.setAccount(new OperatorForUpdateDto.Account());
        }
        if (Objects.isNull(dto.getAccount().getExt())) {
            dto.getAccount().setExt(new SysUserExtForAddDto());
        }
        return success(operatorService.update(dto));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "运营商重置密码", notes = "运营商重置密码", consumes = "application/json")
    @PostMapping(value = "/resetPwd")
    public ResponseObject resetPwd(@RequestBody RequestObject<OperatorForResetPwd> requestObject) {
        OperatorForResetPwd resetPwd = requestObject.getData();

        SysUser sysUser =
                sysUserService.selectById(resetPwd.getId());

        String pwd = resetPwd.getNewPwd();
        sysUser.setUtime(new Date());
        sysUser.setPassword(PasswordUtil.generate(pwd));

        sysUserService.updateById(sysUser);
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "状态切换", consumes = "application/json")
    @PostMapping("/switch")
    public ResponseObject change(@RequestBody RequestObject<Integer> requestObject) {
        return success(operatorService.change(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @PostMapping(value = "/delete")
    public ResponseObject<String> delete(@RequestBody RequestObject<List<Integer>> requestObject) {

        return success(operatorService.delete(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "绑定已有账号下拉列表", consumes = "application/json")
    @GetMapping("/bindingAccount")
    public ResponseObject<List<SysUserForPullDto>> existAccount() {
        return success(operatorService.bindingExistedSysUserList());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "上传头像或logo", consumes = "application/json")
    @PostMapping("/uploadLogo")
    public ResponseObject create(@RequestParam("file") MultipartFile file) {
        return success(operatorService.uploadAvatar(file));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "具有分配权限的子运营商",consumes = "application/json")
    @PostMapping(value = "/subOperatorWithAssign")
    public ResponseObject<Page<Operator>> subOperatorWithAssign (@RequestBody @Valid RequestObject<Pageable<Integer>> requestObject){

        return success(operatorService.getAssignableOperator(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "获取所有父级运营商", consumes = "application/json")
    @PostMapping(value = "/getAllParentOperator")
    public ResponseObject<List<OperatorForCascaderDto>> getAllParentOperator() {
        return success(operatorService.getAllParentOperator());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "根据父运营商获取下面子运营商", consumes = "application/json")
    @PostMapping(value = "/getAllChildOperatorById")
    public ResponseObject<List<OperatorForCascaderDto>> getAllChildOperatorById(@RequestBody RequestObject<Integer> requestObject) {
        Operator operator = operatorService.selectById(requestObject.getData());
        return success(operatorService.getAllChildOperatorById(operator.getSysAccountId()));
    }
}
