package com.gizwits.lease.app.utils;

import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.SystemConfigKey;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by zhl on 2017/4/25.
 */
public class ContextUtil {

    public static String getHostWithContextPath(){
        HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (httpRequest==null)return null;
        return getHostWithContextPath(httpRequest);
    }

    public static String getHost(HttpServletRequest httpRequest){
        StringBuffer sb = new StringBuffer();
        if(httpRequest.getServerPort()!=80){
            sb.append(httpRequest.getScheme()).append("://")
                    .append(httpRequest.getServerName()).append("/");
        }else{
            sb.append(httpRequest.getScheme()).append("://")
                    .append(httpRequest.getServerName()).append("/");
        }
        return sb.toString();
    }


    public static String getHostWithContextPath(HttpServletRequest httpRequest){
        StringBuffer sb = new StringBuffer();
        if(httpRequest.getServerPort()!=80){
            sb.append(httpRequest.getScheme()).append("://")
                    .append(httpRequest.getServerName()).append(":").append(httpRequest.getServerPort())
                    .append(httpRequest.getContextPath());
        }else{
            if(org.apache.commons.lang.StringUtils.isBlank(SysConfigUtils.get(CommonSystemConfig.class).getNginxSuffixAndContextPath())){
                sb.append(httpRequest.getScheme()).append("://")
                        .append(httpRequest.getServerName())
                        .append(httpRequest.getContextPath());
            }else{
                sb.append(httpRequest.getScheme()).append("://")
                        .append(httpRequest.getServerName()).append("/").append(SysConfigUtils.get(CommonSystemConfig.class).getNginxSuffixAndContextPath())
                        .append(httpRequest.getContextPath());
            }
        }
        return sb.toString();
    }

    public static String getHostWithContextPathAndUri(HttpServletRequest request){
        if(ParamUtil.isNullOrEmptyOrZero(SysConfigUtils.get(CommonSystemConfig.class).getWxCodeUrlPath())){
            return getHostWithContextPath(request)+request.getRequestURI();
        }else{
            //return getHostWithContextPath(request)+SysConfigUtils.get(CommonSystemConfig.class).getWxCodeUrlPath();
            return SysConfigUtils.get(CommonSystemConfig.class).getHostWithContext()+SysConfigUtils.get(CommonSystemConfig.class).getWxCodeUrlPath();
        }
    }

}