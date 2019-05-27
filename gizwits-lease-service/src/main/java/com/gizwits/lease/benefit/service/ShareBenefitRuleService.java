package com.gizwits.lease.benefit.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.benefit.dto.ShareBenefitDeviceRangeVo;
import com.gizwits.lease.benefit.dto.ShareBenefitOperatorObjectDto;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.benefit.entity.dto.*;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.manager.entity.Operator;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
public interface ShareBenefitRuleService extends IService<ShareBenefitRule> {

    /**
     * 获取所有可用的分润规则
     * @return
     */
    List<ShareBenefitRule> getAllUsableShareRule();
    /**
     * 分润列表
     * @param pageable
     * @return
     */
    Page<ShareBenefitRuleListDto> listPage(Pageable<ShareBenefitRuleQueryDto> pageable);

    /**
     * 插入分润规则
     * @param shareBenefitRuleDto
     * @return
     */
    boolean insertShareBenefitRule(ShareBenefitRuleDto shareBenefitRuleDto);

    /**查询该运营商的分润规则
     * @param operatorId
     * @return
     */
    ShareBenefitRule getRuleBySysAccountId(Integer operatorId);

    /**
     *  查询分润规则名是否存在（同一用户下）
     * @param checkDto
     * @return
     */
    boolean selectNameIsExist(ShareBenefitRuleNameCheckDto checkDto);

    /**
     * 更新分润规则
     * @param shareBenefitRuleDto
     * @return
     */
    boolean updateShareBenefitRule(ShareBenefitRuleDto shareBenefitRuleDto);

    /**
     * 分润规则详情
     * @param ruleId
     * @return
     */
    ShareBenefitRuleDto shareBenefitRuleDetail(String ruleId);

    /**
     * 查询直属子运营商
     * @return
     */
    List<Operator> listSonOperator(Integer sysAccountId);

    /**
     * 查询该运营商及其直属子运营商的设备
     * @param pageable
     * @return
     */
    Page<Device> listDeviceByOperatorId(Pageable<OperatorDeviceDto> pageable);

    /**
     * 获得详细分润数据
     * @param ruleId
     * @return
     */
    List<ShareBenefitRuleDetailDto> getShareBenefitRuleDetailDtoList(String ruleId, Integer sysAccountId);

    /**
     * 获取当前用户的直接子运营用户
     * @return
     */
    List<ShareBenefitOperatorObjectDto> listShareRuleOperatorObject();

    /**
     * 获取一批设备在currentOperatorSysAccountId的分润比例区间
     * @param deviceSnoList
     * @param currentOperatorSysAccountId
     * @return
     */
    ShareBenefitDeviceRangeVo calculateShareRuleRange(List<String> deviceSnoList, Integer currentOperatorSysAccountId);

    /**
     * 回调函数获取当前用户的下一级运营者列表
     * @param sysUserId
     * @param results
     */
    void resolveChildrenOperatorIds(Integer sysUserId, List<Integer> results);

    /**
     * 回调函数获取当前用户的下一级的分润规则列表
     * @param sysUserId
     * @param ruleList
     */
    void resolveChildrenRules(Integer sysUserId, List<ShareBenefitRule> ruleList);

    String deleteShareBenefitRule(List<String> ids);

    boolean updateRuleLastExecuteTime(String ruleId);
}
