package com.gizwits.lease.model;

import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhl on 2017 2/8.
 */
public class AccessToken {
    private static final long serialVersionUID = 1L;

    private String access_token;// 令牌
    private long expires_in;// 有效时长 单位秒
    private long createTime;// 创建时间 单位毫秒

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
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

    public static AccessToken fromJson(String json) {
        AccessToken token = JSONObject.parseObject(json, AccessToken.class);
        if(token!=null)
            token.setCreateTime(System.currentTimeMillis());
        return token;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in=" + expires_in +
                ", createTime=" + createTime +
                '}';
    }
}
