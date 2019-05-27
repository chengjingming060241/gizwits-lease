/**
 * God bless all goes well!
 * <p>
 * <p>
 * Author   JimLao
 * E-mail   developer.lao@gmail.com
 * Created  on May 25, 2015 4:55:06 PM
 */
package com.gizwits.lease.model;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author jim
 *
 */
public class OauthCode {
    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "expires_in")
    private int expiresIn;

    @JSONField(name = "refresh_token")
    private String refreshToken;

    @JSONField(name = "openid")
    private String openid;
    @JSONField(name = "scope")
    private String scope;


    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public static OauthCode fromJson(String json) {
        OauthCode oauthCode = JSONObject.parseObject(json, OauthCode.class);
        return oauthCode;
    }
}
