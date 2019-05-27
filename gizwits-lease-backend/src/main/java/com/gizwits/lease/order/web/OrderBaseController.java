package com.gizwits.lease.order.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.constant.OrderStatus;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.dto.OrderListDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.entity.OrderBase;
import com.gizwits.lease.order.service.OrderBaseService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
@RestController
@EnableSwagger2
@Api(description = "订单接口")
@RequestMapping("/order/orderBase")
public class OrderBaseController extends BaseController{
    protected Logger logger = LoggerFactory.getLogger("ORDER_LOGGER");
    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表",notes = "分页列表",consumes = "application/json")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject list(@RequestBody @Valid RequestObject<OrderQueryDto> requestObject){
        SysUser sysUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(sysUser);
        OrderQueryDto orderQueryDto  = requestObject.getData();
        orderQueryDto.setSysUserIds(ids);
        Page<OrderListDto> page = orderBaseService.getOrderListDtoPage(orderQueryDto);
        return success(page);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "运营商下的订单列表", consumes = "application/json")
    @PostMapping("/listByOperator")
    public ResponseObject<Page<OrderListDto>> listByOperator(@RequestBody RequestObject<OrderQueryDto> requestObject) {
        return success(orderBaseService.getOrderListDtoPage(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "订单详情",notes = "订单详情",consumes = "application/json")
    @RequestMapping(value = "/orderDetail",method = RequestMethod.POST)
    public ResponseObject orderDetail(@RequestBody @Valid ResponseObject<String> responseObject){
        OrderBase orderBase = orderBaseService.getOrderBaseByOrderNo(responseObject.getData());
        return  success(orderBaseService.orderDetail(orderBase));
    }

    @ApiOperation(value = "将订单变为已完成", notes = "将订单变为已完成", consumes = "application/json")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/finish/order", method = RequestMethod.POST)
    public ResponseObject finishOrder(@RequestBody @Valid RequestObject<String> requestObject) {
        orderBaseService.finish(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "App端订单详情",notes = "订单详情",consumes = "application/json")
    @RequestMapping(value = "/orderAppDetail",method = RequestMethod.POST)
    public ResponseObject orderAppDetail(@RequestBody @Valid ResponseObject<String> responseObject){
        OrderBase orderBase = orderBaseService.getOrderBaseByOrderNo(responseObject.getData());
        return  success(orderBaseService.orderAppDetail(orderBase));
    }

    @ApiOperation(value = "将异常订单变为已完成", notes = "将异常订单变为已完成", consumes = "application/json")
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @RequestMapping(value = "/finish", method = RequestMethod.POST)
    public ResponseObject finish(@RequestBody @Valid RequestObject<String> requestObject) {
        OrderBase orderBase = orderBaseService.selectById(requestObject.getData());
        if (!orderBase.getOrderStatus().equals(OrderStatus.ABNORMAL.getCode()) &&
                !orderBase.getOrderStatus().equals(OrderStatus.USING.getCode())) {
            LeaseException.throwSystemException(LeaseExceEnums.ORDER_STATUS_ERROR);
        }
        orderBaseService.updateOrderStatusAndHandle(orderBase, OrderStatus.FINISH.getCode());
        return success();
    }
}
