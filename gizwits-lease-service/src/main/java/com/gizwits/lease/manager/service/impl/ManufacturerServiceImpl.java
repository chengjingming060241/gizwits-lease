package com.gizwits.lease.manager.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.manager.entity.Manufacturer;
import com.gizwits.lease.manager.dao.ManufacturerDao;
import com.gizwits.lease.manager.service.ManufacturerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 厂商(或企业)表 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
@Service
public class ManufacturerServiceImpl extends ServiceImpl<ManufacturerDao, Manufacturer> implements ManufacturerService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public List<Integer> resolveBindAccount() {
        SysUser current = sysUserService.getCurrentUser();
        List<Manufacturer> manufacturers = selectList(new EntityWrapper<Manufacturer>().eq("sys_user_id", current.getId()));
        if (CollectionUtils.isNotEmpty(manufacturers)) {
            return manufacturers.stream().map(Manufacturer::getSysAccountId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
