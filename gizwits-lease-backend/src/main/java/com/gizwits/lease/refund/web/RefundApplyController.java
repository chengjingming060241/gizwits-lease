package com.gizwits.lease.refund.web;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.product.entity.Brand;
import com.gizwits.lease.refund.dto.*;
import com.gizwits.lease.refund.service.RefundApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;

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
@RequestMapping("/refund")
public class RefundApplyController extends BaseController{

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private RefundApplyService refundApplyService;

	@ApiOperation(value = "申请", notes = "申请",consumes = "application/json")
	@RequestMapping(value = "/apply",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject apply(@RequestBody @Valid RequestObject<RefundAddDto> requestObject){
		refundApplyService.apply(requestObject.getData());
		return success();
	}

	@ApiOperation(value = "审核", notes = "审核",consumes = "application/json")
	@RequestMapping(value = "/audit",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject audit(@RequestBody @Valid RequestObject<RefundAuditDto> requestObject){
		List<String> failIds = refundApplyService.audit(requestObject.getData());
		return success(failIds);
	}

	@ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
	@ApiOperation(value = "退款列表", notes = "退款列表",consumes = "application/json")
	@RequestMapping(value = "/list",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject<Page<RefundInfoDto>> list(@RequestBody @Valid RequestObject<Pageable<RefundListQueryDto>> requestObject){
		if (requestObject.getData().getQuery() == null) {
			requestObject.getData().setQuery(new RefundListQueryDto());
		}
		Page<RefundInfoDto> list = refundApplyService.list(requestObject.getData());
		return success(list);
	}

	@ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
	@ApiOperation(value = "退款详情", notes = "退款详情",consumes = "application/json")
	@RequestMapping(value = "/detail",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject detail(@RequestBody @Valid RequestObject<String> requestObject){
		RefundInfoDto detail = refundApplyService.detail(requestObject.getData());
		return success(detail);
	}

	@ApiOperation(value = "已选退款单统计", notes = "已选退款单统计",consumes = "application/json")
	@RequestMapping(value = "/statistics",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject checkedStatistics(@RequestBody @Valid RequestObject<List<String>> requestObject){
		RefundStatisticsDto dto = refundApplyService.checkedStatistics(requestObject.getData());
		return success(dto);
	}

	@ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
	@ApiOperation(value = "执行退款", notes = "执行退款",consumes = "application/json")
	@RequestMapping(value = "/refund",method = RequestMethod.POST)
	@ResponseBody
	public ResponseObject refund(@RequestBody @Valid RequestObject<List<String>> requestObject){
		List<String> failIds = refundApplyService.refund(requestObject.getData());
		return success(failIds);
	}
	
}
