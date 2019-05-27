package com.gizwits.lease.device.entity.dto;

import java.util.List;

import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.enums.DeleteStatus;

/**
 * @author lilh
 * @date 2017/8/15 17:59
 */
public class DeviceGroupForAccessDto {

    @Query(field = "id")
    private Integer id;

    @Query(field = "is_deleted")
    private Integer isDeleted = DeleteStatus.NOT_DELETED.getCode();

    @Query(field = "sys_user_id", operator = Query.Operator.in)
    private List<Integer> accessableUserIds;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
