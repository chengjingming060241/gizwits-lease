package com.gizwits.lease.product.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 产品表
 * </p>
 *
 * @author rongmc
 * @since 2017-06-19
 */
public class Product extends Model<Product> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 添加时间
     */
    private Date ctime;
    /**
     * 更新时间
     */
    private Date utime;
    /**
     * 产品名称
     */
    private String name;
    /**
     * 产品图片地址
     */
    @TableField("img_url")
    private String imgUrl;

    /**
     * 产品状态,1:启用, 0:禁用
     */
    private Integer status;
    /**
     * 产品类型
     */
    @TableField("category_id")
    private Integer categoryId;
    /**
     * 产品类型名称
     */
    @TableField("category_name")
    private String categoryName;
    /**
     * 所属厂商
     */
    @TableField("manufacturer_id")
    private Integer manufacturerId;
    /**
     * 所属品牌
     */
    @TableField("brand_id")
    private Integer brandId;

    /**
     * 通讯方式
     */
    @TableField("communicate_type")
    private String communicateType;

    /**
     * 操作人
     */
    @TableField("sys_user_id")
    private Integer sysUserId;

    /**
     * 删除标识
     */
    @TableField("is_deleted")
    private Integer isDeleted;


    /**
     * 机智云产品key
     */
    @TableField("gizwits_product_key")
    private String gizwitsProductKey;
    /**
     * 机智云产品secret
     */
    @TableField("gizwits_product_secret")
    private String gizwitsProductSecret;

    /**
     * 机智云企业id
     */
    @TableField("gizwits_enterprise_id")
    private String gizwitsEnterpriseId;

    /**
     * 机智云企业secret
     */
    @TableField("gizwits_enterprise_secret")
    private String gizwitsEnterpriseSecret;

    /**
     * 机智云AppId
     */
    @TableField("gizwits_appid")
    private String gizwitsAppId;

    /**
     * 机智云AppSecret
     */
    @TableField("gizwits_appsecret")
    private String gizwitsAppSecret;

    /**
     * 产品授权id
     */
    @TableField("auth_id")
    private String authId;

    /**
     * 产品授权密钥
     */
    @TableField("auth_secret")
    private String authSecret;

    /**
     * 用于消息分发，唯一即可
     */
    @TableField("subkey")
    private String subkey;

    /**
     * 消息类型，发送给机智云平台
     */
    @TableField("events")
    private String events;

    /**
     * 生成二维码的方式:WEB,网页链接;WEIXIN,微信硬件;
     */
    @TableField("qrcode_type")
    private String qrcodeType;

    /**
     * 获取设备坐标方式:GIZWITS,机智云接口;GD,高德接口(需要相关数据点支撑)
     */
    @TableField("location_type")
    private String locationType;
    /**
     * 微信硬件id
     */
    @TableField("wx_product_id")
    private String wxProductId;
    /**
     * 通信类型，0.移动；1.联通
     */
    @TableField("network_type")
    private String networkType;

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getGizwitsAppId() {
        return gizwitsAppId;
    }

    public void setGizwitsAppId(String gizwitsAppId) {
        this.gizwitsAppId = gizwitsAppId;
    }

    public String getGizwitsAppSecret() {
        return gizwitsAppSecret;
    }

    public void setGizwitsAppSecret(String gizwitsAppSecret) {
        this.gizwitsAppSecret = gizwitsAppSecret;
    }

    public String getQrcodeType() {
        return qrcodeType;
    }

    public void setQrcodeType(String qrcodeType) {
        this.qrcodeType = qrcodeType;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public Date getUtime() {
        return utime;
    }

    public void setUtime(Date utime) {
        this.utime = utime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getCommunicateType() {
        return communicateType;
    }

    public void setCommunicateType(String communicateType) {
        this.communicateType = communicateType;
    }

    public Integer getSysUserId() {
        return sysUserId;
    }

    public void setSysUserId(Integer sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public String getSubkey() {
        return subkey;
    }

    public void setSubkey(String subkey) {
        this.subkey = subkey;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public String getWxProductId() {
        return wxProductId;
    }

    public void setWxProductId(String wxProductId) {
        this.wxProductId = wxProductId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
