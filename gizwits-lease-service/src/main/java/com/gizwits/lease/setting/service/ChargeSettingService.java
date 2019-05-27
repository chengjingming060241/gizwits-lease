package com.gizwits.lease.setting.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.setting.dto.ChargeSettingAddDto;
import com.gizwits.lease.setting.dto.ChargeSettingListDto;
import com.gizwits.lease.setting.dto.ChargeSettingQueryDto;
import com.gizwits.lease.setting.entity.ChargeSetting;

/**
 * <p>
 * 充值设定 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-09-01
 */
public interface ChargeSettingService extends IService<ChargeSetting> {

    /**
     * 添加
     */
    boolean add(ChargeSettingAddDto dto);

    /**
     * 列表
     */
    List<ChargeSettingListDto> list(ChargeSettingQueryDto query);
}
