package com.gizwits.lease.device.vo;

/**
 * @author lilh
 * @date 2017/7/27 16:00
 */
public class DeviceMapDetailDto {

    /** 投放点id */
    private Integer id;

    /** 空闲 */
    private Long freeCount = 0L;

    /** 在用 */
    private Long usingCount = 0L;

    /** 故障 */
    private Long faultCount = 0L;

    /** 离线 */
    private Long offlineCount = 0L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getFreeCount() {
        return freeCount;
    }

    public void setFreeCount(Long freeCount) {
        this.freeCount = freeCount;
    }

    public Long getUsingCount() {
        return usingCount;
    }

    public void setUsingCount(Long usingCount) {
        this.usingCount = usingCount;
    }

    public Long getFaultCount() {
        return faultCount;
    }

    public void setFaultCount(Long faultCount) {
        this.faultCount = faultCount;
    }

    public Long getOfflineCount() {
        return offlineCount;
    }

    public void setOfflineCount(Long offlineCount) {
        this.offlineCount = offlineCount;
    }

}
