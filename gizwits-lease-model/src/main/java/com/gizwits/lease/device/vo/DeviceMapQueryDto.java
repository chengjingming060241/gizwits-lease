package com.gizwits.lease.device.vo;

import com.gizwits.boot.annotation.Query;
import org.springframework.beans.BeanUtils;

/**
 * @author lilh
 * @date 2017/7/27 15:50
 */
public class DeviceMapQueryDto {

    private String mac;

    private String launchAreaName;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区/县 */
    private String area;

    private Integer workStatus;

    private Integer onlineStatus;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getLaunchAreaName() {
        return launchAreaName;
    }

    public void setLaunchAreaName(String launchAreaName) {
        this.launchAreaName = launchAreaName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public static class LaunchAreaPart {
        @Query(field = "name", operator = Query.Operator.like)
        private String launchAreaName;

        /** 省 */
        @Query(field = "province")
        private String province;

        /** 市 */
        @Query(field = "city")
        private String city;

        /** 区/县 */
        @Query(field = "area")
        private String area;

        public LaunchAreaPart(DeviceMapQueryDto dto) {
            BeanUtils.copyProperties(dto, this);
        }

        public String getLaunchAreaName() {
            return launchAreaName;
        }

        public void setLaunchAreaName(String launchAreaName) {
            this.launchAreaName = launchAreaName;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }

    public static class DevicePart {
        @Query(field = "mac", operator = Query.Operator.like)
        private String mac;

        @Query(field = "work_status")
        private Integer workStatus;

        @Query(field = "online_status")
        private Integer onlineStatus;

        public DevicePart(DeviceMapQueryDto dto) {
            BeanUtils.copyProperties(dto, this);
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public Integer getWorkStatus() {
            return workStatus;
        }

        public void setWorkStatus(Integer workStatus) {
            this.workStatus = workStatus;
        }

        public Integer getOnlineStatus() {
            return onlineStatus;
        }

        public void setOnlineStatus(Integer onlineStatus) {
            this.onlineStatus = onlineStatus;
        }
    }
}
