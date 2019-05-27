package com.gizwits.lease.stat.service;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.stat.entity.StatUserWidget;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.stat.vo.StatUserWidgetVo;

import java.util.List;

/**
 * <p>
 * 设备订单看板数据统计表 服务类
 * </p>
 *
 * @author gagi
 * @since 2017-07-18
 */
public interface StatUserWidgetService extends IService<StatUserWidget> {

    void setDataForWidget();

    StatUserWidgetVo widget(SysUser currentUser, List<Integer> ids);
}
