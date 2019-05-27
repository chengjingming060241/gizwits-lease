package com.gizwits.lease.app.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.app.utils.BrowserUtil;

import com.gizwits.lease.device.vo.DeviceUsingVo;
import com.gizwits.lease.order.dto.*;

import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.dto.ChargeOrderDto;
import com.gizwits.lease.order.dto.DepositOrderDto;
import com.gizwits.lease.order.dto.OrderListDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.dto.PageOrderAppList;
import com.gizwits.lease.order.dto.PayOrderDto;
import com.gizwits.lease.order.dto.WXOrderListDto;
import com.gizwits.lease.order.dto.WXOrderQueryDto;
import com.gizwits.lease.order.entity.OrderBase;

import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.order.vo.AppOrderDetailVo;
import com.gizwits.lease.order.vo.AppOrderVo;
import com.gizwits.lease.user.dto.ChargeCardOrderDto;
import com.gizwits.lease.user.entity.UserChargeCardOrder;
import com.gizwits.lease.user.service.UserChargeCardOrderService;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by GaGi on 2017/7/31.
 */
@EnableSwagger2
@Api(description = "提供用户订单接口包含创建订单，查看订单，删除订单")
@RestController
@RequestMapping("/app/order")
public class OrderController extends BaseController {
    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;
    @Autowired
    private UserChargeCardOrderService userChargeCardOrderService;

    @ApiOperation(value = "用户端创建消费订单", notes = "此接口主要用户端下单")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/consume/order", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject<AppOrderVo> consumeOrder(@RequestBody @Valid RequestObject<PayOrderDto> requestObject, HttpServletRequest request) {
        AppOrderVo order = orderBaseService.createOrder(requestObject.getData(), BrowserUtil.getUserBrowserType(request));
        return success(order);
    }

    @ApiOperation(value = "用户端创建充值订单", notes = "此接口主要用户端下充值单")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/charge/order", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject chargeOrder(@RequestBody @Valid RequestObject<ChargeOrderDto> requestObject) {
        UserWalletChargeOrder userWalletChargeOrder = userWalletChargeOrderService.createRechargeOrder(requestObject.getData());
        return success(userWalletChargeOrder);
    }

    @ApiOperation(value = "用户端创建押金订单", notes = "此接口主要用户端下押金单")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/deposit/order", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject depositOrder(@RequestBody @Valid RequestObject<DepositOrderDto> requestObject) {
        UserWalletChargeOrder userWalletChargeOrder = userWalletChargeOrderService.createDepositOrder(requestObject.getData());
        return success(userWalletChargeOrder);
    }


    @ApiOperation(value = "用户端卡订单创建", notes = "此接口主要用户端下卡单")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/card/order", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject chargeCardOrder(@RequestBody @Valid RequestObject<ChargeCardOrderDto> requestObject) {
        UserChargeCardOrder userChargeCardOrder=userChargeCardOrderService.createChargeCardOrder(requestObject.getData());
        return success(userChargeCardOrder);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = "分页列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<WXOrderListDto>> list(@RequestBody @Valid RequestObject<Pageable<WXOrderQueryDto>> requestObject) {
        return success(orderBaseService.getWXOrderListPage(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "查询待支付订单", notes = "待支付", consumes = "application/json")
    @RequestMapping(value = "/paying", method = RequestMethod.POST)
    public ResponseObject<WXOrderListDto> paying(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(orderBaseService.getWxPayingOrder(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "批量删除订单", notes = "删除", consumes = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseObject delete(@RequestBody @Valid RequestObject<List<String>> requestObject) {
        orderBaseService.deleteUserShowOrder(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "用户端查询使用中订单", notes = "查询当前openid使用中的订单")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/using/order", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject<AppOrderVo> getUsingOrder(@RequestBody @Valid RequestObject<AppUsingOrderDto> requestObject) {
        AppOrderVo order = orderBaseService.getForAppOrder(requestObject.getData());
        return success(order);
    }


    @ApiOperation(value = "用户端查询使用中设备", notes = "查询当前openid使用中的设备")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/using/device", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject<List<DeviceUsingVo>> getUsingDevice(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(orderBaseService.getUsingDeviceList(requestObject.getData()));
    }

    @ApiOperation(value = "订单详情", notes = "订单详情")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseObject<AppOrderDetailVo> getOrderDetail(@RequestBody @Valid RequestObject<String> requestObject) {
        return success(orderBaseService.getOrderDetailForApp(requestObject.getData()));

    }
    
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "App端订单列表", consumes = "application/json")
    @PostMapping("/appOrderList")
    public ResponseObject<PageOrderAppList<OrderListDto>> appList(@RequestBody RequestObject<OrderQueryDto> requestObject) {
        return success(orderBaseService.getOrderAppListDtoPage(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "App端订单详情",notes = "订单详情",consumes = "application/json")
    @RequestMapping(value = "/orderAppDetail",method = RequestMethod.POST)
    public ResponseObject orderAppDetail(@RequestBody @Valid ResponseObject<String> responseObject){
        OrderBase orderBase = orderBaseService.getOrderBaseByOrderNo(responseObject.getData());
        return  success(orderBaseService.orderAppDetail(orderBase));

    }
}
