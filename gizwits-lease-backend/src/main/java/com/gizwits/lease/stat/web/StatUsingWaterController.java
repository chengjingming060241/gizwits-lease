package com.gizwits.lease.stat.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.lease.stat.dto.StatUsingWaterDto;
import com.gizwits.lease.stat.service.StatUsingWaterService;
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
import java.util.Map;

/**
 * @author Jin
 * @date 2019/2/25
 */
@EnableSwagger2
@Api(description = "用水时段分析")
@RestController
@RequestMapping("/stat/statUsingWater")
public class StatUsingWaterController extends BaseController {
    @Autowired
    private StatUsingWaterService statUsingWaterService;

    @ApiOperation(value = "获取用水时段分析", notes = "获取用水时段分析")
    @RequestMapping(value = "getHourAnalysis", method = RequestMethod.POST)
    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    public ResponseObject<List<Map<Integer, Integer>>> getHourAnalysis(@RequestBody RequestObject<StatUsingWaterDto> requestObject) {
        StatUsingWaterDto query = requestObject.getData();
        return success(statUsingWaterService.getHourAnalysis(query.getFromDate(), query.getToDate(), query.getLaunchAreaName(), query.getOperator()));
    }

}
