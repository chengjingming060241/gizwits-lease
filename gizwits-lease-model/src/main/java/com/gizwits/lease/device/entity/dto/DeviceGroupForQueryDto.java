package com.gizwits.lease.device.entity.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.enums.DeleteStatus;

/**
 * Dto - 设备组查询
 *
 * @author lilh
 * @date 2017/8/15 15:01
 */
public class DeviceGroupForQueryDto {


    @JsonIgnore
    @Query(field = "is_deleted")
    private Integer isDeleted = DeleteStatus.NOT_DELETED.getCode();

    @JsonIgnore
    @Query(field = "sys_user_id", operator = Query.Operator.in)
    private List<Integer> accessableUserIds;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public List<Integer> getAccessableUserIds() {
        return accessableUserIds;
    }

    public void setAccessableUserIds(List<Integer> accessableUserIds) {
        this.accessableUserIds = accessableUserIds;
    }
}
