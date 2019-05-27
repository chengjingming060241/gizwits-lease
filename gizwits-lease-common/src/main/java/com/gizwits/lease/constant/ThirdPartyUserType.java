package com.gizwits.lease.constant;

/**
 * Created by zhl on 2017/8/10.
 */
public enum ThirdPartyUserType {
    WEIXIN(1,"微信"),
    ALIPAY(2,"支付宝"),
    BAIDU(3,"百度"),
    SINA(4,"新浪"),
    NORMAL(5,"普通浏览器"),
    APP(6,"APP"),
    ;

    Integer code;
    String name;

    ThirdPartyUserType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
