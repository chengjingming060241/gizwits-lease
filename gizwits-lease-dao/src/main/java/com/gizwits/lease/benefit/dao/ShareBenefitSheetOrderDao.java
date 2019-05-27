package com.gizwits.lease.benefit.dao;

import com.gizwits.lease.benefit.entity.ShareBenefitSheetOrder;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 所有要参与分润的订单 Mapper 接口
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
public interface ShareBenefitSheetOrderDao extends BaseMapper<ShareBenefitSheetOrder> {

    /**
     * 修改分润订单的状态
     *
     * @param sheetNo   分润单号
     * @param preStatus 指定状态的分润订单
     * @param status    修改后的状态
     * @return
     */
    int updateOrderStatusBySheetNo(@Param("sheetNo") String sheetNo, @Param("preStatus") Integer preStatus, @Param("status") Integer status);

    Integer getOrderCount(@Param("sysUserId") Integer ownerId, @Param("productId") Integer productId, @Param("date") Date now);

    List<ShareBenefitSheetOrder> selectSheetOrderByRuleIdAndSnoAndOrderNo(@Param("ruleId")String ruleId, @Param("orderNo")String orderNo, @Param("sno")String sno);

    BigDecimal sumSheetOrderMoney(@Param("sheetNo")String sheetNo, @Param("orderList")List<Integer> orderList);

    ShareBenefitSheetOrder selectSumOrderAmountAndShareMoney(@Param("sheetNo") String sheetNo, @Param("orderNoList") List<String> orderNoList);
    BigDecimal sumSheetOrderTotalMoney(@Param("sheetNo")String sheetNo);

    int updateBatchSheetOrderStatus(@Param("status")Integer status, @Param("sheetNo")String sheetNo, @Param("sheetOrderList")List<ShareBenefitSheetOrder> sheetOrderList);

    int updateAllSheetOrderStatus(@Param("status")Integer toStatus, @Param("sheetNo")String sheetNo);
}