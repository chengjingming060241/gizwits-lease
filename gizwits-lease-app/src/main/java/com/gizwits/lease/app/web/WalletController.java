package com.gizwits.lease.app.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.constant.WalletEnum;
import com.gizwits.lease.wallet.dto.RechargeDto;
import com.gizwits.lease.wallet.dto.UserWalletDto;
import com.gizwits.lease.wallet.dto.UserWalletUseRecordDto;
import com.gizwits.lease.wallet.dto.WalletPayDto;
import com.gizwits.lease.wallet.entity.UserWallet;
import com.gizwits.lease.wallet.service.RechargeMoneyService;
import com.gizwits.lease.wallet.service.UserWalletService;
import com.gizwits.lease.wallet.service.UserWalletUseRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by GaGi on 2017/8/4.
 */
@EnableSwagger2
@Api(value = "提供操作钱包，余额的接口")
@RestController
@RequestMapping(value = "/app/wallet")
public class WalletController extends BaseController {
    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private RechargeMoneyService rechargeMoneyService;

    @Autowired
    private UserWalletUseRecordService userWalletUseRecordService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "当前余额", notes = "当前余额", consumes = "application/json")
    @RequestMapping(value = "/balanceForMobileOrOpenid", method = RequestMethod.POST)
    public ResponseObject<UserWallet> balanceForMobileOrOpenid(@RequestBody @Valid RequestObject<UserWalletDto> requestObject) {
        requestObject.getData().setWalletType(WalletEnum.BALENCE.getCode());
        return success(userWalletService.selectUserWallet(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "当前余额", notes = "当前余额", consumes = "application/json")
    @RequestMapping(value = "/balance", method = RequestMethod.POST)
    public ResponseObject<UserWallet> balance(@RequestBody @Valid RequestObject<UserWalletDto> requestObject) {
        return success(userWalletService.selectUserWallet(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "余额支付", notes = "余额支付", consumes = "application")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public ResponseObject pay(@RequestBody @Valid RequestObject<WalletPayDto> requestObject) {
        userWalletService.pay(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值页面", notes = "", consumes = "application/json")
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public ResponseObject recharge(@RequestBody @Valid RequestObject<UserWalletDto> requestObject) {
        RechargeDto rechargeDto = userWalletService.getRechargeDto(requestObject.getData());
        return success(rechargeDto);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "用户钱包充值记录", notes = "用户钱包充值记录", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<UserWalletUseRecordDto>> list(@RequestBody @Valid RequestObject<Pageable<String>> requestObject){
        return success(userWalletUseRecordService.listPage(requestObject.getData()));
    }
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值比例", notes = "充值比例", consumes = "application/json")
    @RequestMapping(value = "/rate", method = RequestMethod.POST)
    public ResponseObject<BigDecimal> rate(@RequestBody @Valid RequestObject<Integer> requestObject){

        return success(rechargeMoneyService.getRate(requestObject.getData()));
    }
}
