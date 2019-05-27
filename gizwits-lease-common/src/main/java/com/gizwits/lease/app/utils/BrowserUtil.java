package com.gizwits.lease.app.utils;

import com.gizwits.lease.constant.ThirdPartyUserType;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhl on 2017/8/19.
 */
public class BrowserUtil {
    public static Integer getUserBrowserType(HttpServletRequest request){
        String agent = request.getHeader("User-Agent").toLowerCase();
        Integer browserAgentType = 0;
        if(agent.indexOf("micromessenger")>=0){//微信
            browserAgentType = ThirdPartyUserType.WEIXIN.getCode();
        }else if(agent.indexOf("alipay")>=0){//支付宝
            browserAgentType = ThirdPartyUserType.ALIPAY.getCode();
        }else{
            browserAgentType = ThirdPartyUserType.NORMAL.getCode();
        }
        return browserAgentType;
    }
}
