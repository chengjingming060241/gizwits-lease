package com.gizwits.lease.qek.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.message.entity.SysMessage;
import com.gizwits.lease.message.service.SysMessageService;
import com.gizwits.lease.order.service.OrderBaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/23.
 */
@RestController
@EnableSwagger2
@Api(description = "沁尔康订单")
@RequestMapping("/app/qek/order")
public class QekOrderController extends BaseController {

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private SysMessageService sysMessageService;



    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "订单消息", consumes = "application/json")
    @PostMapping("/orderMess")
    public ResponseObject<Page<SysMessage>> orderMess(@RequestBody @Valid RequestObject<Pageable<String>> requestObject) {

        return success(sysMessageService.getUserOrderMessage(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "结束订单", consumes = "application/json")
    @PostMapping("/closeOrder")
    public ResponseObject closeOrder(@RequestBody @Valid RequestObject<String> requestObject) {
        orderBaseService.closeOrder(requestObject.getData());
        return success();
    }
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "清空订单消息", consumes = "application/json")
    @PostMapping("/clearOrder")
    public ResponseObject<Page<SysMessage>> clearOrder(@RequestBody @Valid RequestObject<String> requestObject) {
        sysMessageService.clearOrder(requestObject.getData());
        return success();
    }
}
