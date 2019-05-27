package com.gizwits.lease.stat.dao;

import com.gizwits.lease.stat.dto.StatFaultDto;
import com.gizwits.lease.stat.entity.StatFaultAlertType;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-08-15
 */
public interface StatFaultAlertTypeDao extends BaseMapper<StatFaultAlertType> {

    List<StatFaultAlertType> getDataForStat(@Param("dto") StatFaultDto data, @Param("ids") List<Integer> ids);

    int updateByCtimeAndSysUserIdAndSno(@Param("date") Date now, @Param("sysUserId") Integer ownerId,
            @Param("sno") String sno, @Param("identity_name") String identity_name, @Param("count") Integer count);
}