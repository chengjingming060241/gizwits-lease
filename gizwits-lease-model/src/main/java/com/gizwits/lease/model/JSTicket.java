/**
 * God bless all goes well!
 * <p/>
 * Author   JimLao
 * E-mail   developer.lao@gmail.com
 * Created  on Apr 10, 2015 3:04:16 PM
 */
package com.gizwits.lease.model;


import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;

public class JSTicket implements Serializable {
    private static final long serialVersionUID = 1L;

    private String errcode;
    private String errmsg;
    private String ticket;
    private long expires_in;
    private long createTime;// 创建时间 单位毫秒

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(long expires_in) {
        this.expires_in = expires_in;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public static JSTicket fromJson(String json) {
        JSTicket ticket = JSONObject.parseObject(json, JSTicket.class);
        ticket.setCreateTime(System.currentTimeMillis());
        return ticket;
    }

}
