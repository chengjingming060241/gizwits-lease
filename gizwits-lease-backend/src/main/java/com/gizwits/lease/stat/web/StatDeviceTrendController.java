package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.stat.dto.StatDeviceTrendDto;
import com.gizwits.lease.stat.service.StatDeviceTrendService;
import com.gizwits.lease.stat.vo.StatTrendVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 设备趋势统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@EnableSwagger2
@Api(description = "设备趋势图")
@RestController
@RequestMapping("/stat/statDeviceTrend")
public class StatDeviceTrendController extends BaseController {
    @Autowired
    private StatDeviceTrendService statDeviceTrendService;
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "获取设备新增趋势图数据（productId选填）")
    @RequestMapping(value = "/newTrend", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatTrendVo>> newTrend(@RequestBody @Valid RequestObject<StatDeviceTrendDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatTrendVo> list = statDeviceTrendService.getNewTrend(requestObject.getData(), currentUser, ids);
        return success(list);
    }

    //活跃趋势
    @ApiOperation(value = "获取设备活跃趋势图数据（productId选填）")
    @RequestMapping(value = "/activeTrend", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatTrendVo>> activeTrend(@RequestBody @Valid RequestObject<StatDeviceTrendDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatTrendVo> list = statDeviceTrendService.getActiveTrend(requestObject.getData(), currentUser, ids);
        return success(list);
    }

    //设备订单率趋势
    @ApiOperation(value = "获取设备使用率趋势图数据（productId选填）")
    @RequestMapping(value = "/usePercentTrend", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatTrendVo>> usePercentTrend(@RequestBody @Valid RequestObject<StatDeviceTrendDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatTrendVo> list = statDeviceTrendService.getUsePercentTrend(requestObject.getData(), currentUser, ids);
        return success(list);
    }


}
