package com.gizwits.lease.product.dto;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Dto - 机智云数据点接口请求参数
 *
 * @author lilh
 * @date 2017/7/4 10:35
 */
@JSONType(ignores = {"gizwitsProductKey", "productId"})
public class GizwitsDataPointReqDto implements Serializable {
    private static final long serialVersionUID = 4531666932706002507L;


    /**
     * 产品id
     */
    @JsonProperty("productId")
    private Integer productId;

    /**
     * 产品key
     */
    @NotBlank
    @JsonProperty("gizwitsProductKey")
    private String gizwitsProductKey;

    /**
     * 产品secret
     */
    @NotBlank
    @JsonProperty("gizwitsProductSecret")
    @JSONField(name = "product_secret")
    private String gizwitsProductSecret;

    /**
     * 企业id
     */
    @NotBlank
    @JsonProperty("gizwitsEnterpriseId")
    @JSONField(name = "enterprise_id")
    private String gizwitsEnterpriseId;

    /**
     * 企业secret
     */
    @NotBlank
    @JsonProperty("gizwitsEnterpriseSecret")
    @JSONField(name = "enterprise_secret")
    private String gizwitsEnterpriseSecret;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getGizwitsProductKey() {
        return gizwitsProductKey;
    }

    public void setGizwitsProductKey(String gizwitsProductKey) {
        this.gizwitsProductKey = gizwitsProductKey;
    }

    public String getGizwitsProductSecret() {
        return gizwitsProductSecret;
    }

    public void setGizwitsProductSecret(String gizwitsProductSecret) {
        this.gizwitsProductSecret = gizwitsProductSecret;
    }

    public String getGizwitsEnterpriseId() {
        return gizwitsEnterpriseId;
    }

    public void setGizwitsEnterpriseId(String gizwitsEnterpriseId) {
        this.gizwitsEnterpriseId = gizwitsEnterpriseId;
    }

    public String getGizwitsEnterpriseSecret() {
        return gizwitsEnterpriseSecret;
    }

    public void setGizwitsEnterpriseSecret(String gizwitsEnterpriseSecret) {
        this.gizwitsEnterpriseSecret = gizwitsEnterpriseSecret;
    }
}
