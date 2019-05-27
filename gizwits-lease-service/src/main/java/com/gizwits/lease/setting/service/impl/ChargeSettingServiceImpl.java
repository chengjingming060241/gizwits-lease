package com.gizwits.lease.setting.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.setting.dao.ChargeSettingDao;
import com.gizwits.lease.setting.dto.ChargeSettingAddDto;
import com.gizwits.lease.setting.dto.ChargeSettingListDto;
import com.gizwits.lease.setting.dto.ChargeSettingQueryDto;
import com.gizwits.lease.setting.entity.ChargeSetting;
import com.gizwits.lease.setting.service.ChargeSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 充值设定 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-09-01
 */
@Service
public class ChargeSettingServiceImpl extends ServiceImpl<ChargeSettingDao, ChargeSetting> implements ChargeSettingService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public boolean add(ChargeSettingAddDto dto) {
        int count = selectCount(new EntityWrapper<ChargeSetting>().eq("money", dto.getMoney()));
        if (count > 0) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_SETTING_EXIST);
        }
        count = selectCount(new EntityWrapper<>());
        if (count >= SysConfigUtils.get(CommonSystemConfig.class).getDefaultChargeSettingCount()) {
            LeaseException.throwSystemException(LeaseExceEnums.CHARGE_SETTING_OVERFLOW);
        }
        SysUser current = sysUserService.getCurrentUser();
        ChargeSetting chargeSetting = new ChargeSetting();
        chargeSetting.setCtime(new Date());
        chargeSetting.setMoney(dto.getMoney());
        chargeSetting.setSysUserId(current.getId());
        chargeSetting.setSysUserName(current.getUsername());
        return insert(chargeSetting);
    }

    @Override
    public List<ChargeSettingListDto> list(ChargeSettingQueryDto query) {
        List<ChargeSetting> chargeSettings = selectList(QueryResolverUtils.parse(query, new EntityWrapper<ChargeSetting>().orderBy("money", true)));
        return chargeSettings.stream().map(ChargeSettingListDto::new).collect(Collectors.toList());
    }
}
