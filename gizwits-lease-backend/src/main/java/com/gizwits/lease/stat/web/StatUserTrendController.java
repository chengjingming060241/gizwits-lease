package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.stat.dto.StatUserTrendDto;
import com.gizwits.lease.stat.service.StatUserTrendService;
import com.gizwits.lease.stat.vo.StatSexVo;
import com.gizwits.lease.stat.vo.StatTimesVo;
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
 * 用户趋势及性别，使用次数统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@EnableSwagger2
@Api(description = "用户趋势图")
@RestController
@RequestMapping("/stat/statUserTrend")
public class StatUserTrendController extends BaseController {
    @Autowired
    private StatUserTrendService statUserTrendService;
    @Autowired
    private SysUserService sysUserService;

    //新增用户趋势接口
    @ApiOperation(value = "获取新增用户趋势图接口")
    @RequestMapping(value = "/newTrend", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatTrendVo>> newTrend(@RequestBody @Valid ResponseObject<StatUserTrendDto> responseObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatTrendVo> list = statUserTrendService.getNewTrend(currentUser, responseObject.getData(), ids);
        return success(list);
    }

    //活跃用户趋势接口
    @ApiOperation(value = "获取活跃用户趋势图接口")
    @RequestMapping(value = "/activeTrend", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatTrendVo>> activeTrend(@RequestBody @Valid ResponseObject<StatUserTrendDto> responseObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatTrendVo> list = statUserTrendService.getActiveTrend(currentUser, responseObject.getData(), ids);
        return success(list);
    }

    //总数用户趋势接口
    @ApiOperation(value = "获取用户总数趋势图接口")
    @RequestMapping(value = "/totalTrend", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatTrendVo>> totalTrend(@RequestBody @Valid RequestObject<StatUserTrendDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatTrendVo> list = statUserTrendService.getTotalTrend(currentUser, requestObject.getData(), ids);
        return success(list);
    }

    //用户性别分布
    @ApiOperation(value = "获取用户性别分布图接口")
    @RequestMapping(value = "/sex", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<StatSexVo> sexDistribution() {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        StatSexVo statSex = statUserTrendService.getSexDistribution(currentUser, ids);
        return success(statSex);
    }

    //使用次数占比
    @ApiOperation(value = "获取用户性别分布图接口")
    @RequestMapping(value = "/times", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<StatTimesVo> timesDistribution() {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        StatTimesVo statTimes = statUserTrendService.getTimesDistribution(currentUser, ids);
        return success(statTimes);
    }
}
