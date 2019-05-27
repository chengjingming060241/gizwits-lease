package com.gizwits.lease.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Created by rongmc on 2017/7/20.
 */
@Configuration
@ConfigurationProperties(prefix = "cron")
public class CronConfig {
    private String orderAnalysis;
    private String deviceTrend;
    private String userTrend;
    private String deviceLocation;
    private String userLocation;
    private String deviceOrderWidget;
    private String userWidget;
    private String faultAlertType;

    private String everySec;
    private String everyTenMin;
    private String everyFiveMin;
    private String daily;

    private String everyMin;
    private String every30Sec;

    public String getEvery30Sec() {
        return every30Sec;
    }

    public void setEvery30Sec(String every30Sec) {
        this.every30Sec = every30Sec;
    }

    public String getEveryFiveMin() {
        return everyFiveMin;
    }

    public void setEveryFiveMin(String everyFiveMin) {
        this.everyFiveMin = everyFiveMin;
    }

    public String getEveryMin() {
        return everyMin;
    }

    public void setEveryMin(String everyMin) {
        this.everyMin = everyMin;
    }

    public String getOrderAnalysis() {
        return orderAnalysis;
    }

    public void setOrderAnalysis(String orderAnalysis) {
        this.orderAnalysis = orderAnalysis;
    }

    public String getFaultAlertType() {
        return faultAlertType;
    }

    public void setFaultAlertType(String faultAlertType) {
        this.faultAlertType = faultAlertType;
    }

    public String getDeviceTrend() {
        return deviceTrend;
    }

    public void setDeviceTrend(String deviceTrend) {
        this.deviceTrend = deviceTrend;
    }

    public String getUserTrend() {
        return userTrend;
    }

    public void setUserTrend(String userTrend) {
        this.userTrend = userTrend;
    }

    public String getDeviceLocation() {
        return deviceLocation;
    }

    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getDeviceOrderWidget() {
        return deviceOrderWidget;
    }

    public void setDeviceOrderWidget(String deviceOrderWidget) {
        this.deviceOrderWidget = deviceOrderWidget;
    }

    public String getUserWidget() {
        return userWidget;
    }

    public void setUserWidget(String userWidget) {
        this.userWidget = userWidget;
    }

    public String getEverySec() {
        return everySec;
    }

    public void setEverySec(String everySec) {
        this.everySec = everySec;
    }

    public String getEveryTenMin() {
        return everyTenMin;
    }

    public void setEveryTenMin(String everyTenMin) {
        this.everyTenMin = everyTenMin;
    }

    public String getDaily() {
        return daily;
    }

    public void setDaily(String daily) {
        this.daily = daily;
    }
}
