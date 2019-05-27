package com.gizwits.lease.constant;

/**
 * @author lilh
 * @date 2017/8/31 15:29
 */
public class DeviceExcelTemplate {

    /** 设备mac */
    private String mac;

    /** 二维码(url地址) */
    private String qrcode;

    public DeviceExcelTemplate() {
    }

    public DeviceExcelTemplate(String mac, String qrcode) {
        this.mac = mac;
        this.qrcode = qrcode;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }
}
