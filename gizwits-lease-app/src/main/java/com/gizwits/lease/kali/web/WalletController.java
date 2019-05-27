package com.gizwits.lease.kali.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.constant.UserWalletUseEnum;
import com.gizwits.lease.wallet.dto.UserWalletUseRecordDto;
import com.gizwits.lease.wallet.dto.WalletPayDto;
import com.gizwits.lease.wallet.service.UserWalletService;
import com.gizwits.lease.wallet.service.UserWalletUseRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/23.
 */
@RestController(value = "kali")
@EnableSwagger2
@Api(description = "卡励用户钱包充值记录")
@RequestMapping(value = "/kali/wallet")
public class WalletController extends BaseController{

    @Autowired
    UserWalletUseRecordService userWalletUseRecordService;

    @Autowired
    private UserWalletService userWalletService;

//    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
//    @ApiOperation(value = "余额支付", notes = "余额支付", consumes = "application")
//    @RequestMapping(value = "/pay", method = RequestMethod.POST)
//    public    @ResponseBody
//    ResponseObject pay(@RequestBody @Valid RequestObject<WalletPayDto> requestObject) {
//        userWalletService.payForKali(requestObject.getData());
//        return success();
//    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "kali钱包充值记录", notes = "kali钱包充值记录", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<UserWalletUseRecordDto>> list(@RequestBody @Valid RequestObject<Pageable<String>> requestObject){
        return success(userWalletUseRecordService.listPage(requestObject.getData(), UserWalletUseEnum.RECHARGE.getCode()));
    }

}
