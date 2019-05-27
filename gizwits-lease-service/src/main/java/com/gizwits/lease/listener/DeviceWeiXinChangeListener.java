package com.gizwits.lease.listener;


import com.gizwits.lease.constant.BindType;
import com.gizwits.lease.device.entity.UserDevice;
import com.gizwits.lease.device.service.UserDeviceService;
import com.gizwits.lease.event.DeviceWeiXinUnBindEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.util.WxUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by GaGi on 2017/7/28.
 */
@Component
public class DeviceWeiXinChangeListener implements ApplicationListener<DeviceWeiXinUnBindEvent> {
    protected static Logger logger = LoggerFactory.getLogger("WEIXIN_LOGGER");
    @Autowired
    private UserDeviceService userDeviceService;

    @Override
    public void onApplicationEvent(DeviceWeiXinUnBindEvent event) {
        if (StringUtils.isEmpty(event.getOpenId())||StringUtils.isEmpty(event.getWxDid())|| Objects.isNull(event.getSysUserExt())){
            LeaseException.throwSystemException(LeaseExceEnums.WX_UNBIND_FAIL);
        }
        String res = WxUtil.unBindDev(event.getOpenId(), event.getWxDid(), event.getSysUserExt());
        //绑定三次
        for (int i = 0; i < 3; ++i) {
            logger.info("微信解绑结果：" + res);
            //如果解绑成功
            if (res.indexOf("ok") != -1) {
                UserDevice userDevice = event.getUserDevice();
                if (BindType.BIND.getCode().equals(userDevice.getIsBind())) {
                    userDevice.setIsBind(0);
                    userDeviceService.updateById(userDevice);
                }
                break;
            } else {
                //如果解绑失败，再次解绑
                res = WxUtil.unBindDev(event.getOpenId(), event.getWxDid(), event.getSysUserExt());
            }
        }
    }
}
