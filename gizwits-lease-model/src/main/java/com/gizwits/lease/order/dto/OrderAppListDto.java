package com.gizwits.lease.order.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by xian on 28/8/2017.
 */
public class OrderAppListDto implements Serializable {

    private Date beginTime;
    private Date endTime;
    private Double totalPay; //总费用
    private List<OrderListDto> orderListDtoList;
}
