package com.gizwits.lease.xzy.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.app.utils.BrowserUtil;
import com.gizwits.lease.order.dto.PrePayDto;
import com.gizwits.lease.trade.service.TradeAlipayService;
import com.gizwits.lease.user.service.UserAlipayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/15.
 */
@RestController
@Api(description = "支付宝支付接口")
@RequestMapping(value = "/xiangzhiyun/alipayPay")
@EnableSwagger2
public class AlipayPayController extends BaseController {

    @Autowired
    private UserAlipayService userAlipayService;

    @Autowired
    private  TradeAlipayService tradeAlipayService;


    @ApiOperation(value = "支付宝支付",notes = "支付宝支付",consumes = "application/json")
    @RequestMapping(value = "/pay",method = RequestMethod.POST)
    public ResponseObject pay(@RequestBody @Valid RequestObject<PrePayDto> requestObject, HttpServletRequest request){

        return success(tradeAlipayService.prePay(requestObject.getData(), BrowserUtil.getUserBrowserType(request)));
    }

    @ApiOperation(value = "支付宝订单支付状态查询接口", notes = "此接口主要是订单支付状态查询")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/queryStatus", method = RequestMethod.POST)
    public ResponseObject prePayQueryStatus(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(userAlipayService.queryOrderPayStatus(requestObject.getData()));
    }

    /**
     * 支付宝支付回调
     * @param request
     * @param resp
     */
    @RequestMapping(value = "alipay",method = RequestMethod.POST)
    public void alipayNotify(HttpServletRequest request, HttpServletResponse resp) {
        tradeAlipayService.alipayNotify(request,resp);
    }



}
