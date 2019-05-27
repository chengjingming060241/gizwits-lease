package com.gizwits.lease.user.service.impl;

import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.user.dao.UserChargeCardOperationRecordDao;
import com.gizwits.lease.user.dto.UserChargeCardOperationListDto;
import com.gizwits.lease.user.dto.UserChargeCardOperationRecordQueryDto;
import com.gizwits.lease.user.entity.UserChargeCardOperationRecord;
import com.gizwits.lease.user.service.UserChargeCardOperationRecordService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 充值卡操作记录(启用/禁用) 服务实现类
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
@Service
public class UserChargeCardOperationRecordServiceImpl extends ServiceImpl<UserChargeCardOperationRecordDao, UserChargeCardOperationRecord> implements UserChargeCardOperationRecordService {

    @Override
    public Page<UserChargeCardOperationListDto> list(Pageable<UserChargeCardOperationRecordQueryDto> pageable) {
        Page<UserChargeCardOperationRecord> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        Page<UserChargeCardOperationRecord> selectPage = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), new EntityWrapper<>()));
        Page<UserChargeCardOperationListDto> result = new Page<>();
        BeanUtils.copyProperties(selectPage, result);
        if (CollectionUtils.isNotEmpty(selectPage.getRecords())) {
            result.setRecords(selectPage.getRecords().stream().map(UserChargeCardOperationListDto::new).collect(Collectors.toList()));
        }
        return result;
    }
}
