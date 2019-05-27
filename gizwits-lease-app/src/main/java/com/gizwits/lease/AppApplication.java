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
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;

/**
 * Created by zhl on 2017/6/28.
 */
@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan("com.gizwits")
@MapperScan(basePackages = {"com.gizwits.lease.*.dao"})
public class AppApplication extends SpringBootServletInitializer {

    protected final static Logger logger = LoggerFactory.getLogger(AppApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(AppApplication.class);
        app.addListeners(new ApplicationListenerReady());
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AppApplication.class);
    }

    /**
     * 文件上传配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize("2024KB"); //KB,MB
        /// 设置总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        return factory.createMultipartConfig();
    }
}
