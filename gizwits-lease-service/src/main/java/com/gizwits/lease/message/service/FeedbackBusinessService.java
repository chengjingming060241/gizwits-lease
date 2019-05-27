package com.gizwits.lease.message.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.message.entity.FeedbackBusiness;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.message.entity.dto.FeedbackBusinessDto;
import com.gizwits.lease.message.entity.dto.FeedbackUserDto;
import com.gizwits.lease.message.entity.dto.FeedbackQueryDto;

/**
 * <p>
 * 业务系统表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
public interface FeedbackBusinessService extends IService<FeedbackBusiness> {

    /**
     * 系统业务分页查询
     * @param pageable
     * @return
     */
    Page<FeedbackBusiness> page(Pageable<FeedbackQueryDto> pageable);

    /**
     * 统计该用户同一天发送的消息
     * @param userId
     * @return
     */
    Integer countByUserId(Integer userId);

    void insertFeedbackBusiness(FeedbackBusinessDto feedbackDto);
	
}
