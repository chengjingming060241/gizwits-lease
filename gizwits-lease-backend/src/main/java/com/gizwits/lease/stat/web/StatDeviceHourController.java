package com.gizwits.lease.stat.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 设备在线时段分析统计表 前端控制器
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@EnableSwagger2
@Api(description = "设备时段分布")
@RestController
@RequestMapping("/stat/statDeviceHour")
public class StatDeviceHourController {
	
}
