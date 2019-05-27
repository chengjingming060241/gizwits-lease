package com.gizwits.lease.benefit.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.benefit.dto.*;
import com.gizwits.lease.benefit.entity.ShareBenefitRule;
import com.gizwits.lease.benefit.entity.ShareBenefitSheet;
import com.gizwits.lease.benefit.entity.ShareBenefitSheetOrder;
import com.gizwits.lease.benefit.entity.dto.ShareBenefitRuleDeviceVo;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.order.entity.OrderBase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 分润账单表 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
public interface ShareBenefitSheetService extends IService<ShareBenefitSheet> {

    Double total(ShareBenefitSheetForQueryDto queryDto);

    /**
     * 列表
     */
    Page<ShareBenefitSheetForListDto> page(Pageable<ShareBenefitSheetForQueryDto> pageable);


    boolean generateShareBenefitForShareRule(ShareBenefitRule rule);
    /**
     * 详情
     */
    ShareBenefitSheetForDetailDto detail(ShareBenefitSheetForQueryDto query);

    /**
     * 审核
     */
    ShareBenefitSheetForDetailDto audit(ShareBenefitSheetForAuditDto dto);

    /**
     * 重新审核
     */
    ShareBenefitSheetForDetailDto reaudit(ShareBenefitSheetForAuditDto dto);

    /**
     * 执行分润
     * @param query
     * @return
     */
    ResponseObject  executeShareBenefit(ShareBenefitSheetForQueryDto query);


    //boolean generateShareSheetForDevices(Map<String,List<ShareBenefitRuleDeviceVo>> deviceShareRules);

    ShareBenefitSheet generateSheetOrder(List<OrderBase> orderList, ShareBenefitRuleDeviceVo deviceVo, String sheetNo, List<ShareBenefitSheetOrder> sheetOrderList, ShareBenefitSheet sheet);

    boolean generateShareBenefitSheetForDevices(List<Device> deviceList, Boolean isUnbind);

    Map<String,Object> calculateSheetMoney(String sheetNo,List<Integer> excludeOrderList);

    Map<String,List> groupOrderByStatus(String sheetNo);

    void executeAllShareBenefit();

    void updateReceiverInfo(String oldOpenId, String newOpenId, String oldReceiverWxName, String newReceiverWxName, Integer sysAccountId);
}
