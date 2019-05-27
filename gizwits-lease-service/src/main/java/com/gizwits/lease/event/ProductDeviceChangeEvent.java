package com.gizwits.lease.event;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.utils.WebUtils;
import com.gizwits.lease.enums.ProductOperateType;
import org.springframework.context.ApplicationEvent;

/**
 * Event - 产品修改事件
 *
 * @author lilh
 * @date 2017/7/20 17:00
 */
public class ProductDeviceChangeEvent extends ApplicationEvent {
    private static final long serialVersionUID = 2820627631043510765L;

    private Integer productId;

    private String deviceSno;

    private ProductOperateType operateType;

    private SysUser operator;

    private String ip;

    private ProductDeviceChangeEvent(Object source) {
        super(source);
    }

    private ProductDeviceChangeEvent(Integer productId, String deviceSno, ProductOperateType operateType, SysUser operator, String ip) {
        this(new Object());
        this.productId = productId;
        this.deviceSno = deviceSno;
        this.operateType = operateType;
        this.operator = operator;
        this.ip = ip;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getDeviceSno() {
        return deviceSno;
    }

    public void setDeviceSno(String deviceSno) {
        this.deviceSno = deviceSno;
    }

    public ProductOperateType getOperateType() {
        return operateType;
    }

    public void setOperateType(ProductOperateType operateType) {
        this.operateType = operateType;
    }

    public SysUser getOperator() {
        return operator;
    }

    public void setOperator(SysUser operator) {
        this.operator = operator;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public static class Builder {
        private Integer productId;

        private String deviceSno;

        private ProductOperateType operateType;

        private SysUser operator;

        private Builder() {
        }

        public static Builder create() {
            return new Builder();
        }

        public Builder productId(Integer productId) {
            this.productId = productId;
            return this;
        }

        public Builder deviceSno(String deviceSno) {
            this.deviceSno = deviceSno;
            return this;
        }

        public Builder productOperateType(ProductOperateType productOperateType) {
            this.operateType = productOperateType;
            return this;
        }

        public Builder operator(SysUser operator) {
            this.operator = operator;
            return this;
        }

        public ProductDeviceChangeEvent build() {
            return new ProductDeviceChangeEvent(this.productId, this.deviceSno, this.operateType, this.operator, WebUtils.getRemoteAddr());
        }
    }
}
