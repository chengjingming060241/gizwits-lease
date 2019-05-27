package com.gizwits.lease.stat.dao;

import com.gizwits.lease.stat.entity.StatUserWidget;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
  * 设备订单看板数据统计表 Mapper 接口
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
public interface StatUserWidgetDao extends BaseMapper<StatUserWidget> {

    int updateByUtimeAndSysUserId(@Param("widget") StatUserWidget statUserWidget);

    StatUserWidget widget(@Param("sysUserId") Integer id,@Param("date") Date now);

    StatUserWidget widgetByIds(@Param("ids") List<Integer> ids,@Param("date") Date now);
}