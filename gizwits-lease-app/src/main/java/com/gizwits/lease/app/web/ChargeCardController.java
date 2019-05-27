package com.gizwits.lease.app.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.app.utils.BrowserUtil;
import com.gizwits.lease.order.dto.PrePayDto;
import com.gizwits.lease.trade.service.TradeWeixinService;
import com.gizwits.lease.user.dto.UserChargeCardBindDto;
import com.gizwits.lease.user.dto.UserChargeCardIdDto;
import com.gizwits.lease.user.dto.UserChargeCardOpenidDto;
import com.gizwits.lease.user.dto.UserChargeCardRechargeRecordDto;
import com.gizwits.lease.user.dto.UserChargeCardRechargeRecordQueryDto;
import com.gizwits.lease.user.service.UserChargeCardOrderService;
import com.gizwits.lease.user.service.UserChargeCardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Created by xpg on 30/08/2017.
 */
//@EnableSwagger2
@Api(description = "充值卡接口")
@RestController
@RequestMapping("/app/chargeCard")
public class ChargeCardController extends BaseController {

    @Autowired
    private UserChargeCardService userChargeCardService;

    @Autowired
    private UserChargeCardOrderService userChargeCardOrderService;

    @Autowired
    private TradeWeixinService tradeWeixinService;

    @ApiOperation("绑定充值卡")
    @RequestMapping(value = "/bind", method = RequestMethod.POST)
    public ResponseObject bindChargeCard(@RequestBody @Valid RequestObject<UserChargeCardBindDto> requestObject) {
        userChargeCardService.bindChargeCard(requestObject.getData());
        return success();
    }

    @ApiOperation("获取充值卡列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject getChargeCardList(@RequestBody @Valid RequestObject<UserChargeCardOpenidDto> requestObject) {
        return success(userChargeCardService.getChargeCardList(requestObject.getData()));
    }

    @ApiOperation("获取充值卡基本信息")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseObject getChargeCardDetail(@RequestBody @Valid RequestObject<UserChargeCardIdDto> requestObject) {
        return success(userChargeCardService.getChargeCardDetail(requestObject.getData()));
    }

    @ApiOperation("生成充值卡充值交易，微信预支付接口")
    @RequestMapping(value = "/recharge/prePay", method = RequestMethod.POST)
    public ResponseObject prePay(@RequestBody @Valid ResponseObject<PrePayDto> responseObject, HttpServletRequest request) {
        return tradeWeixinService.cardRechargePrePay(responseObject.getData(), BrowserUtil.getUserBrowserType(request));
    }

    @ApiOperation("获取充值卡充值记录")
    @RequestMapping(value = "/rechargeRecord", method = RequestMethod.POST)
    public ResponseObject<Page<UserChargeCardRechargeRecordDto>> getRechargeRecord(@RequestBody @Valid RequestObject<Pageable<UserChargeCardRechargeRecordQueryDto>> requestObject) {
        return success(userChargeCardOrderService.getRechargeRecord(requestObject.getData()));
    }

}
