package com.gizwits.lease.device.dao;

import com.gizwits.lease.device.entity.DeviceRunningRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.device.vo.DeviceRunningRecordForOfflineListDto;
import com.gizwits.lease.device.vo.DeviceRunningRecordForQueryDto;

import java.util.List;

/**
 * <p>
  * 设备运行记录表 Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-07-12
 */
public interface DeviceRunningRecordDao extends BaseMapper<DeviceRunningRecord> {

    /**
     * 离线设备列表
     * @param query
     * @return
     */
    List<DeviceRunningRecordForOfflineListDto> offlineList(DeviceRunningRecordForQueryDto query);

    /**
     * 离线设备记录数
     * @param query
     * @return
     */
    Integer findTotalSize(DeviceRunningRecordForQueryDto query);
}