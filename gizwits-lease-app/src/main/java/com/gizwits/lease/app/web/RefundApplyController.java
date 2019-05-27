package com.gizwits.lease.app.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.refund.dto.RefundAddDto;
import com.gizwits.lease.refund.dto.RefundInfoDto;
import com.gizwits.lease.refund.service.RefundApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * <p>
 * 退款申请表 前端控制器
 * </p>
 *
 * @author Joke
 * @since 2018-02-08
 */
@RestController
@EnableSwagger2
@Api(description = "退款接口")
@RequestMapping("/app/refund")
public class RefundApplyController extends BaseController {

	@Autowired
	private RefundApplyService refundApplyService;

	@ApiOperation(value = "申请", notes = "申请",consumes = "application/json")
	@ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
	@RequestMapping(value = "/apply",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject apply(@RequestBody @Valid RequestObject<RefundAddDto> requestObject){
		refundApplyService.apply(requestObject.getData());
		return success();
	}

	@ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
	@ApiOperation(value = "检查是否有申请中或已审核的退款单", notes = "申请退款前，检查一下是否有申请中或已审核的退款单",consumes = "application/json")
	@RequestMapping(value = "/check",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject check(@RequestBody @Valid RequestObject<String> requestObject){
		RefundInfoDto refundInfoDto = refundApplyService.checkBeforeApply(requestObject.getData());
		return success(refundInfoDto);
	}

}
