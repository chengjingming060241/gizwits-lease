package com.gizwits.lease.device.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.device.entity.DeviceAlarm;
import com.baomidou.mybatisplus.service.IService;

import com.gizwits.lease.device.entity.dto.*;
import com.gizwits.lease.device.vo.DeviceAlarmListVo;

import com.gizwits.lease.device.entity.dto.DeviceAlarmAppListDto;
import com.gizwits.lease.device.entity.dto.DeviceAlarmListPageQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceAlarmQueryDto;
import com.gizwits.lease.device.entity.dto.DeviceAlarmDetailDto;
import com.gizwits.lease.device.entity.dto.DeviceAlramInfoDto;

import com.gizwits.lease.product.entity.ProductDataPoint;


import java.util.List;

/**
 * <p>
 * 设备故障(警告)记录表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-15
 */
public interface DeviceAlarmService extends IService<DeviceAlarm> {

    /**
     *  统计该用户有多少告警设备记录
     * @param deviceAlarmQueryDto
     * @return
     */
     Integer countDeviceAlram(DeviceAlarmQueryDto deviceAlarmQueryDto);

    /**
     * 分页查询
     * @param deviceAlarmQueryDto
     * @return
     */
     List<DeviceAlramInfoDto> listPage(DeviceAlarmQueryDto deviceAlarmQueryDto);

    /**
     * app端分页查询
     * @param deviceAlarmAppListDto
     * @return
     */
    List<DeviceAlramAppInfoDto> appListPage(DeviceAlarmAppListDto deviceAlarmAppListDto);

    /**
     * 通过故障id查询故障信息和所属设备的分页
     * @param id
     * @return
     */
     DeviceAlarmDetailDto getDeviceAlramInfoById(Pageable<DeviceAlarmListPageQueryDto> pageable, Integer id);

    /**
     * 通过设备sno分页
     * @param pageable
     * @param deviceSno
     * @return
     */
    Page<DeviceAlramInfoDto> listPageByDeviceSon(Pageable<DeviceAlarmListPageQueryDto> pageable, String deviceSno);

    /**
     * 查询故障名称
     * @param snos
     * @return
     */
   List<String> getDeviceAlarmNameBySno(List<String> snos);

    /**
     * 通过设备序列号查询故障参数
     * @param snos
     * @return
     */
	List<String> getDeviceAlarmAttrBySno(List<String> snos);

    /**
     * 推送故障信息
     * @param deviceAlarm
     */
     void sendDeviceAlarmMessage(DeviceAlarm deviceAlarm);

     /** 查找设备指定未解决故障
     * @param mac
     * @param productKey
     * @param attr
     * @return
     */
    DeviceAlarm getUnresolveAlarm(String mac,String productKey,String attr);

    /**
     * 保存并推送消息
     * @param deviceAlarm
     * @return
     */
    boolean saveOne(DeviceAlarm deviceAlarm);

    /**
     * 得到设备序列号
     * @param deviceAlarmQueryDto
     * @return
     */
    public List<String> getDeviceSnos(DeviceAlarmQueryDto deviceAlarmQueryDto);

    /**
     * 根据传入的产品id判断查询序列号
     * @param productId
     * @return
     */
    public List<String> getSnos(Integer productId);

    /**
     * 通过数据点信息查询故障信息
     * @param productDataPoint
     * @return
     */
    DeviceAlarm getDeviceAlarmByProductDataPoint(ProductDataPoint productDataPoint);

    /**
     * 设备分页
     * @param deviceAlarmQueryDto
     * @return
     */
    Page getPage(DeviceAlarmQueryDto deviceAlarmQueryDto);

    /**
     * App端设备分页
     * @param deviceAlarmAppListDto
     * @return
     */
    Page getAppPage(DeviceAlarmAppListDto deviceAlarmAppListDto);

    /**
     * 通过产品获取设备sno
     * @param productId
     * @return
     */
    List<String> getDeviceSnoByProductId(Integer productId);

    /**
     * 根据投放点查询故障信息
     * @param pageable
     * @return
     */
    Page<DeviceAlarmListVo>  deviceAlarmListManager(Pageable<String> pageable);
}
