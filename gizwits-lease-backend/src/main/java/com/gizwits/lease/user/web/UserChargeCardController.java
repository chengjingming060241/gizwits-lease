package com.gizwits.lease.user.web;

import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.constant.PayType;
import com.gizwits.lease.order.dto.OrderListDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.user.dto.UserChargeCardChargeHistoryListDto;
import com.gizwits.lease.user.dto.UserChargeCardDetailDto;
import com.gizwits.lease.user.dto.UserChargeCardListDto;
import com.gizwits.lease.user.dto.UserChargeCardOperationDto;
import com.gizwits.lease.user.dto.UserChargeCardOperationListDto;
import com.gizwits.lease.user.dto.UserChargeCardOperationRecordQueryDto;
import com.gizwits.lease.user.dto.UserChargeCardOrderQueryDto;
import com.gizwits.lease.user.dto.UserChargeCardQueryDto;
import com.gizwits.lease.user.service.UserChargeCardOperationRecordService;
import com.gizwits.lease.user.service.UserChargeCardOrderService;
import com.gizwits.lease.user.service.UserChargeCardService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 充值卡 前端控制器
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@Api(description = "充值卡")
@RestController
@RequestMapping("/user/userChargeCard")
public class UserChargeCardController extends BaseController {

    @Autowired
    private UserChargeCardService userChargeCardService;

    @Autowired
    private OrderBaseService orderBaseService;

    @Autowired
    private UserChargeCardOrderService userChargeCardOrderService;

    @Autowired
    private UserChargeCardOperationRecordService userChargeCardOperationRecordService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<Page<UserChargeCardListDto>> list(@RequestBody RequestObject<Pageable<UserChargeCardQueryDto>> requestObject) {
        Pageable<UserChargeCardQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new UserChargeCardQueryDto());
        }
        return success(userChargeCardService.list(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<UserChargeCardDetailDto> detail(@RequestBody @Valid RequestObject<Integer> requestObject) {
        return success(userChargeCardService.detail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "启用", consumes = "application/json")
    @PostMapping("/enable")
    public ResponseObject<Boolean> enable(@RequestBody @Valid RequestObject<UserChargeCardOperationDto> requestObject) {
        return success(userChargeCardService.enable(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "禁用", consumes = "application/json")
    @PostMapping("/disable")
    public ResponseObject<Boolean> disable(@RequestBody @Valid RequestObject<UserChargeCardOperationDto> requestObject) {
        return success(userChargeCardService.disable(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值卡订单", consumes = "application/json")
    @PostMapping("/orders")
    public ResponseObject<Page<OrderListDto>> listByCardNum(@RequestBody RequestObject<Pageable<OrderQueryDto>> requestObject) {
        Pageable<OrderQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery()) || Objects.isNull(pageable.getQuery().getPayCardNum())) {
            throw new IllegalArgumentException();
        }
        pageable.getQuery().setPayType(PayType.CARD.getCode());
        return success(orderBaseService.getOrderListDtoPage(pageable.getQuery()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值记录", consumes = "application/json")
    @PostMapping("/chargeRecords")
    public ResponseObject<Page<UserChargeCardChargeHistoryListDto>> chargeRecord(@RequestBody @Valid RequestObject<Pageable<UserChargeCardOrderQueryDto>> requestObject) {
        return success(userChargeCardOrderService.list(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "操作记录", consumes = "application/json")
    @PostMapping("/operateRecords")
    public ResponseObject<Page<UserChargeCardOperationListDto>> operateRecord(@RequestBody @Valid RequestObject<Pageable<UserChargeCardOperationRecordQueryDto>> requestObject) {
        return success(userChargeCardOperationRecordService.list(requestObject.getData()));
    }

}
