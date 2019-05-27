package com.gizwits.lease;

import com.gizwits.boot.listener.ApplicationListenerReady;
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

@SpringBootApplication
@ComponentScan("com.gizwits")
//@EnableAutoConfiguration
@MapperScan(basePackages = {"com.gizwits.lease.*.dao"})
public class Application  extends SpringBootServletInitializer {

	protected final static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.addListeners(new ApplicationListenerReady());
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		logger.info("机智云租赁平台'运营系统服务端'启动成功,自由的玩耍吧!");
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}

}