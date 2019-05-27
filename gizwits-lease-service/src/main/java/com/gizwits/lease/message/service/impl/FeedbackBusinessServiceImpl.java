package com.gizwits.lease.message.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.message.entity.FeedbackBusiness;
import com.gizwits.lease.message.dao.FeedBackBusinessDao;
import com.gizwits.lease.message.entity.dto.FeedbackBusinessDto;
import com.gizwits.lease.message.entity.dto.FeedbackUserDto;
import com.gizwits.lease.message.entity.dto.FeedbackQueryDto;
import com.gizwits.lease.message.service.FeedbackBusinessService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 业务系统表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
@Service
public class FeedbackBusinessServiceImpl extends ServiceImpl<FeedBackBusinessDao, FeedbackBusiness> implements FeedbackBusinessService {

    @Autowired
    private SysUserService sysUserService;


    @Override
    public Page<FeedbackBusiness> page(Pageable<FeedbackQueryDto> pageable) {
        SysUser sysUser = sysUserService.getCurrentUser();
        Page<FeedbackBusiness> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<FeedbackBusiness> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("recipient_id", sysUser.getId());
        return selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
    }

    @Override
    public Integer countByUserId(Integer userId) {
        EntityWrapper<FeedbackBusiness> entityWrapper = new EntityWrapper<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        entityWrapper.eq("user_id", userId).like("ctime", sdf.format(date));
        return selectCount(entityWrapper);
    }

    @Override
    public void insertFeedbackBusiness(FeedbackBusinessDto feedbackDto) {
        SysUser sysUser = sysUserService.getCurrentUser();
        int count = countByUserId(sysUser.getId());
        if (count > 10) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_OPRATION.getCode(), SysExceptionEnum.ILLEGAL_OPRATION.getMessage());
        }
        FeedbackBusiness feedbackBusiness = new FeedbackBusiness();
        feedbackBusiness.setCtime(new Date());
        feedbackBusiness.setContent(feedbackDto.getContent());
        feedbackBusiness.setPictureNum(feedbackDto.getPictureNum());
        feedbackBusiness.setPictureUrl(feedbackDto.getPictureUrl());
        feedbackBusiness.setRecipientId(feedbackDto.getRecipientId());
        feedbackBusiness.setRecipientName(feedbackDto.getRecipientName());

        feedbackBusiness.setMobile(sysUser.getMobile());
        feedbackBusiness.setNickName(sysUser.getNickName());
        feedbackBusiness.setUserId(sysUser.getId());
        feedbackBusiness.setUserName(sysUser.getUsername());
        feedbackBusiness.setAvatar(sysUser.getAvatar());
        insert(feedbackBusiness);
    }

}
