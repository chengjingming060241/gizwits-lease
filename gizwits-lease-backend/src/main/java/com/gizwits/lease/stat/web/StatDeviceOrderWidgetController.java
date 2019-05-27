package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.stat.dto.StatDeviceDto;
import com.gizwits.lease.stat.service.StatDeviceOrderWidgetService;
import com.gizwits.lease.stat.vo.StatAlarmWidgetVo;
import com.gizwits.lease.stat.vo.StatDeviceWidgetVo;
import com.gizwits.lease.stat.vo.StatOrderWidgetVo;
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
 * 设备订单看板数据统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
@EnableSwagger2
@Api(description = "设备看板，订单看板，告警设备看板")
@RestController
@RequestMapping("/stat/statDeviceOrderWidget")
public class StatDeviceOrderWidgetController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StatDeviceOrderWidgetService statDeviceOrderWidgetService;

    //订单看板
    @ApiOperation(value = "订单看板（productId不填时为全部）")
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<StatOrderWidgetVo> orderWidget(@RequestBody @Valid RequestObject<StatDeviceDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        StatOrderWidgetVo statOrderWidgetVo = statDeviceOrderWidgetService.orderWidget(requestObject.getData().getProductId(), currentUser, ids);
        return success(statOrderWidgetVo);
    }

    //设备看板
    @ApiOperation(value = "设备看板（productId不填时为全部）")
    @RequestMapping(value = "/device", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<StatDeviceWidgetVo> deviceWidget(@RequestBody @Valid RequestObject<StatDeviceDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        if(ParamUtil.isNullOrEmptyOrZero(requestObject.getData())){
            StatDeviceDto deviceDto = new StatDeviceDto();
            deviceDto.setProductId(0);
            requestObject.setData(deviceDto);
        }
        StatDeviceWidgetVo statDeviceWidgetVo = statDeviceOrderWidgetService.deviceWidget(requestObject.getData().getProductId(), currentUser, ids);
        return success(statDeviceWidgetVo);
    }

    //故障看板
    @ApiOperation(value = "故障看板（productId不填时为全部）")
    @RequestMapping(value = "/alarm", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<StatAlarmWidgetVo> alarmWidget(@RequestBody @Valid RequestObject<StatDeviceDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        StatAlarmWidgetVo statAlarmWidgetVo = statDeviceOrderWidgetService.alarmWidget(requestObject.getData().getProductId(), currentUser, ids);
        return success(statAlarmWidgetVo);
    }

    @ApiOperation(value = "故障看板（productId不填时为全部）")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject test() {
        statDeviceOrderWidgetService.setDataForWidget();
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "15天内到期设备数、已上线离线设备数、水量剩余10%设备数、今日订单总额")
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ResponseObject<Map<String, Object>> index() {
        Map<String, Object> res = new HashMap<>(5);
        res.put("expDevices", statDeviceOrderWidgetService.count15DaysDevices());
        res.put("offDevices", statDeviceOrderWidgetService.countOffDevices());
        res.put("remainDevices", statDeviceOrderWidgetService.countRemain10Devices());
        res.put("totalAmount", statDeviceOrderWidgetService.sumTotalAmountToday());

        return success(res);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "看板运营商总金额排序", consumes = "application/json")
    @GetMapping(value = "/operatorSort")
    public ResponseObject<List<Map<String, Double>>> operatorSort() {
        return success(statDeviceOrderWidgetService.sumOperatorAndSort());
    }

}
