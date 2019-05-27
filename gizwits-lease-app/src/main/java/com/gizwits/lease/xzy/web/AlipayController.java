package com.gizwits.lease.xzy.web;

import com.gizwits.boot.base.BaseController;
import com.gizwits.lease.user.service.UserAlipayService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.annotations.Api;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by yinhui on 2017/8/14.
 */
@RestController
@EnableSwagger2
@Api(description = "支付宝接口（获取用户信息）")
@RequestMapping(value = "/xiangzhiyun/alipay")
public class AlipayController extends BaseController {

    @Autowired
    private UserAlipayService userAlipayService;

    @RequestMapping(value = "userinfo", method = RequestMethod.GET, produces = "text/xml;charset=UTF-8")
    public void getAlipayCode(@RequestParam("app_id") String appId,
                              @RequestParam("scope") String scope,
                              @RequestParam("state") String state,
                              @RequestParam("auth_code") String authCode,HttpServletResponse response) throws Exception{
        userAlipayService.getAndSaveUserinfo(state,authCode,response);
    }

    @RequestMapping(value = "verify", method = RequestMethod.POST, produces = "text/xml;charset=UTF-8")
    public void verify(HttpServletRequest request, HttpServletResponse response) throws Exception{
        userAlipayService.verifyAlipay(response, request);
    }



}
