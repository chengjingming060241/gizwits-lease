package com.gizwits.lease.device.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.constant.DeviceStatus;
import com.gizwits.lease.device.dao.DeviceRunningRecordDao;
import com.gizwits.lease.device.entity.DeviceRunningRecord;
import com.gizwits.lease.device.service.DeviceRunningRecordService;
import com.gizwits.lease.device.vo.DeviceRunningRecordForListDto;
import com.gizwits.lease.device.vo.DeviceRunningRecordForOfflineListDto;
import com.gizwits.lease.device.vo.DeviceRunningRecordForQueryDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 设备运行记录表 服务实现类
 * </p>
 *
 * @author zhl
 * @since 2017-07-12
 */
@Service
public class DeviceRunningRecordServiceImpl extends ServiceImpl<DeviceRunningRecordDao, DeviceRunningRecord> implements DeviceRunningRecordService {

    @Autowired
    private DeviceRunningRecordDao deviceRunningRecordDao;

    @Override
    public Page<DeviceRunningRecordForListDto> list(Pageable<DeviceRunningRecordForQueryDto> pageable) {
        Page<DeviceRunningRecord> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<DeviceRunningRecord> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<DeviceRunningRecordForListDto> result = new Page<>();
        result.setRecords(new ArrayList<>(selectPage.getRecords().size()));
        selectPage.getRecords().forEach(item -> {
            DeviceRunningRecordForListDto dto = new DeviceRunningRecordForListDto(item);
            dto.setWorkStatusDesc(DeviceStatus.getName(dto.getWorkStatus()));
            result.getRecords().add(dto);
        });
        return result;
    }

    @Override
    public Page<DeviceRunningRecordForOfflineListDto> offlineList(Pageable<DeviceRunningRecordForQueryDto> pageable) {
        DeviceRunningRecordForQueryDto query = pageable.getQuery();
        if (pageable.getCurrent() < 1) {
            pageable.setCurrent(1);
        }
        query.setBegin((pageable.getCurrent() - 1) * pageable.getSize());
        query.setEnd(pageable.getCurrent() * pageable.getSize());
        query.setPagesize(pageable.getSize());
        List<DeviceRunningRecordForOfflineListDto> list = deviceRunningRecordDao.offlineList(query);
        Integer total = deviceRunningRecordDao.findTotalSize(query);

        // 更新已读
        DeviceRunningRecord record = new DeviceRunningRecord();
        record.setIsRead(1);
        Wrapper<DeviceRunningRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("is_read", 0);
        deviceRunningRecordDao.update(record, wrapper);

        Page<DeviceRunningRecordForOfflineListDto> page = new Page<>();
        page.setSize(pageable.getSize());
        page.setCurrent(pageable.getCurrent());
        page.setRecords(list);
        page.setTotal(total);
        return page;
    }

    @Override
    public Integer getUnreadOffline() {
        return deviceRunningRecordDao.findTotalSize(new DeviceRunningRecordForQueryDto());
    }

}
