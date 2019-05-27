package com.gizwits.lease.user.dto;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Dto - 移入/出黑名单相关
 *
 * @author lilh
 * @date 2017/8/3 11:30
 */
public class UserForMoveDto {

    @NotEmpty
    private List<String> userIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}

