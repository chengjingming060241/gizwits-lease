package com.gizwits.lease.benefit.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetailDevice;
import com.gizwits.lease.benefit.dao.ShareBenefitRuleDetailDeviceDao;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitDeviceUpdateDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDetailDeviceDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDetailDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailDeviceService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.lease.constant.TradeOrderType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;


/**
 * <p>
 * 分润规则详细设备表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@Service
public class ShareBenefitRuleDetailDeviceServiceImpl extends ServiceImpl<ShareBenefitRuleDetailDeviceDao, ShareBenefitRuleDetailDevice> implements ShareBenefitRuleDetailDeviceService {

    @Autowired
    private ShareBenefitRuleDetailDeviceDao shareBenefitRuleDetailDeviceDao;

    @Override
    public boolean insertRuledetailDevice(ShareBenefitRuleDetailDto shareBenefitRuleDetailDto, Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap) {
        List<ShareBenefitRuleDetailDeviceDto> detailDeviceDtos = shareBenefitRuleDetailDto.getRuleDetailDeviceDtoList();
        for (ShareBenefitRuleDetailDeviceDto deviceDto : detailDeviceDtos) {
            ShareBenefitRuleDetailDevice shareBenefitRuleDetailDevice = getShareBenefitRuleDetailDevice(shareBenefitRuleDetailDto, deviceDto);
            shareBenefitRuleDetailDevice.setCtime(new Date());
            shareBenefitRuleDetailDevice.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
            if(MapUtils.isNotEmpty(childrenDeviceRuleMap)
                    &&childrenDeviceRuleMap.containsKey(deviceDto.getSno())){
                shareBenefitRuleDetailDevice.setChildrenPercentage(childrenDeviceRuleMap.get(deviceDto.getSno()).getSharePercentage());
            }else{
                shareBenefitRuleDetailDevice.setChildrenPercentage(BigDecimal.ZERO);
            }
            insert(shareBenefitRuleDetailDevice);
        }
        return true;
    }

    public ShareBenefitRuleDetailDevice getShareBenefitRuleDetailDevice(ShareBenefitRuleDetailDto shareBenefitRuleDetailDto, ShareBenefitRuleDetailDeviceDto d) {
        ShareBenefitRuleDetailDevice shareBenefitRuleDetailDevice = new ShareBenefitRuleDetailDevice();
        shareBenefitRuleDetailDevice.setRuleDetailId(shareBenefitRuleDetailDto.getRuleDetailId());
        shareBenefitRuleDetailDevice.setSno(d.getSno());
        shareBenefitRuleDetailDevice.setSharePercentage(shareBenefitRuleDetailDto.getSharePercentage());
        shareBenefitRuleDetailDevice.setUtime(new Date());
        return shareBenefitRuleDetailDevice;
    }

    @Override
    public boolean updateRuleDetailDevice(ShareBenefitRuleDetailDto shareBenefitRuleDetailDto, ShareBenefitRuleDetail detail) {
        List<ShareBenefitRuleDetailDeviceDto> detailDeviceDtos = shareBenefitRuleDetailDto.getRuleDetailDeviceDtoList();
        for (ShareBenefitRuleDetailDeviceDto d : detailDeviceDtos) {
            ShareBenefitRuleDetailDevice shareBenefitRuleDetailDevice = getShareBenefitRuleDetailDevice(shareBenefitRuleDetailDto, d);
            if (ParamUtil.isNullOrEmptyOrZero(d.getRuleDetailDeviceId())) {
                shareBenefitRuleDetailDevice.setCtime(new Date());
                insert(shareBenefitRuleDetailDevice);
            } else {
                shareBenefitRuleDetailDevice.setIsDeleted(1);
                updateById(shareBenefitRuleDetailDevice);
            }
        }
        return false;
    }

    @Override
    public List<ShareBenefitRuleDetailDevice> selectByRuleDetailId(String ruleDetailId) {
        EntityWrapper<ShareBenefitRuleDetailDevice> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("rule_detail_id", ruleDetailId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectList(entityWrapper);
    }

    @Override
    public boolean deleteRuleDetailDevice(String ruleDetailId) {
        EntityWrapper<ShareBenefitRuleDetailDevice> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("rule_detail_id", ruleDetailId);
        ShareBenefitRuleDetailDevice shareBenefitRuleDetailDevice = selectOne(entityWrapper);
        if(!Objects.isNull(shareBenefitRuleDetailDevice)){
            shareBenefitRuleDetailDevice.setIsDeleted(1);
            updateById(shareBenefitRuleDetailDevice);
        }

        return false;
    }

    public ShareBenefitRuleDeviceVo getDeviceShareRuleBySysAccountId(String sno, Integer sysAccountId){
        return shareBenefitRuleDetailDeviceDao.selectRuleBySnoAndSysAccountId(sno, sysAccountId);
    }

    /**
     * 获取指定规则下设备的分润规则
     * @param ruleId
     * @param sysAccountId
     * @param snoList
     * @return
     */
    @Override
    public List<ShareBenefitRuleDeviceVo> getDeviceShareRuleByRuleIdOrSysAccountIdAndSno(String ruleId,Integer sysAccountId, List<String> snoList){
        if (ParamUtil.isNullOrEmptyOrZero(ruleId) && ParamUtil.isNullOrEmptyOrZero(sysAccountId)){
            return null;
        }
        return shareBenefitRuleDetailDeviceDao.selectRuleDevicesBySnos(ruleId, sysAccountId, snoList);
    }

    public List<ShareBenefitRuleDeviceVo> getDeviceRuleListBySno(String sno){
        if(ParamUtil.isNullOrEmptyOrZero(sno)){
            return null;
        }
        return shareBenefitRuleDetailDeviceDao.selectRulesBySno(sno);
    }

    public List<ShareBenefitRuleDeviceVo> getAllDeviceShareRuleByRuleId(String ruleId){
        if(ParamUtil.isNullOrEmptyOrZero(ruleId))
            return null;
        return shareBenefitRuleDetailDeviceDao.selectDeviceRuleByRuleId(ruleId);
    }

    public boolean updateDeviceChildrenPercentage(String sno, BigDecimal childrenPercentage, String ruleId){
        ShareBenefitDeviceUpdateDto updateDto = new ShareBenefitDeviceUpdateDto();
        updateDto.setChildrenPercentage(childrenPercentage);
        updateDto.setRuleId(ruleId);
        List<String> snoList = new ArrayList<>();
        snoList.add(sno);
        updateDto.setDeviceSnoList(snoList);
        return updateDevicePercentage(updateDto);
    }

    public boolean updateDevicePercentage(ShareBenefitDeviceUpdateDto updateDto){
        if(Objects.isNull(updateDto)){
            return false;
        }
        return shareBenefitRuleDetailDeviceDao.updateParentDeviceSharePercentage(updateDto)>0;
    }

    public boolean deletedDeviceRuleInShareRule(String ruleId, String sno){
        if(ParamUtil.isNullOrEmptyOrZero(ruleId) || ParamUtil.isNullOrEmptyOrZero(sno)){
            return false;
        }
        return shareBenefitRuleDetailDeviceDao.updateDeviceForOperatorToDeleted(ruleId, sno)>0;
    }
}
