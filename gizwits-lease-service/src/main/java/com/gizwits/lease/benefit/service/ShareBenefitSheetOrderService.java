package com.gizwits.lease.benefit.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.benefit.entity.ShareBenefitSheetOrder;
import com.gizwits.lease.order.entity.dto.SheetOrderForListDto;
import com.gizwits.lease.order.entity.dto.SheetOrderForQueryDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 所有要参与分润的订单 服务类
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
public interface ShareBenefitSheetOrderService extends IService<ShareBenefitSheetOrder> {

    /**
     * 列表
     */
    Page<SheetOrderForListDto> page(Pageable<SheetOrderForQueryDto> pageable);

    boolean updateSheetOrderStatus(String sheetNo,Integer status,Integer preStatus);

    boolean checkOrderIsShareForRule(String ruleId, String orderNo, String sno);

    BigDecimal sumSheetOrderMoney(String sheetNo, List<Integer> excludeOrderList);

    ShareBenefitSheetOrder sumOrderAmountAndShareMoney(String sheetNo, List<String> orderNoList);
    BigDecimal sumSheetOrderTotalMoney(String sheetNo);

    /**
     * 根据分润单号查询所有的订单
     * @param sheetNo
     * @return
     */
    List<ShareBenefitSheetOrder> getShareBenefitSheerOrderBySheetNo(String sheetNo);

    /**
     * 批量修改分润订单的状态
     * @param status
     * @param sheetNo
     * @param sheetOrderList
     * @return
     */
    boolean updateBatchSheetOrderListStatus(Integer status, String sheetNo, List<ShareBenefitSheetOrder> sheetOrderList);

    /**
     * 修改分润单中所有订单的状态
     * @param sheetNo
     * @param toStatus
     * @return
     */
    boolean updateAllSheetOrderStatus(String sheetNo, Integer toStatus);
}
