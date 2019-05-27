package com.gizwits.lease;

import com.gizwits.lease.common.LeaseWebProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Config - web配置
 *
 * @author lilh
 * @date 2017/7/24 11:27
 */
@Configuration
@EnableConfigurationProperties(LeaseWebProperties.class)
public class WebConfig {

    @Autowired
    private LeaseWebProperties leaseWebProperties;

    @Bean
    public WebMvcConfigurerAdapter customConfigurerAdapter() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
                configurer.enable();
            }

        };
    }


    @Bean
    public ErrorPageRegistrar customErrorRegistrar() {
        return registry -> {
            //ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, leaseWebProperties.getNotFoundPath());
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, leaseWebProperties.getNotFoundPath());
            //ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, leaseWebProperties.getInternalServerErrorPath());
            ErrorPage globalError = new ErrorPage(leaseWebProperties.getErrorPath());
            registry.addErrorPages(error404Page, globalError);
        };
    }

/*    @Bean
    @ConditionalOnMissingBean(CommonsMultipartResolver.class)
    @ConditionalOnClass(FileUploadBase.class)
    @ConfigurationProperties(prefix = "gizwits.lease.multipart")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("UTF-8");
        multipartResolver.setMaxUploadSize(50 * 1024 * 1024);//50M
        return multipartResolver;
    }*/

}
