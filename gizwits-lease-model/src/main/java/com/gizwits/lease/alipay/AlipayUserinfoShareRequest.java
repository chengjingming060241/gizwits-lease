package com.gizwits.lease.alipay;

import com.alipay.api.AlipayObject;
import com.alipay.api.AlipayRequest;
import com.alipay.api.internal.util.AlipayHashMap;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;

import java.util.Map;

/**
 * Created by zhl on 2017/8/21.
 */
public class AlipayUserinfoShareRequest implements AlipayRequest<AlipayUserUserinfoShareResponse> {
    private AlipayHashMap udfParams;
    private String apiVersion = "1.0";
    private String terminalType;
    private String terminalInfo;
    private String prodCode;
    private String notifyUrl;
    private String returnUrl;
    private boolean needEncrypt = false;
    private AlipayObject bizModel = null;

    public AlipayUserinfoShareRequest() {
    }

    public String getNotifyUrl() {
        return this.notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getReturnUrl() {
        return this.returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalType() {
        return this.terminalType;
    }

    public void setTerminalInfo(String terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    public String getTerminalInfo() {
        return this.terminalInfo;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getProdCode() {
        return this.prodCode;
    }

    public String getApiMethodName() {
        return "alipay.user.info.share";
    }

    public Map<String, String> getTextParams() {
        AlipayHashMap txtParams = new AlipayHashMap();
        if(this.udfParams != null) {
            txtParams.putAll(this.udfParams);
        }

        return txtParams;
    }

    public void putOtherTextParam(String key, String value) {
        if(this.udfParams == null) {
            this.udfParams = new AlipayHashMap();
        }

        this.udfParams.put(key, value);
    }

    public Class<AlipayUserUserinfoShareResponse> getResponseClass() {
        return AlipayUserUserinfoShareResponse.class;
    }

    public boolean isNeedEncrypt() {
        return this.needEncrypt;
    }

    public void setNeedEncrypt(boolean needEncrypt) {
        this.needEncrypt = needEncrypt;
    }

    public AlipayObject getBizModel() {
        return this.bizModel;
    }

    public void setBizModel(AlipayObject bizModel) {
        this.bizModel = bizModel;
    }
}

