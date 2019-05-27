package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.stat.dto.StatFaultDto;
import com.gizwits.lease.stat.service.StatFaultAlertTypeService;
import com.gizwits.lease.stat.vo.StatFaultVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-08-15
 */
@Api(description = "售后类型柱状图")
@EnableSwagger2
@RestController
@RequestMapping("/stat/statFaultAlertType")
public class StatFaultAlertTypeController extends BaseController {
    @Autowired
    private StatFaultAlertTypeService statFaultAlertTypeService;
    @Autowired
    private SysUserService sysUserService;

    //售后类型比例
    @ApiOperation(value = "获取售后类型比例")
    @RequestMapping(value = "/faultType", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<StatFaultVo>> faultType(@RequestBody RequestObject<StatFaultDto> requestObject) {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        List<StatFaultVo> list = statFaultAlertTypeService.getDataForStat(requestObject.getData(), currentUser, ids);
        return success(list);
    }
}
