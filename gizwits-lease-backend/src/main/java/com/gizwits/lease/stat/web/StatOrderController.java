package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.constant.QrcodeType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.product.entity.Product;
import com.gizwits.lease.stat.dto.StatOrderAnalysisDto;
import com.gizwits.lease.stat.service.StatDeviceOrderWidgetService;
import com.gizwits.lease.stat.service.StatOrderService;
import com.gizwits.lease.stat.service.StatUserTrendService;
import com.gizwits.lease.stat.service.StatUserWidgetService;
import com.gizwits.lease.stat.vo.StatOrderAnalysisVo;
import com.gizwits.lease.user.service.UserWeixinService;
import com.gizwits.lease.util.QrcodeUtil;
import com.gizwits.lease.util.WxUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单分析统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@EnableSwagger2
@Api(description = "订单分析图")
@RestController
@RequestMapping("/stat/statOrder")
public class StatOrderController extends BaseController {
    @Autowired
    private StatOrderService statOrderService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StatUserWidgetService statUserWidgetService;
    @Autowired
    StatUserTrendService statUserTrendService;

    @Autowired
    private StatDeviceOrderWidgetService statDeviceOrderWidgetService;

    @ApiOperation(value = "获取折柱图接口（包含金额和数量和增长率,productId必填）")
    @RequestMapping(value = "/analysis", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<Map<String, Object>> orderAnalysis(@RequestBody @Valid RequestObject<StatOrderAnalysisDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);

        // 订单数据、金额
        List<StatOrderAnalysisVo> statOrderAnalysisDtoList = statOrderService.getOrderAnalysis(requestObject.getData(), currentUser, ids);

        // 平均客单价、机均订单数、机均金额
        Map<String, Double> avgOrder = statOrderService.getOrderAvgAnalysis(requestObject.getData());

        // 统计各时段的订单数
        List<Map<Integer, Integer>> orderCountList = statOrderService.getOrderCountAnalysis(requestObject.getData());

        // 统计各时段订单金额
        List<Map<Integer, Double>> orderMoneyList = statOrderService.getOrderMoneyAnalysis(requestObject.getData());

        // 统计投放点金额排序
        List<Map<String, Double>> areaMoneyList = statOrderService.getOrderMoneyAreaAnalysis(requestObject.getData());

        // 统计机器金额排序
        List<Map<String, Double>> machineMoneyList = statOrderService.getOrderMoneyMachineAnalysis(requestObject.getData());

        Map<String, Object> res = new HashMap<>(16);
        
        res.putAll(avgOrder);
        res.put("list", statOrderAnalysisDtoList);
        res.put("orderCountList", orderCountList);
        res.put("orderMoneyList", orderMoneyList);
        res.put("areaMoneyList", areaMoneyList);
        res.put("machineMoneyList", machineMoneyList);

        return success(res);
    }


    @ApiOperation(value = "测试订单分析")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject test() {
        statOrderService.setDataForStatOrder();
        return success();
    }


    @ApiOperation(value = "测试设备订单看板")
    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject  test2() {
    statDeviceOrderWidgetService.setDataForWidget();
        return success();
    }

    @ApiOperation(value = "测试用户看板")
    @RequestMapping(value = "/test3", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject  test3() {
       statUserWidgetService.setDataForWidget();
        return success();
    }

    @ApiOperation(value = "测试用户趋势")
    @RequestMapping(value = "/test4", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject  test4() {
        statUserTrendService.setDataForStatUserTrend();
        return success();
    }
}
