package com.gizwits.lease.qek.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.message.entity.SysMessage;
import com.gizwits.lease.message.entity.dto.SysMessageListDto;
import com.gizwits.lease.message.entity.dto.SysMessageQueryDto;
import com.gizwits.lease.message.service.SysMessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/23.
 */
@RestController
@EnableSwagger2
@RequestMapping("/app/qek/operatorMessage")
public class OperatorMessageController extends BaseController {
    @Autowired
    private SysMessageService sysMessageService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = "分页列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<SysMessage>> list(@RequestBody @Valid RequestObject<Pageable<String>> requestObject) {
        return success(sysMessageService.getMessagePage(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "清空运营商消息", consumes = "application/json")
    @PostMapping("/clearMess")
    public ResponseObject<Page<SysMessage>> clearMess(@RequestBody @Valid RequestObject<String> requestObject) {
        sysMessageService.clearMessage(requestObject.getData());
        return success();
    }
}
