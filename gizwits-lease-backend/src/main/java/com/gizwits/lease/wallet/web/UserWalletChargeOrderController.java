package com.gizwits.lease.wallet.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.constant.UserWalletChargeOrderType;
import com.gizwits.lease.constant.UserWalletUseEnum;
import com.gizwits.lease.constant.WalletEnum;
import com.gizwits.lease.order.dto.ChargeOrderDto;
import com.gizwits.lease.wallet.dto.DepositListDto;
import com.gizwits.lease.wallet.dto.DepositQueryDto;
import com.gizwits.lease.wallet.dto.UserWalletChargeListDto;
import com.gizwits.lease.wallet.dto.UserWalletChargeOrderQueryDto;
import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户钱包充值单表 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-07-31
 */
@RestController
@EnableSwagger2
@Api(description = " 钱包充值单接口")
@RequestMapping("/wallet/userWalletChargeOrder")
public class UserWalletChargeOrderController extends BaseController{

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "钱包充值单列表",notes = "钱包充值单列表",consumes = "application/json")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public ResponseObject<Page<DepositListDto>> list(@RequestBody @Valid RequestObject<Pageable<DepositQueryDto>> requestObject){
        DepositQueryDto queryDto = requestObject.getData().getQuery();
        if (queryDto == null) {
            queryDto = new DepositQueryDto();
        }
        //钱包类型为押金
        queryDto.setWalletType(WalletEnum.BALENCE.getCode());
        return success(userWalletChargeOrderService.listDeposit(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值列表模块看板数据", consumes = "application/json")
    @GetMapping("/rechargeData")
    public ResponseObject<Map<String, Double>> rechargeData() {
        // 充值总金额
        double realRecharge = userWalletChargeOrderService.sumRealRecharge();
        // 已消费金额
        double realUse = userWalletChargeOrderService.sumRealUse();
        // 充值剩余金额
        double realRemain = realRecharge - realUse;
        // 赠送总金额
        double realGive = userWalletChargeOrderService.sumRealGive();
        // 赠送金额余额
        double realGiveUse = userWalletChargeOrderService.sumRealGiveUse();
        double realGiveRemain = realGive - realGiveUse;
        // 总余额
        double remain = realRemain + realGiveRemain;

        Map<String, Double> res = new HashMap<>(8);
        res.put("realRecharge", new BigDecimal(realRecharge).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        res.put("realUse", new BigDecimal(realUse).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        res.put("realRemain", new BigDecimal(realRemain).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        res.put("realGive", new BigDecimal(realGive).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        res.put("realGiveRemain", new BigDecimal(realGiveRemain).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
        res.put("remain", new BigDecimal(remain).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());

        return success(res);
    }


    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "押金列表",notes = "押金列表",consumes = "application/json")
    @RequestMapping(value = "/depositList",method = RequestMethod.POST)
    public ResponseObject<Page<DepositListDto>> depositList(@RequestBody @Valid RequestObject<Pageable<DepositQueryDto>> requestObject){
        DepositQueryDto queryDto = requestObject.getData().getQuery();
        if (queryDto == null) {
            queryDto = new DepositQueryDto();
        }
        //钱包类型为押金
        queryDto.setWalletType(WalletEnum.DEPOSIT.getCode());
        return success(userWalletChargeOrderService.listDeposit(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "押金详情",notes = "押金详情",consumes = "application.json")
    @RequestMapping(value = "depositDetail",method = RequestMethod.POST)
    public ResponseObject<DepositListDto> depositDetail(@RequestBody @Valid RequestObject<String> requestObject){

        return success(userWalletChargeOrderService.depositDetail(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "退款信息",notes = "退款信息",consumes = "application/json")
    @RequestMapping(value = "/refundInfo",method = RequestMethod.POST)
    public ResponseObject refundInfo(@RequestBody @Valid RequestObject<String> requestObject){

        return success(userWalletChargeOrderService.refundInfo(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "退款",notes = "退款",consumes = "application/json")
    @RequestMapping(value = "/refund",method = RequestMethod.POST)
    public ResponseObject refund(@RequestBody @Valid RequestObject<String> requestObject) {
        userWalletChargeOrderService.refund(requestObject.getData());
        return success();
    }

    @ApiOperation(value = "创建充值单",notes = "创建充值单",consumes = "application/json")
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public ResponseObject<UserWalletChargeOrder> create(@RequestBody @Valid RequestObject<ChargeOrderDto> requestObject){

        return success(userWalletChargeOrderService.createRechargeOrder(requestObject.getData()));

    }

}
