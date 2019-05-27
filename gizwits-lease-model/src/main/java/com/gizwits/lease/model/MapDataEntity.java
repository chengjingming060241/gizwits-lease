package com.gizwits.lease.model;

import com.alibaba.fastjson.JSON;

/**
 * Created by kufufu on 2017/5/18.
 */
public class MapDataEntity {

    private String imei;
    /**
     * 手机mac地址
     */
//    private String smac;
    private String imsi;
    /**
     * 移动国家码
     */
    private String mcc;
    /**
     * 移动网络码
     */
    private String mnc;
    /**
     * 基站Lac区域Id
     */
    private String lac1;
    /**
     * 基站Cell基站Id
     */
    private String cellid1;
    /**
     * 基站Rssi信号强度
     */
    private String rssi1;
    private String lac2;
    private String cellid2;
    private String rssi2;
    private String lac3;
    private String cellid3;
    private String rssi3;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

//    public String getSmac() {
//        return smac;
//    }
//
//    public void setSmac(String smac) {
//        this.smac = smac;
//    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMcc() {
        return mcc;
    }

    public void setMcc(String mcc) {
        this.mcc = mcc;
    }

    public String getMnc() {
        return mnc;
    }

    public void setMnc(String mnc) {
        this.mnc = mnc;
    }

    public String getLac1() {
        return lac1;
    }

    public void setLac1(String lac1) {
        this.lac1 = lac1;
    }

    public String getCellid1() {
        return cellid1;
    }

    public void setCellid1(String cellid1) {
        this.cellid1 = cellid1;
    }

    public String getRssi1() {
        return rssi1;
    }

    public void setRssi1(String rssi1) {
        this.rssi1 = rssi1;
    }

    public String getLac2() {
        return lac2;
    }

    public void setLac2(String lac2) {
        this.lac2 = lac2;
    }

    public String getCellid2() {
        return cellid2;
    }

    public void setCellid2(String cellid2) {
        this.cellid2 = cellid2;
    }

    public String getRssi2() {
        return rssi2;
    }

    public void setRssi2(String rssi2) {
        this.rssi2 = rssi2;
    }

    public String getLac3() {
        return lac3;
    }

    public void setLac3(String lac3) {
        this.lac3 = lac3;
    }

    public String getCellid3() {
        return cellid3;
    }

    public void setCellid3(String cellid3) {
        this.cellid3 = cellid3;
    }

    public String getRssi3() {
        return rssi3;
    }

    public void setRssi3(String rssi3) {
        this.rssi3 = rssi3;
    }

    public static MapDataEntity fromJson(String json) {
        return JSON.parseObject(json, MapDataEntity.class);
    }

    public static MapDataEntity transformData(MapDataEntity newMapDataEntity) {
        newMapDataEntity.setMcc("460");
        newMapDataEntity.setMnc("0");

        newMapDataEntity.setLac1(String.valueOf(Integer.parseInt(newMapDataEntity.getLac1(), 16)));
        newMapDataEntity.setLac2(String.valueOf(Integer.parseInt(newMapDataEntity.getLac2(), 16)));
        newMapDataEntity.setLac3(String.valueOf(Integer.parseInt(newMapDataEntity.getLac3(), 16)));

        newMapDataEntity.setCellid1(String.valueOf(Integer.parseInt(newMapDataEntity.getCellid1(), 16)));
        newMapDataEntity.setCellid2(String.valueOf(Integer.parseInt(newMapDataEntity.getCellid2(), 16)));
        newMapDataEntity.setCellid3(String.valueOf(Integer.parseInt(newMapDataEntity.getCellid3(), 16)));


        newMapDataEntity.setRssi1(MapDataEntity.getRssi(newMapDataEntity.getRssi1()));
        newMapDataEntity.setRssi2(MapDataEntity.getRssi(newMapDataEntity.getRssi2()));
        newMapDataEntity.setRssi3(MapDataEntity.getRssi(newMapDataEntity.getRssi3()));

        return newMapDataEntity;
    }

    private static String getRssi(String rssi) {
        int result = Integer.parseInt(rssi, 16) - 100;
        if (result > 0) {
            result = result * 2 - 113;
        }
        return String.valueOf(result);
    }
}
