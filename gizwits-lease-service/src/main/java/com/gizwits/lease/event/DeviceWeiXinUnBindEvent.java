package com.gizwits.lease.event;

import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.lease.device.entity.UserDevice;
import org.springframework.context.ApplicationEvent;

/**
 * Created by GaGi on 2017/7/28.
 */
public class DeviceWeiXinUnBindEvent extends ApplicationEvent {
    private String openId;
    private String wxDid;
    private SysUserExt sysUserExt;
    private UserDevice userDevice;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public DeviceWeiXinUnBindEvent(Object source) {
        super(source);
    }

    public static DeviceWeiXinUnBindEvent build(String openId,String wxDid,SysUserExt sysUserExt,UserDevice userDevice){
        DeviceWeiXinUnBindEvent event = new DeviceWeiXinUnBindEvent(new Object());
        event.setWxDid(wxDid);
        event.setOpenId(openId);
        event.setSysUserExt(sysUserExt);
        event.setUserDevice(userDevice);
        return event;
    }

    public UserDevice getUserDevice() {
        return userDevice;
    }

    public void setUserDevice(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxDid() {
        return wxDid;
    }

    public void setWxDid(String wxDid) {
        this.wxDid = wxDid;
    }

    public SysUserExt getSysUserExt() {
        return sysUserExt;
    }

    public void setSysUserExt(SysUserExt sysUserExt) {
        this.sysUserExt = sysUserExt;
    }
}
