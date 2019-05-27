package com.gizwits.lease.user.service;

import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.lease.device.vo.DeviceAuth;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhl on 2017/6/28.
 */
public interface UserWeixinService {
    /**
     * 公众号Token验证
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
     boolean verifySignature(String signature,String timestamp,String nonce);

    /**
     * 微信公众号回调
     * @param message
     * @return
     */
     String handleCallbackMsg(String message);



    /**
     * JSSDK 参数生成
     * @param req
     * @param openid
     * @param deviceId
     * @return
     */
    ResponseObject createJSSDKSignature(HttpServletRequest req, String openid, String deviceId);

    /**
     * 根据Code获取openid
     * @param code
     * @param state
     * @return
     */
    String getOpenid(String code,String state);

    /**
     * 获取Code
     * @param request
     * @param response
     * @param param
     */
    void skipToGetCode(HttpServletRequest request, HttpServletResponse response, String param);

    String authorizeDevice(List<DeviceAuth> deviceAuths, SysUserExt sysUserExt);

}
