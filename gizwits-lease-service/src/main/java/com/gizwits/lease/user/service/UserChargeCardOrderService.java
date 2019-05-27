package com.gizwits.lease.user.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.user.dto.*;
import com.gizwits.lease.user.entity.UserChargeCardOrder;

/**
 * <p>
 * 充值卡充值订单 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-08-29
 */
public interface UserChargeCardOrderService extends IService<UserChargeCardOrder> {

	Page<UserChargeCardChargeHistoryListDto> list(Pageable<UserChargeCardOrderQueryDto> pageable);

	UserChargeCardOrder createChargeCardOrder(ChargeCardOrderDto cardOrderDto);

	void updateCardOrderStatus(String orderNo, Integer toStatus);

	void updateCardOrderStatus(UserChargeCardOrder userChargeCardOrder, Integer toStatus);

	boolean checkAndUpdateCardOrder(String orderNo, Double totalFee, String tardeNo);

	Page<UserChargeCardRechargeRecordDto> getRechargeRecord(Pageable<UserChargeCardRechargeRecordQueryDto> dto);
}
