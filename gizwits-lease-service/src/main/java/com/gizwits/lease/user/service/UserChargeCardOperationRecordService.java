package com.gizwits.lease.user.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.user.dto.UserChargeCardOperationListDto;
import com.gizwits.lease.user.dto.UserChargeCardOperationRecordQueryDto;
import com.gizwits.lease.user.entity.UserChargeCardOperationRecord;

/**
 * <p>
 * 充值卡操作记录(启用/禁用) 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
public interface UserChargeCardOperationRecordService extends IService<UserChargeCardOperationRecord> {

    /**
     * 列表
     */
    Page<UserChargeCardOperationListDto> list(Pageable<UserChargeCardOperationRecordQueryDto> pageable);

}
