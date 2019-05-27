package com.gizwits.lease;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.gizwits")
@MapperScan(basePackages = {"com.gizwits.lease.*.dao"})
public class JobApplication extends SpringBootServletInitializer  {

	protected final static Logger logger = LoggerFactory.getLogger(JobApplication.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(JobApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		logger.info("机智云租赁平台启动成功,自由的玩耍吧!");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(JobApplication.class);
	}
}