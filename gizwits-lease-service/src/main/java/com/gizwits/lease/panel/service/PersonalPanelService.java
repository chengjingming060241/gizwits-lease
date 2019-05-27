package com.gizwits.lease.panel.service;

import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.panel.dto.ItemDto;
import com.gizwits.lease.panel.dto.ItemForUpdateDto;
import com.gizwits.lease.panel.dto.MainPageShowPanelDto;
import com.gizwits.lease.panel.entity.PersonalPanel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
public interface PersonalPanelService extends IService<PersonalPanel> {

    /**
     * 用于定时任务回写
     *
     * @param sysUserId  系统账号
     * @param moduleItem 具体的统计项,参考PanelModuleItemType
     * @param itemValue  统计项对应的值
     * @param itemOdd
     * @param panelType  数据展示或图表展示
     * @return 是否更新成功
     */
    boolean updateItemValue(Integer sysUserId, Integer moduleItem, String itemValue, String itemOdd, Integer panelType);

    /**
     * 回写数据展示类数据
     *
     * @param sysUserId  系统账号
     * @param moduleItem 数据统计项,参考PanelModuleItemType
     * @param itemValue  统计项对应的值
     * @param itemOdd
     * @return 是否更新成功
     */
    boolean updateDataItemValue(Integer sysUserId, Integer moduleItem, String itemValue, String itemOdd);

    /**
     * 回写图表展示类数据
     *
     * @param sysUserId  系统账号
     * @param moduleItem 图表统计项,参考PanelModuleItemType
     * @param itemValue  统计项对应的值
     * @return 是否更新成功
     */
    boolean updateChartItemValue(Integer sysUserId, Integer moduleItem, String itemValue);

    /**
     * 首页默认展示的模块
     */
    MainPageShowPanelDto getCurrentShowPanelData(SysUser current);

    /**
     * 编辑面板
     */
    Map<String, Map<String, List<ItemDto>>> getEditPanelData(SysUser current);

    /**
     * 更新
     */
    boolean update(ItemForUpdateDto dto);

}
