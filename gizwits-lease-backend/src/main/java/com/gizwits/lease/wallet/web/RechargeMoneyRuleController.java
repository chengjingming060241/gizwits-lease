package com.gizwits.lease.wallet.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.wallet.dto.RechargeRuleForDetailDto;
import com.gizwits.lease.wallet.dto.RechargeRuleForEditDto;
import com.gizwits.lease.wallet.service.RechargeMoneyRuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * <p>
 * 充值优惠规则表 前端控制器
 * </p>
 *
 * @author Joke
 * @since 2017-10-11
 */
@RestController
@EnableSwagger2
@Api(description = " 充值优惠规则接口")
@RequestMapping("/wallet/rechargeMoneyRule")
public class RechargeMoneyRuleController extends BaseController{

	@Autowired
	private RechargeMoneyRuleService rechargeMoneyRuleService;

	@ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
	@ApiOperation(value = "编辑优惠规则",notes = "编辑优惠规则",consumes = "application/json")
	@RequestMapping(value = "/edit",method = RequestMethod.POST)
	public ResponseObject edit(@RequestBody @Valid RequestObject<RechargeRuleForEditDto> requestObject){
		rechargeMoneyRuleService.editRule(requestObject.getData());
		return success();
	}

	@ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
	@ApiOperation(value = "优惠规则详情",notes = "优惠规则详情",consumes = "application/json")
	@RequestMapping(value = "/detail",method = RequestMethod.POST)
	public ResponseObject detail(@RequestBody @Valid RequestObject<Integer> requestObject){
		RechargeRuleForDetailDto ruleDetail = rechargeMoneyRuleService.getRuleDetail(requestObject.getData());
		return success(ruleDetail);
	}
}
