package com.gizwits.lease.stat.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 控制设备时段用户统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@EnableSwagger2
@Api(description = "用户时段分布")
@RestController
@RequestMapping("/stat/statUserHour")
public class StatUserHourController {
	
}
