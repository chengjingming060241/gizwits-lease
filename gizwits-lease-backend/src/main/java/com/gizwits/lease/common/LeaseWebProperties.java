package com.gizwits.lease.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Config - 属性配置
 *
 * @author lilh
 * @date 2017/7/25 10:47
 */
@ConfigurationProperties(prefix = "gizwits.lease.web")
public class LeaseWebProperties {

    private static final String DEFAULT_NOT_FOUND_PATH = "/index.html";

    private static final String DEFAULT_INTERNAL_SERVER_ERROR_PATH = "/index.html";

    private static final String DEFAULT_UNAUTHORIZED_PATH = "/unauthorized.html";

    private String notFoundPath = DEFAULT_NOT_FOUND_PATH;

    private String internalServerErrorPath = DEFAULT_INTERNAL_SERVER_ERROR_PATH;

    private String errorPath = DEFAULT_NOT_FOUND_PATH;


    public String getNotFoundPath() {
        return notFoundPath;
    }

    public void setNotFoundPath(String notFoundPath) {
        this.notFoundPath = notFoundPath;
    }

    public String getInternalServerErrorPath() {
        return internalServerErrorPath;
    }

    public void setInternalServerErrorPath(String internalServerErrorPath) {
        this.internalServerErrorPath = internalServerErrorPath;
    }

    public String getErrorPath() {
        return errorPath;
    }

    public void setErrorPath(String errorPath) {
        this.errorPath = errorPath;
    }
}