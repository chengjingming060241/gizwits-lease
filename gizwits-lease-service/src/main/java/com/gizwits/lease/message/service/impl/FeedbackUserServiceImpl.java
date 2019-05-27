package com.gizwits.lease.message.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.FeedBackUserType;
import com.gizwits.lease.device.entity.Device;
import com.gizwits.lease.device.service.DeviceService;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.message.dao.FeedbackUserDao;
import com.gizwits.lease.message.entity.FeedbackUser;
import com.gizwits.lease.message.entity.dto.FeedbackQueryDto;
import com.gizwits.lease.message.entity.dto.FeedbackUserDto;
import com.gizwits.lease.message.service.FeedbackUserService;
import com.gizwits.lease.product.service.ProductService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserWxExt;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.user.service.UserWxExtService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 * 问题反馈表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
@Service
public class FeedbackUserServiceImpl extends ServiceImpl<FeedbackUserDao, FeedbackUser> implements FeedbackUserService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserWxExtService userWxExtService;

    @Autowired
    private SysUserExtService sysUserExtService;

    protected static Logger logger = LoggerFactory.getLogger(FeedbackUserServiceImpl.class);

    @Override
    public Page<FeedbackUser> page(Pageable<FeedbackQueryDto> pageable) {
        /*List<Product> products = productService.getProductsWithPermission();
        List<Integer> productIds = new ArrayList<>();
        for (Product p : products) {
            productIds.add(p.getId());
        }
        List<String> deviceSno = new ArrayList<>();
        if (!ParamUtil.isNullOrEmptyOrZero(productIds)) {
            deviceSno = deviceService.getSnosByProductId(productIds);
        }*/

        Page<FeedbackUser> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<FeedbackUser> entityWrapper = new EntityWrapper<>();
        SysUser sysUser = sysUserService.getCurrentUser();
        entityWrapper.eq("recipient_id", sysUser.getId());
        /*if (!ParamUtil.isNullOrEmptyOrZero(deviceSno)) {
            entityWrapper.eq("recipient_id", sysUser.getId());
            entityWrapper.in("sno", deviceSno);
        } else {
            entityWrapper.eq("recipient_id", sysUser.getId());
        }*/
        return selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
    }

    @Override
    public Integer countByUserName(String userName) {
        EntityWrapper<FeedbackUser> entityWrapper = new EntityWrapper<>();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        entityWrapper.eq("username", userName).like("ctime", sdf.format(date));
        return selectCount(entityWrapper);
    }

    @Override
    public void insertFeedbackUser(FeedbackUserDto feedbackDto) {
        String openId = feedbackDto.getOpenId();
        User user = userService.getUserByIdOrOpenidOrMobile(openId);
        int count = countByUserName(user.getUsername());
        if (count > 10) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_OPRATION.getCode(), SysExceptionEnum.ILLEGAL_OPRATION.getMessage());
        }
        FeedbackUser feedbackUser = new FeedbackUser();
        feedbackUser.setCtime(new Date());
        feedbackUser.setAvatar(user.getAvatar());
        feedbackUser.setMobile(user.getMobile());
        feedbackUser.setNickName(user.getNickname());
        feedbackUser.setUserName(user.getUsername());

        feedbackUser.setOrigin(feedbackDto.getOrigin());
        feedbackUser.setContent(feedbackDto.getContent());
        feedbackUser.setPictureNum(feedbackDto.getPictureNum());
        feedbackUser.setPictureUrl(feedbackDto.getPictureUrl());


    }

    @Override
    public boolean saveUserFeedback(List<MultipartFile> fileList, String sno, String phone, String content, Integer origin, String openid) {
        StringBuffer filenames = new StringBuffer();
        FeedbackUser feedbackUser = new FeedbackUser();
        if (fileList != null && fileList.size() > 0) {
            if (fileList.size() > 5) {
                LeaseException.throwSystemException(LeaseExceEnums.UPLOAD_TOO_MANY_PICTURES);
            }
            feedbackUser.setPictureNum(fileList.size());
            try {
                for (MultipartFile file : fileList) {
                    if (!file.getOriginalFilename().contains(".")) {
                        LeaseException.throwSystemException(LeaseExceEnums.PICTURE_SUFFIX_ERROR);
                    }
                    if (!file.getContentType().equals("image/jpeg")) {
                        LeaseException.throwSystemException(LeaseExceEnums.PICTURE_SUFFIX_ERROR);
                    }
                    String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                    String newFilename = UUID.randomUUID() + suffix;
                    filenames.append(newFilename).append(";");
                    file.transferTo(new File(SysConfigUtils.get(CommonSystemConfig.class).getFeedBackImagePath() + newFilename));
                }
            } catch (Exception ex) {
                logger.error("==============>图片上传失败，原因：" + ex);
                ex.printStackTrace();
            }
        }
        feedbackUser.setPictureUrl(filenames.toString());
        if (StringUtils.isEmpty(content)) {
            content = "默认的问题反馈内容";
        }
        feedbackUser.setContent(content);
        if (ParamUtil.isNullOrEmptyOrZero(origin)) {
            origin = FeedBackUserType.USER.getCode();
        }
        feedbackUser.setOrigin(origin);
        if (org.apache.commons.lang.StringUtils.isNotEmpty(sno)) {
            Device device = deviceService.selectById(sno);
            if (device != null) {
                feedbackUser.setSno(device.getSno());
                feedbackUser.setMac(device.getMac());
            }
        } else {
            feedbackUser.setSno("设备号");
            feedbackUser.setMac("mac");
        }
        feedbackUser.setCtime(new Date());
        feedbackUser.setUserName(phone);
        feedbackUser.setMobile(phone);
        User user = userService.getUserByIdOrOpenidOrMobile(openid);
        if (Objects.isNull(user)) {
             user = userService.getUserByMobile(phone);
        }
        if (!Objects.isNull(user)) {
            feedbackUser.setAvatar(user.getAvatar());
            feedbackUser.setNickName(user.getNickname());
            feedbackUser.setUserName(user.getUsername());
        }
        UserWxExt userWxtExt = userWxExtService.getByOpenid(user.getOpenid());
        if (Objects.nonNull(userWxtExt)){
            SysUserExt sysUserExt = sysUserExtService.selectOne(new EntityWrapper<SysUserExt>().eq("wx_id", userWxtExt.getWxId()));
            if (Objects.nonNull(sysUserExt)){
                SysUser sysUser = sysUserService.selectById(sysUserExt.getSysUserId());
                feedbackUser.setRecipientName(sysUser.getUsername());
                feedbackUser.setRecipientId(sysUser.getId());
            }
        }
        return insert(feedbackUser);
    }


}
