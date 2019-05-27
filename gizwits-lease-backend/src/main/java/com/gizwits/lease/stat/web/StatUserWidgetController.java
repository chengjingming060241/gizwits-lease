package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.stat.service.StatUserWidgetService;
import com.gizwits.lease.stat.vo.StatUserWidgetVo;
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
 * 设备订单看板数据统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
@EnableSwagger2
@Api(description = "用户看板")
@RestController
@RequestMapping("/stat/statUserWidget")
public class StatUserWidgetController extends BaseController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StatUserWidgetService statUserWidgetService;

    @ApiOperation(value = "用户看板（productId不填时为全部）")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<StatUserWidgetVo> userWidget() {
        SysUser currentUser = sysUserService.getCurrentUser();
        List<Integer> ids = sysUserService.resolveAccessableUserIds(currentUser);
        StatUserWidgetVo statUserWidgetVo = statUserWidgetService.widget(currentUser, ids);
        return success(statUserWidgetVo);
    }
}
