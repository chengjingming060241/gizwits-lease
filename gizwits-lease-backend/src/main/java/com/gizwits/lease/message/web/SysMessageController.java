package com.gizwits.lease.message.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.constant.SysMessageEnum;
import com.gizwits.lease.message.entity.SysMessage;
import com.gizwits.lease.message.entity.dto.SysMessageAdd;
import com.gizwits.lease.message.entity.dto.SysMessageListDto;
import com.gizwits.lease.message.entity.dto.SysMessageQueryDto;
import com.gizwits.lease.message.service.SysMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 系统消息表 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
@RestController
@EnableSwagger2
@Api(description = "系统信息接口")
@RequestMapping("/message/sysMessage")
public class SysMessageController extends BaseController {
    @Autowired
    private SysMessageService sysMessageService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = "分页列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<SysMessageListDto>> list(@RequestBody @Valid RequestObject<Pageable<SysMessageQueryDto>> requestObject) {
        return success(sysMessageService.getSysMessagePage(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "添加",notes = "添加",consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public ResponseObject add(@RequestBody @Valid RequestObject<SysMessageAdd> requestObject){
        sysMessageService.addSysMessage(requestObject.getData());
        return  success();
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "创建的角色",notes = "创建的角色",consumes = "application/json")
    @RequestMapping(value = "/role",method = RequestMethod.POST)
    public ResponseObject role(){

        return  success(sysMessageService.listRoles());
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情",notes = "详情",consumes = "application/json")
    @RequestMapping(value = "/detail",method = RequestMethod.POST)
    public ResponseObject detail(@RequestBody @Valid RequestObject<Integer> requestObject){

        return  success(sysMessageService.detail(requestObject.getData()));
    }

}