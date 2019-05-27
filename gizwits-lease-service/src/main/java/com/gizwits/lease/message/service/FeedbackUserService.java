package com.gizwits.lease.message.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.lease.message.entity.FeedbackUser;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.lease.message.entity.dto.FeedbackUserDto;
import com.gizwits.lease.message.entity.dto.FeedbackQueryDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 问题反馈表 服务类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
public interface FeedbackUserService extends IService<FeedbackUser> {

    /**
     * 分页列表查询
     * @param pageable
     * @return
     */
    Page<FeedbackUser> page(Pageable<FeedbackQueryDto> pageable);

    Integer countByUserName(String userName);

    void insertFeedbackUser(FeedbackUserDto feedbackDto);

    boolean saveUserFeedback(List<MultipartFile> fileList, String sno, String phone, String content, Integer origin, String openid);
}
