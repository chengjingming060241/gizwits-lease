package com.gizwits.lease.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhl on 2017/2/10.
 */
public class PlatformAccessToken {
    private String token;// 令牌
    private long expire_at;// 有效时长 单位秒
    private long createTime;// 创建时间 单位毫秒

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire_at() {
        return expire_at;
    }

    public void setExpire_at(long expire_at) {
        this.expire_at = expire_at;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public static PlatformAccessToken fromJson(Object object) {
        PlatformAccessToken token = JSONObject.parseObject(object.toString(), PlatformAccessToken.class);
        token.setCreateTime(System.currentTimeMillis());
        return token;
    }

    @Override
    public String toString() {
        return "PlatformAccessToken{" +
                "token='" + token + '\'' +
                ", expire_at=" + expire_at +
                ", createTime=" + createTime +
                '}';
    }
}
