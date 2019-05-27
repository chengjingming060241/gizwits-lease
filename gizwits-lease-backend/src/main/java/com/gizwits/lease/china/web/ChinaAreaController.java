package com.gizwits.lease.china.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.china.service.ChinaAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * <p>
 * 全国省市行政编码表 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-07-14
 */

@Controller
@EnableSwagger2
@Api(description = "全国省市行政区接口")
@RequestMapping("/China/chinaArea")
public class ChinaAreaController extends BaseController {
    @Autowired
    private ChinaAreaService chinaAreaService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "获得下一级地区",notes = "获得下一级地区",consumes = "application/json")
    @RequestMapping(value = "/area",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject area(@RequestBody @Valid RequestObject<Integer> code){
        return success(chinaAreaService.getAreas(code.getData()));
    }


}
