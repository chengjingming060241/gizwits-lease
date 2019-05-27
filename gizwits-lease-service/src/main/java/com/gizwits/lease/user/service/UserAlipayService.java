package com.gizwits.lease.user.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by yinhui on 2017/8/15.
 */
public interface UserAlipayService {


    void verifyAlipay(HttpServletResponse response, HttpServletRequest request);

    /**
     * 用户授权后获取用户信息并返回相应参数到cookie中
     * @param deviceId
     * @param authCode
     * @param response
     */
    void getAndSaveUserinfo(String deviceId,String authCode,HttpServletResponse response);

    boolean queryOrderPayStatus(String orderNo);
}
