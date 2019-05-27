package com.gizwits.lease.message.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.message.entity.SysMessage;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.message.entity.dto.RoleDto;
import com.gizwits.lease.message.entity.dto.SysMessageAdd;
import com.gizwits.lease.message.entity.dto.SysMessageForAddpage;
import com.gizwits.lease.message.entity.dto.SysMessageListDto;
import com.gizwits.lease.message.entity.dto.SysMessageQueryDto;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 系统消息表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
public interface SysMessageService extends IService<SysMessage> {

    /**
     * 分页查询
     * @param pageable
     * @return
     */
     Page<SysMessageListDto> getSysMessagePage(Pageable<SysMessageQueryDto> pageable);

    /**
     * 添加页面所需数据
     * @return
     */
    SysMessageForAddpage listRoles();

    /**
     * 添加
     * @param sysMessageAdd
     * @return
     */
    boolean addSysMessage(SysMessageAdd sysMessageAdd);

    /**
     * 定时发送
     */
    void sendByTime(String date);

    /**
     * 详情
     * @param id
     */
    SysMessageListDto detail(Integer id);

    /**
     * 根据openid查询这个用户相关的公众号曾经发送的消息列表
     * @param openid
     * @return
     */
    List<SysMessageListDto> getMessagePage(String openid);

    /**
     * 根据openid查询这个用户相关的公众号曾经发送的消息列表
     * @param pageable
     * @return
     */
    Page<SysMessage> getMessagePage(Pageable<String> pageable);

    /**
     * 沁尔康查看用户推送到公众号的订单信息
     * @param pageable
     * @return
     */
    Page<SysMessage> getUserOrderMessage(Pageable<String> pageable);

    /**
     * 沁尔康：清空订单信息
     * @param mobile
     */
    void clearOrder(String mobile);

    /**
     * 沁尔康：清空运营商发送的消息
     * @param mobile
     */
    void clearMessage(String mobile);
}
