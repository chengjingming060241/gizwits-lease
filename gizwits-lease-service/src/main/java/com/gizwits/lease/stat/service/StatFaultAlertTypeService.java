package com.gizwits.lease.stat.service;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.dto.StatFaultDto;
import com.gizwits.lease.stat.entity.StatFaultAlertType;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.stat.vo.StatFaultVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gagi
 * @since 2017-08-15
 */
public interface StatFaultAlertTypeService extends IService<StatFaultAlertType> {

    List<StatFaultVo> getDataForStat(StatFaultDto data, SysUser currentUser, List<Integer> ids);

    void setDataForType();

}
