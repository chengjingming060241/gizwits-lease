package com.gizwits.lease.benefit.web;

import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.benefit.dto.*;
import com.gizwits.lease.benefit.service.ShareBenefitSheetService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 分润账单表 前端控制器
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@EnableSwagger2
@Api(description = "分润单")
@RestController
@RequestMapping("/benefit/shareBenefitSheet")
public class ShareBenefitSheetController extends BaseController {

    @Autowired
    private ShareBenefitSheetService shareBenefitSheetService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "列表", consumes = "application/json")
    @PostMapping("/page")
    public ResponseObject<Page<ShareBenefitSheetForListDto>> page(@RequestBody RequestObject<Pageable<ShareBenefitSheetForQueryDto>> requestObject) {
        Pageable<ShareBenefitSheetForQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new ShareBenefitSheetForQueryDto());
        }
        pageable.getQuery().setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        return success(shareBenefitSheetService.page(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详情", consumes = "application/json")
    @PostMapping("/detail")
    public ResponseObject<ShareBenefitSheetForDetailDto> detail(@RequestBody RequestObject<Integer> requestObject) {
        if (Objects.isNull(requestObject.getData())) {
            throw new IllegalArgumentException("参数不合法");
        }
        ShareBenefitSheetForQueryDto query = new ShareBenefitSheetForQueryDto();
        query.setId(requestObject.getData());
        query.setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        return success(shareBenefitSheetService.detail(query));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "审核", consumes = "application/json")
    @PostMapping("/audit")
    public ResponseObject<ShareBenefitSheetForDetailDto> audit(@RequestBody @Valid RequestObject<ShareBenefitSheetForAuditDto> requestObject) {
        if (CollectionUtils.isEmpty(requestObject.getData().getSheetOrderIds())) {
            LeaseException.throwSystemException(LeaseExceEnums.CHOOSE_SHARE_BENEFIT_ORDER);
        }
        return success(shareBenefitSheetService.audit(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "重新审核", consumes = "application/json")
    @PostMapping("/reaudit")
    public ResponseObject<ShareBenefitSheetForDetailDto> reaudit(@RequestBody @Valid RequestObject<ShareBenefitSheetForAuditDto> requestObject) {
        return success(shareBenefitSheetService.reaudit(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润", consumes = "application/json")
    @PostMapping("/execute")
    public ResponseObject executeShareBenefit(@RequestBody @Valid RequestObject<Integer> requestObject){
        if (Objects.isNull(requestObject.getData())) {
            throw new IllegalArgumentException("参数不合法");
        }
        ShareBenefitSheetForQueryDto query = new ShareBenefitSheetForQueryDto();
        query.setId(requestObject.getData());
        query.setAccessableUserIds(sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        return shareBenefitSheetService.executeShareBenefit(query);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "计算订单合计金额", consumes = "application/json")
    @PostMapping("/calculate")
    public ResponseObject calculateSheetMoney(@RequestBody @Valid RequestObject<ShareBenefitCalculateDto> requestObject){
        if (Objects.isNull(requestObject.getData())) {
            throw new IllegalArgumentException("参数不合法");
        }
        return success(shareBenefitSheetService.calculateSheetMoney(requestObject.getData().getSheetNo(),requestObject.getData().getExcludeOrderList()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润订单状态汇总", consumes = "application/json")
    @PostMapping("/group")
    public ResponseObject groupSheetOrder(@RequestBody @Valid RequestObject<String> requestObject){
        if (Objects.isNull(requestObject.getData())) {
            throw new IllegalArgumentException("参数不合法");
        }
        return success(shareBenefitSheetService.groupOrderByStatus(requestObject.getData()));
    }
}
