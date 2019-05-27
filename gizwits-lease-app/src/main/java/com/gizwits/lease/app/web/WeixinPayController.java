package com.gizwits.lease.app.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.app.utils.BrowserUtil;
import com.gizwits.lease.order.dto.PrePayDto;
import com.gizwits.lease.trade.service.TradeWeixinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * Created by zhl on 2017/6/30.
 */
@EnableSwagger2
@Api(value = "微信支付,微信退款")
@RestController
@RequestMapping(value = "/app/wxPay")
public class WeixinPayController extends BaseController {

    @Autowired
    private TradeWeixinService tradeWeixinService;


    @ApiOperation(value = "微信预支付接口，点击前往支付就调用该接口", notes = "此接口主要是预支付接口")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/prePayForBrowserUtil", method = RequestMethod.POST)
    public ResponseObject prePayForBrowserUtil(@RequestBody @Valid ResponseObject<PrePayDto> responseObject, HttpServletRequest request) {
        return tradeWeixinService.prePay(responseObject.getData(), BrowserUtil.getUserBrowserType(request));
    }

    @ApiOperation(value = "微信预支付接口，点击前往支付就调用该接口", notes = "此接口主要是预支付接口")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/prePay", method = RequestMethod.POST)
    public ResponseObject prePay(@RequestBody @Valid ResponseObject<PrePayDto> responseObject, HttpServletRequest request) {
        return tradeWeixinService.prePay(responseObject.getData(),BrowserUtil.getUserBrowserType(request));
    }

    @ApiOperation(value = "微信订单支付状态查询接口", notes = "此接口主要是订单支付状态查询")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/queryStatus", method = RequestMethod.POST)
    public ResponseObject prePayQueryStatus(@RequestBody @Valid ResponseObject<String> responseObject) {
        return success(tradeWeixinService.queryOrderPayStatus(responseObject.getData()));
    }

    /**
     * 微信支付回调
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/wxPayNotify")
    @ApiIgnore
    public void wxPayNotify(HttpServletRequest request, HttpServletResponse response) {
        tradeWeixinService.handleWxPayCallback(request, response);
    }


}
