package com.gizwits.lease.wallet.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.wallet.dto.UserWalletUseRecordDto;
import com.gizwits.lease.wallet.service.UserWalletUseRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * <p>
 * 用户钱包操作记录表 前端控制器
 * </p>
 * Created by yinhui on 2017/7/29.
 */
@RestController
@EnableSwagger2
@Api(description = "用户钱包操作记录接口")
@RequestMapping(value = "/wallet/userWalletUseRecord")
public class UserWalletUseRecordController extends BaseController{

    @Autowired
    private UserWalletUseRecordService userWalletUseRecordService;

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "用户钱包操作记录",notes = "用户钱包操作记录",consumes = "application/json")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject<Page<UserWalletUseRecordDto>> list(@RequestBody @Valid RequestObject<Pageable<String>> requestObject){
         return success(userWalletUseRecordService.listPage(requestObject.getData()));
     }
}
