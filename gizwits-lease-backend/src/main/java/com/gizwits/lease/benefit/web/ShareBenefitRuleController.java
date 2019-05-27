package com.gizwits.lease.benefit.web;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.benefit.dto.ShareBenefitDeviceRangeDto;
import com.gizwits.lease.benefit.dto.ShareBenefitDeviceRangeVo;
import com.gizwits.lease.benefit.dto.ShareBenefitOperatorObjectDto;
import com.gizwits.lease.benefit.entity.dto.*;
import com.gizwits.lease.benefit.service.ShareBenefitRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p> 分润规则
 * 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@RestController
@EnableSwagger2
@Api(description = "分润规则接口")
@RequestMapping("/benefit/shareBenefitRule")
public class ShareBenefitRuleController extends BaseController {

    @Autowired
    private ShareBenefitRuleService shareBenefitRuleService;

    @Autowired
    private SysUserService sysUserService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润规则列表", notes = "分润规则列表", consumes = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseObject<Page<ShareBenefitRuleListDto>> list(@RequestBody @Valid RequestObject<Pageable<ShareBenefitRuleQueryDto>> requestObject) {
        Pageable<ShareBenefitRuleQueryDto> pageable = requestObject.getData();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new ShareBenefitRuleQueryDto());
        }
        pageable.getQuery().setIsDeleted(DeleteStatus.NOT_DELETED.getCode());
        pageable.getQuery().setCreatorId(sysUserService.getCurrentUser().getId());
        return success(shareBenefitRuleService.listPage(pageable));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润详情", notes = "分润详情", consumes = "application/json")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public ResponseObject<ShareBenefitRuleDto> detail(@RequestBody @Valid RequestObject<String> requestObject) {

        return success(shareBenefitRuleService.shareBenefitRuleDetail(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润添加", notes = "分润添加", consumes = "application/json")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseObject add(@RequestBody @Valid RequestObject<ShareBenefitRuleDto> requestObject) {
        shareBenefitRuleService.insertShareBenefitRule(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "子运营者", notes = "分润规则添加对应对象", consumes = "application/json")
    @RequestMapping(value = "/sonOperators", method = RequestMethod.POST)
    public ResponseObject<List<ShareBenefitOperatorObjectDto>> sonOperators() {
        return success(shareBenefitRuleService.listShareRuleOperatorObject());
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "所拥有设备", notes = "所拥有设备", consumes = "application/json")
    @RequestMapping(value = "/devices", method = RequestMethod.POST)
    public ResponseObject devices(@RequestBody @Valid RequestObject<Pageable<OperatorDeviceDto>> requestObject) {

        return success(shareBenefitRuleService.listDeviceByOperatorId(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "获取分润比例区间", notes = "获取一批设备的分润区间", consumes = "application/json")
    @RequestMapping(value = "/range", method = RequestMethod.POST)
    public ResponseObject<ShareBenefitDeviceRangeVo> rulePercentageRange(@RequestBody @Valid RequestObject<ShareBenefitDeviceRangeDto> requestObject) {

        return success(shareBenefitRuleService.calculateShareRuleRange(requestObject.getData().getSnoList(),requestObject.getData().getSysAccountId()));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新", notes = "更新", consumes = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseObject update(@RequestBody @Valid RequestObject<ShareBenefitRuleDto> requestObject) {
        shareBenefitRuleService.updateShareBenefitRule(requestObject.getData());
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分润规则名是否存在", notes = "分润规则名是否存在", consumes = "application/json")
    @RequestMapping(value = "/nameIsExist", method = RequestMethod.POST)
    public ResponseObject nameIsExist(@RequestBody @Valid RequestObject<ShareBenefitRuleNameCheckDto> requestObject) {
        return success(shareBenefitRuleService.selectNameIsExist(requestObject.getData()));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseObject delete(@RequestBody @Valid RequestObject<List<String>> requestObject) {

        String result = shareBenefitRuleService.deleteShareBenefitRule(requestObject.getData());
        return success(result);
    }
}
