package com.gizwits.lease.app.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.china.service.ChinaAreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

/**
 * Created by zhl on 2017/9/8.
 */
@RestController
@EnableSwagger2
@Api(description = "全国省市行政区接口")
@RequestMapping("/app/chinaArea")
public class AreaController extends BaseController{
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
