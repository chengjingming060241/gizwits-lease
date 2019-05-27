package com.gizwits.lease.xzy.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.order.dto.OrderQueryByMobileDto;
import com.gizwits.lease.order.dto.WXOrderListDto;
import com.gizwits.lease.order.dto.WXOrderQueryDto;
import com.gizwits.lease.order.service.OrderBaseService;

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
 * Created by yinhui on 2017/8/10.
 */
@RestController(value = "xzy")
@EnableSwagger2
@Api(description = "享智云订单接口")
@RequestMapping(value = "/xiangzhiyun/xOrder")
public class OrderController extends BaseController{

    @Autowired
    private OrderBaseService orderBaseService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = "分页列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<WXOrderListDto>> list(@RequestBody @Valid RequestObject<Pageable<OrderQueryByMobileDto>> requestObject) {

        return success(orderBaseService.WxOrderListPage(requestObject.getData()));
    }
}
