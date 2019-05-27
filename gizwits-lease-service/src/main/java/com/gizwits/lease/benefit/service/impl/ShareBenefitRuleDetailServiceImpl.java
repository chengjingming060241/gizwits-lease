package com.gizwits.lease.benefit.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.app.utils.LeaseUtil;
import com.gizwits.lease.benefit.entity.ShareBenefitRuleDetail;
import com.gizwits.lease.benefit.dao.ShareBenefitRuleDetailDao;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDetailDeviceDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDetailDto;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDto;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailDeviceService;
import com.gizwits.lease.benefit.service.ShareBenefitRuleDetailService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.gizwits.lease.constant.BooleanEnum;
import com.gizwits.lease.constant.TradeOrderType;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.message.service.SysMessageService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 分润规则详细表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-08-01
 */
@Service
public class ShareBenefitRuleDetailServiceImpl extends ServiceImpl<ShareBenefitRuleDetailDao, ShareBenefitRuleDetail> implements ShareBenefitRuleDetailService {

    protected final static Logger logger = LoggerFactory.getLogger("BENEFIT_LOGGER");


    @Autowired
    private ShareBenefitRuleDetailDao shareBenefitRuleDetailDao;

    @Autowired
    private ShareBenefitRuleDetailDeviceService shareBenefitRuleDetailDeviceService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMessageService sysMessageService;

    @Override
    public boolean insertShareBenefitRuleDetail(ShareBenefitRuleDto shareBenefitRuleDto) {

        List<ShareBenefitRuleDetailDto> ruleDetailDtoList = shareBenefitRuleDto.getRuleDetailDtoList();
        Set<String> deviceSnoSet = new HashSet<>();
        for (ShareBenefitRuleDetailDto detailDto : ruleDetailDtoList) {
            ShareBenefitRuleDetail shareBenefitruledetail = getShareBenefitRuleDetailAndUpdate(shareBenefitRuleDto, detailDto);
            detailDto.setRuleDetailId(shareBenefitruledetail.getId());

            shareBenefitRuleDetailDeviceService.insertRuledetailDevice(filterRepeatSno(detailDto, deviceSnoSet),null);
            logger.info("插入分润详情【" + shareBenefitruledetail.getId() + "】");
        }
        return true;
    }

    private ShareBenefitRuleDetailDto filterRepeatSno(ShareBenefitRuleDetailDto detailDto, Set<String> deviceSnoSet){
        List<ShareBenefitRuleDetailDeviceDto> detailDeviceDtos = detailDto.getRuleDetailDeviceDtoList();
        if (CollectionUtils.isEmpty(detailDeviceDtos)){
            return detailDto;
        }

        List<ShareBenefitRuleDetailDeviceDto> noRepeatSnoList = new ArrayList<>();
        for (ShareBenefitRuleDetailDeviceDto detailDeviceDto:detailDeviceDtos){
            if (!deviceSnoSet.contains(detailDeviceDto.getSno())){
                deviceSnoSet.add(detailDeviceDto.getSno());
                noRepeatSnoList.add(detailDeviceDto);
            }
        }
        detailDto.setRuleDetailDeviceDtoList(noRepeatSnoList);
        return detailDto;
    }

    public ShareBenefitRuleDetail getShareBenefitRuleDetailAndUpdate(ShareBenefitRuleDto shareBenefitRuleDto, ShareBenefitRuleDetailDto detailDto) {
        ShareBenefitRuleDetail shareBenefitruledetail = new ShareBenefitRuleDetail();
        if(Objects.nonNull(detailDto.getRuleDetailId())){
            shareBenefitruledetail = selectById(detailDto.getRuleDetailId());
            if(Objects.isNull(shareBenefitruledetail)||shareBenefitruledetail.getIsDeleted().equals(BooleanEnum.TRUE.getCode())){
                logger.error("=====分润规则明细{}不存在====",detailDto.getRuleDetailId());
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
        }else{
            shareBenefitruledetail.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
            shareBenefitruledetail.setCtime(new Date());
            shareBenefitruledetail.setRuleId(shareBenefitRuleDto.getRuleId());
        }
        shareBenefitruledetail.setRuleId(shareBenefitRuleDto.getRuleId());
        shareBenefitruledetail.setUtime(new Date());
        shareBenefitruledetail.setName(detailDto.getRuleDetailName());
        shareBenefitruledetail.setSharePercentage(detailDto.getSharePercentage());
        shareBenefitruledetail.setShareType(detailDto.getShareType());
        insertOrUpdate(shareBenefitruledetail);
        return shareBenefitruledetail;
    }

    private ShareBenefitRuleDetail createShareBenefitRuleDetail(ShareBenefitRuleDto shareBenefitRuleDto, ShareBenefitRuleDetailDto detailDto){
        ShareBenefitRuleDetail shareBenefitruledetail = new ShareBenefitRuleDetail();
        shareBenefitruledetail.setId(LeaseUtil.generateOrderNo(TradeOrderType.SHARE.getCode()));
        shareBenefitruledetail.setCtime(new Date());
        shareBenefitruledetail.setRuleId(shareBenefitRuleDto.getRuleId());
        shareBenefitruledetail.setRuleId(shareBenefitRuleDto.getRuleId());
        shareBenefitruledetail.setUtime(new Date());
        shareBenefitruledetail.setName(detailDto.getRuleDetailName());
        shareBenefitruledetail.setSharePercentage(detailDto.getSharePercentage());
        shareBenefitruledetail.setShareType(detailDto.getShareType());
        insertOrUpdate(shareBenefitruledetail);
        return shareBenefitruledetail;
    }



    @Override
    public ShareBenefitRuleDetail selectByRuleIdAndName(String ruleId, String name) {
        logger.info("查询详细分润条件：ruleId=" + ruleId + ",name=" + name);
        EntityWrapper<ShareBenefitRuleDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("rule_id", ruleId).eq("name", name)
                .eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectOne(entityWrapper);
    }

    /**
     * 修改分润规则明细
     *
     * 1.删除所有旧的分润规则明细和设备规则明细
     * 2.插入新的分润规则明细及设备明细
     *
     * @param shareBenefitRuleDto
     * @param childrenDeviceRuleMap
     * @return
     */
    @Override
    public boolean updateShareBenefitRuleDatail(ShareBenefitRuleDto shareBenefitRuleDto,Map<String,ShareBenefitRuleDeviceVo> childrenDeviceRuleMap) {
        if(shareBenefitRuleDetailDao.updateRuleDetailAndDeviceToDeleted(shareBenefitRuleDto.getRuleId())<=0){
            logger.error("======修改分润规则{}的明细规则和设备为删除状态失败====",shareBenefitRuleDto.getRuleId());
            LeaseException.throwSystemException(LeaseExceEnums.SHARE_ACTION_FAIL);
        }
        List<ShareBenefitRuleDetailDto> ruleDetailDtoList = shareBenefitRuleDto.getRuleDetailDtoList();
        Set<String> deviceSnoSet = new HashSet<>();
        for (ShareBenefitRuleDetailDto detailDto : ruleDetailDtoList) {
            ShareBenefitRuleDetail shareBenefitruledetail = createShareBenefitRuleDetail(shareBenefitRuleDto, detailDto);
            detailDto.setRuleDetailId(shareBenefitruledetail.getId());
            shareBenefitRuleDetailDeviceService.insertRuledetailDevice(filterRepeatSno(detailDto,deviceSnoSet), childrenDeviceRuleMap);
        }
        return true;
    }

    /**
     * 修改分润规则后，通知分润对象
     * @param shareBenefitRuleDto
     */
    private void sendShareBenefitRuleModifiedMessage(ShareBenefitRuleDto shareBenefitRuleDto) {
        String content = "";
        for (ShareBenefitRuleDetailDto detailDto : shareBenefitRuleDto.getRuleDetailDtoList()) {
            content += "您的分润规则 " + detailDto.getRuleDetailName() + " 已被修改，分润比例为" + detailDto.getSharePercentage() + "%,\n";
        }
        content += "如有疑问，请联系相关人员。";

//        sysMessageService.sendMessage("分润规则修改通知", content, Arrays.asList(shareBenefitRuleDto.getSysAccountId()));
    }

    @Override
    public void deleteShareBenefitRuleDatailByIds(List<String> ids) {
        logger.info("删除详细分润：" + ids);
        List<ShareBenefitRuleDetail> list = selectBatchIds(ids);
        for (ShareBenefitRuleDetail d : list) {
            d.setIsDeleted(1);
            updateById(d);
            shareBenefitRuleDetailDeviceService.deleteRuleDetailDevice(d.getId());
        }
    }

    @Override
    public List<ShareBenefitRuleDetail> selectByRuleId(String ruleId) {
        logger.info("通过ruleId=" + ruleId + "查询详细分润");
        EntityWrapper<ShareBenefitRuleDetail> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("rule_id", ruleId).eq("is_deleted", DeleteStatus.NOT_DELETED.getCode());
        return selectList(entityWrapper);
    }

    @Override
    public void deleteShareBenefitRuleDatail(List<ShareBenefitRuleDetail> list) {
        for (ShareBenefitRuleDetail d : list) {
            d.setIsDeleted(1);
            updateById(d);
            shareBenefitRuleDetailDeviceService.deleteRuleDetailDevice(d.getId());
        }
    }


    public ShareBenefitRuleDetail getRuleDetailByRuleIdAndSno(Integer ruleId,String sno){
        return shareBenefitRuleDetailDao.findDetailByRuleIdAndSno(ruleId,sno);
    }

}
