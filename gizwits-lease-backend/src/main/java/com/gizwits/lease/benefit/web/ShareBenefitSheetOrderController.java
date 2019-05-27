package com.gizwits.lease.benefit.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.benefit.service.ShareBenefitSheetOrderService;
import com.gizwits.lease.order.entity.dto.SheetOrderForListDto;
import com.gizwits.lease.order.entity.dto.SheetOrderForQueryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 所有要参与分润的订单 前端控制器
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@Api(description = "参与分润的订单")
@EnableSwagger2
@RestController
@RequestMapping("/benefit/shareBenefitSheetOrder")
public class ShareBenefitSheetOrderController extends BaseController {

    @Autowired
    private ShareBenefitSheetOrderService shareBenefitSheetOrderService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/list")
    public ResponseObject<Page<SheetOrderForListDto>> page(@RequestBody RequestObject<Pageable<SheetOrderForQueryDto>> requestObject) {

        return success(shareBenefitSheetOrderService.page(requestObject.getData()));
    }
}
