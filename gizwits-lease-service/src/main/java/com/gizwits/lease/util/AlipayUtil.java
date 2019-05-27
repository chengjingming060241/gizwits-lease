package com.gizwits.lease.util;

import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.SystemConfigKey;

import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Created by yinhui on 2017/8/14.
 */
@Component
public class AlipayUtil {

    /**用户授权url*/
    private static String USER_AUTHORIZATION_URL = "https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=APPID&scope=SCOPE&redirect_uri=ENCODED_URL&state=STATE";

    /**
     * 获取用户授权url
     * @return
     */
    public static String getUserAuthorizationUrl(String deviceId, SysUserExt sysUserExt){
        if(Objects.isNull(sysUserExt)){
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_CONFIG_ERROR);
        }
        if(ParamUtil.isNullOrEmptyOrZero(sysUserExt.getAlipayAppid())
                ||ParamUtil.isNullOrEmptyOrZero(sysUserExt.getAlipayPublicKey())){
            LeaseException.throwSystemException(LeaseExceEnums.DEVICE_NOT_SUPPORT_ALIPAY);
        }
        return USER_AUTHORIZATION_URL.replace("APPID", sysUserExt.getAlipayAppid()).replace("SCOPE", SysConfigUtils.get(CommonSystemConfig.class).getAlipayUserScope())
                .replace("ENCODED_URL",SysConfigUtils.get(CommonSystemConfig.class).getAlipayCodeUrl()).replace("STATE",deviceId);
    }

    public static void skipToGetCode(HttpServletResponse response, String deviceId, SysUserExt sysUserExt){
        try {
            response.sendRedirect(getUserAuthorizationUrl(deviceId,sysUserExt));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
