package com.gizwits.lease.device.dao;

import com.gizwits.lease.device.entity.DeviceServiceModeSetting;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
  * 设备收费模式设定(麻将机系统特有需求) Mapper 接口
 * </p>
 *
 * @author zhl
 * @since 2017-09-04
 */
public interface DeviceServiceModeSettingDao extends BaseMapper<DeviceServiceModeSetting> {

    int deleteDeviceServiceModeSettingByAssignAccountIdAndSnoList(@Param("assignAccountId")Integer assignAccountId, @Param("snoList")List<String> snoList);

    int deleteDeviceServiceModeSettingByAssignAccountIdAndSno(@Param("assignAccountId")Integer assignAccountId, @Param("sno")String sno);
}