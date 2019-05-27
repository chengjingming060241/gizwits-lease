package com.gizwits.lease.app.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.order.dto.OrderTimerAddDto;
import com.gizwits.lease.order.dto.OrderTimerEnableDto;
import com.gizwits.lease.order.dto.OrderTimerUpdateDto;
import com.gizwits.lease.order.entity.OrderTimer;
import com.gizwits.lease.order.service.OrderTimerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 订单定时任务 前端控制器
 * </p>
 *
 * @author zhl
 * @since 2017-08-09
 */
@EnableSwagger2
@Api(value = "设备定时任务")
@RestController
@RequestMapping("/order/orderTimer")
public class OrderTimerController extends BaseController {

    @Autowired
    private OrderTimerService orderTimerService;

    @ApiOperation(value = "获取指定订单的定时任务", notes = "移动端获取订单定时任务")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/getTimer", method = RequestMethod.POST)
    public @ResponseBody ResponseObject<List<OrderTimer>> getTimers(@RequestBody @Valid RequestObject<String> requestObject) {

        return success(orderTimerService.getOrderTimersByOrderNo(requestObject.getData()));
    }

    @ApiOperation(value = "创建定时任务", notes = "移动端创建订单定时任务")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody ResponseObject createTimer(@RequestBody @Valid RequestObject<OrderTimerAddDto> requestObject) {

        orderTimerService.createOrderTimer(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "修改定时任务", notes = "移动端修改订单定时任务")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public @ResponseBody ResponseObject updateTimer(@RequestBody @Valid RequestObject<OrderTimerUpdateDto> requestObject) {

        return success();
    }

    @ApiOperation(value = "删除定时任务", notes = "移动端删除订单定时任务")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody ResponseObject deleteTimer(@RequestBody @Valid RequestObject<List<Integer>> requestObject) {

        return success();
    }

    @ApiOperation(value = "启警定时任务", notes = "移动端启警定时任务")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/enable", method = RequestMethod.POST)
    public @ResponseBody ResponseObject switchEnableTimer(@RequestBody @Valid RequestObject<OrderTimerEnableDto> requestObject) {

        return success();
    }

}

