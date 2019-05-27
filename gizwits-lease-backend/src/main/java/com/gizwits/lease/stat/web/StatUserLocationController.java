package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.stat.service.StatUserLocationService;
import com.gizwits.lease.stat.vo.StatLocationVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * <p>
 * 用户地图分布统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-14
 */
@EnableSwagger2
@Api(description = "用户分布，分布排行")
@RestController
@RequestMapping("/stat/statUserLocation")
public class StatUserLocationController extends BaseController {
    @Autowired
    private StatUserLocationService statUserLocationService;
    @Autowired
    private SysUserService sysUserService;

    //用户地区分布
    @ApiOperation(value = "获取用户分布图（productId）")
    @RequestMapping(value = "/userDitribution", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatLocationVo>> ditribution() {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatLocationVo> list = statUserLocationService.getDitribution(currentUser, ids);
        return success(list);
    }

    //用户地区排行
    @ApiOperation(value = "获取用户分布图排行（productId）")
    @RequestMapping(value = "/userRank", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatLocationVo>> rank() {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatLocationVo> list = statUserLocationService.getRank(currentUser, ids);
        return success(list);
    }


}
