
package com.gizwits.lease.app.web;


import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.user.service.UserWeixinService;
import com.gizwits.lease.util.AlipayUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhl on 2017/8/9.
 */
@EnableSwagger2
@Api(value = "设备二维码扫描入口")
@Controller
@RequestMapping("/app/scan")
public class ScanController {

    @Autowired
    private UserWeixinService userWeixinService;

    @Autowired
    private DeviceService deviceService;

    @ApiOperation(value = "扫描二维码打开页面和点击链接的页面,需要将DeviceId传递过来")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void wxPage(HttpServletRequest request, HttpServletResponse response, RedirectAttributes model) throws IOException {
        String agent = request.getHeader("User-Agent").toLowerCase();
        String deviceId = request.getParameter("deviceId");

        if(agent.indexOf("micromessenger")>=0){//微信
            userWeixinService.skipToGetCode(request,response,deviceId);
        }else if(agent.indexOf("alipay")>=0){//支付宝
            AlipayUtil.skipToGetCode(response,deviceId, deviceService.getWxConfigByDeviceId(deviceId));

        }else{

            Cookie deviceIdCookie = new Cookie("deviceId", deviceId);
            deviceIdCookie.setPath("/");
            response.addCookie(deviceIdCookie);

            response.sendRedirect("/");
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

}

