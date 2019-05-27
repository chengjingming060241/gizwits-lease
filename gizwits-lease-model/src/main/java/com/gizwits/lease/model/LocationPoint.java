package com.gizwits.lease.model;

import java.math.BigDecimal;

/**
 * Created by zhl on 2017/9/6.
 */
public class LocationPoint {
    private BigDecimal longitude;
    private BigDecimal latitude;

    public LocationPoint(BigDecimal longitude, BigDecimal latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public LocationPoint() {
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }
}
