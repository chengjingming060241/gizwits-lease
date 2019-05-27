package com.gizwits.lease.order.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.gizwits.lease.order.dto.LastOrderForUserWalletChargeOrderDto;
import com.gizwits.lease.order.dto.OrderBaseListDto;
import com.gizwits.lease.order.dto.OrderQueryDto;
import com.gizwits.lease.order.entity.OrderBase;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
  * 订单表 Mapper 接口
 * </p>
 *
 * @author rongmc
 * @since 2017-06-30
 */
public interface OrderBaseDao extends BaseMapper<OrderBase> {
    OrderBase findByTradeNo(String tradeNo);

    Map<String,Number> findForStatOrder(@Param("sno") String sno, @Param("date") Date date, @Param("status")List<Integer> status);

    /**
     * 微信支付
     * @param sno
     * @param date
     * @param status
     * @return
     */
    Map<String,Number> findForStatOrderWx(@Param("sno") String sno, @Param("date") Date date, @Param("status")List<Integer> status);

    /**
     * 钱包支付
     * @param sno
     * @param date
     * @param status
     * @return
     */
    Map<String,Number> findForStatOrderWallet(@Param("sno") String sno, @Param("date") Date date, @Param("status")List<Integer> status);

    List<OrderBase> findByUserIdAndStatus(@Param("userId")Integer userId, @Param("orderStatus")Integer orderStatus);

    OrderBase findByDeviceIdAndStatus(@Param("deviceId")String deviceId, @Param("orderStatus")Integer orderStatus);

    Integer getOrderCount(@Param("sysUserId") Integer sysUserId, @Param("productId") Integer productId,
                          @Param("fromDate") Date fromDate,@Param("toDate") Date toDate,@Param("status") List<Integer> orderStatus,
                           @Param("format")String dataFormar);

    List<OrderBaseListDto> listPage(OrderQueryDto orderQueryDto);

    List<OrderBaseListDto> appListPage(OrderQueryDto orderQueryDto);

    Integer findTotalSize(OrderQueryDto orderQueryDto);

    Integer findAppListTotalSize(OrderQueryDto orderQueryDto);

    List<OrderBase> listReadyForShareBenefitOrder(@Param("lastExecuteTime") Date lastExecuteTime, @Param("sno") String sno);


    Integer countUsingOrderByUserIdAndProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);

    Double appListPaySum(OrderQueryDto orderQueryDto);

    Date earliestOrderTime();

    OrderBase findLastUsingDevicePort(@Param("sno")String sno,@Param("port")int port,@Param("status")Integer status);

    OrderBase findUsingOrderByUserIdAndProductId(@Param("userId")Integer userId,@Param("productId")Integer productId);

    LastOrderForUserWalletChargeOrderDto getLastOrderForUserWalletChargeOrder(@Param("userId") Integer userId, @Param("lastDate") Date lastDate);

}