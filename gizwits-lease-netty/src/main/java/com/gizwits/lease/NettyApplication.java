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
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by zhl on 2017/7/12.
 */
@EnableSwagger2
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.gizwits")
@MapperScan(basePackages = {"com.gizwits.lease.*.dao"})
@EnableScheduling
public class NettyApplication extends SpringBootServletInitializer {

    protected final static Logger logger = LoggerFactory.getLogger(NettyApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(NettyApplication.class);
        app.addListeners(new ApplicationListenerReady());
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(NettyApplication.class);
    }
}