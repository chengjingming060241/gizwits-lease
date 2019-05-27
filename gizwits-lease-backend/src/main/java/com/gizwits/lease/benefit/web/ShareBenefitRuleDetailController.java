package com.gizwits.lease.benefit.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDetailNameDto;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailService;

import org.springframework.beans.factory.annotation.Autowired;
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
 * <p> 详细分润规则
 * 前端控制器
 * </p>
 * Created by yinhui on 2017/8/1.
 */
@RestController
@EnableSwagger2
@Api(description = "详细分润规则接口")
@RequestMapping(value = "/benefit/shareBenefitRuleDetail")
public class ShareBenefitRuleDetailController extends BaseController {

    @Autowired
    private ShareBenefitRuleDetailService shareBenefitRuleDetailService;


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除", notes = "删除", consumes = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseObject sonOperators(@RequestBody @Valid RequestObject<List<String>> requestObject) {
        shareBenefitRuleDetailService.deleteShareBenefitRuleDatailByIds(requestObject.getData());
        return success();
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "详细分润规则名是否存在", notes = "详细分润规则名是否存在", consumes = "application/json")
    @RequestMapping(value = "/detailNameIsExist", method = RequestMethod.POST)
    public ResponseObject detailNameIsExist(@RequestBody @Valid RequestObject<ShareBenefitRuleDetailNameDto> requestObject) {
        ShareBenefitRuleDetailNameDto nameDto = requestObject.getData();
        boolean flag;
        if (ParamUtil.isNullOrEmptyOrZero(nameDto.getRuleId())) {
            flag = false;
        } else {
            ShareBenefitRuleDetail shareBenefitRuleDetail = shareBenefitRuleDetailService.selectByRuleIdAndName(nameDto.getRuleId(), nameDto.getDetailName());
            flag = !ParamUtil.isNullOrEmptyOrZero(shareBenefitRuleDetail);
        }
        return success(flag);
    }
}
