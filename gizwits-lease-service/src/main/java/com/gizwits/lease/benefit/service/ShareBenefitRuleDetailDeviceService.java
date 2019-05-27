package com.gizwits.lease.benefit.service;

import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetailDevice;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitDeviceUpdateDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDetailDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分润规则详细设备表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
public interface ShareBenefitRuleDetailDeviceService extends IService<ShareBenefitRuleDetailDevice> {

    boolean insertRuledetailDevice(ShareBenefitRuleDetailDto shareBenefitRuleDetailDto,Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap);

    boolean updateRuleDetailDevice(ShareBenefitRuleDetailDto shareBenefitRuleDetailDto, ShareBenefitRuleDetail detail);

    List<ShareBenefitRuleDetailDevice> selectByRuleDetailId(String ruleDetailId);

    boolean deleteRuleDetailDevice(String ruleDetailId);

    ShareBenefitRuleDeviceVo getDeviceShareRuleBySysAccountId(String sno, Integer sysAccountId);

    /**
     * 获取指定规则下设备的分润规则
     * @param ruleId
     * @param sysAccountId
     * @param snoList
     * @return
     */
    List<ShareBenefitRuleDeviceVo> getDeviceShareRuleByRuleIdOrSysAccountIdAndSno(String ruleId,Integer sysAccountId, List<String> snoList);

    boolean updateDevicePercentage(ShareBenefitDeviceUpdateDto updateDto);

    List<ShareBenefitRuleDeviceVo> getAllDeviceShareRuleByRuleId(String ruleId);

    List<ShareBenefitRuleDeviceVo> getDeviceRuleListBySno(String sno);

    boolean updateDeviceChildrenPercentage(String sno, BigDecimal childrenPercentage, String ruleId);

    boolean deletedDeviceRuleInShareRule(String ruleId, String sno);
}
