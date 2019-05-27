package com.gizwits.lease.message.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.enums.DeleteStatus;
import com.gizwits.boot.sys.entity.SysRole;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.entity.SysUserToRole;
import com.gizwits.boot.sys.service.SysRoleService;
import com.gizwits.boot.sys.service.SysUserExtService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.sys.service.SysUserToRoleService;
import com.gizwits.boot.utils.ParamUtil;
import com.gizwits.boot.utils.QueryResolverUtils;
import com.gizwits.lease.constant.SysMessageEnum;
import com.gizwits.lease.enums.IsTrueEnum;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.message.dao.SysMessageDao;
import com.gizwits.lease.message.entity.SysMessage;
import com.gizwits.lease.message.entity.SysMessageToUser;
import com.gizwits.lease.message.entity.dto.RoleDto;
import com.gizwits.lease.message.entity.dto.SysMessageAdd;
import com.gizwits.lease.message.entity.dto.SysMessageForAddpage;
import com.gizwits.lease.message.entity.dto.SysMessageListDto;
import com.gizwits.lease.message.entity.dto.SysMessageQueryDto;
import com.gizwits.lease.message.service.SysMessageService;
import com.gizwits.lease.message.service.SysMessageToUserService;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserWxExt;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.user.service.UserWxExtService;
import com.gizwits.lease.util.WxUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 系统消息表 服务实现类
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
@Service
public class SysMessageServiceImpl extends ServiceImpl<SysMessageDao, SysMessage> implements SysMessageService {

   protected final static Logger logger_wx = LoggerFactory.getLogger("WEIXIN_LOGGER");

   protected final static Logger logger = LoggerFactory.getLogger("MESSAGE_LOGGER");


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserToRoleService sysUserToRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMessageToUserService sysMessageToUserService;

    @Autowired
    private SysUserExtService sysUserExtService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserWxExtService userWxExtService;


    @Override
    public Page<SysMessageListDto> getSysMessagePage(Pageable<SysMessageQueryDto> pageable) {
        SysUser sysUser = sysUserService.getCurrentUser();
        logger.info("查看用户：" + sysUser.getId());
        Integer typeId = pageable.getQuery().getType();
        if (typeId.equals(SysMessageEnum.ADDRESSER_ID.getCode())) {
            return getOutboxPage(pageable, sysUser);
        } else if (typeId.equals(SysMessageEnum.RECIPIENT_ID.getCode())) {
            return getInboxPage(pageable, sysUser);
        }
        return null;
    }

    /**
     * 根据openid查询这个用户相关的公众号曾经发送的消息列表
     */
    @Override
    public List<SysMessageListDto> getMessagePage(String openid) {
        User user = userService.getUserByIdOrOpenidOrMobile(openid);
        if (user == null) {
            return null;
        }
        EntityWrapper<SysUserExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("wx_id", user.getWxId());
        SysUserExt sysUserExt = sysUserExtService.selectOne(extEntityWrapper);
        if (sysUserExt == null) {
            return null;
        }
        EntityWrapper<SysMessage> messageEntityWrapper = new EntityWrapper<>();
        messageEntityWrapper.eq("addresser_id", sysUserExt.getSysUserId());
        List<SysMessage> list = selectList(messageEntityWrapper);
        if (ParamUtil.isNullOrEmptyOrZero(list)) {
            return null;
        }
        List<SysMessageListDto> sysMessageListDtoList = new ArrayList<>();
        for (SysMessage message : list) {
            SysMessageListDto sysMessageListDto = getSysMessageListDto(message);
            sysMessageListDtoList.add(sysMessageListDto);
        }

        return sysMessageListDtoList;
    }

    @Override
    public Page<SysMessage> getMessagePage(Pageable<String> pageable) {
        Integer operatorId = getInteger(pageable.getQuery());
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("addresser_id",operatorId).eq("is_read",0);
        Page<SysMessage> page = new Page<>();
        BeanUtils.copyProperties(pageable,page);
        return selectPage(page,QueryResolverUtils.parse(pageable.getQuery(),entityWrapper));
    }

    private Integer getInteger(String  openId) {
        logger.info("微信openId或者手机号="+openId);
        if(ParamUtil.isNullOrEmptyOrZero(openId)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        User user = userService.getUserByIdOrOpenidOrMobile(openId);
        if(user==null){
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        EntityWrapper<SysUserExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("wx_id",user.getWxId());
        SysUserExt sysUserExt = sysUserExtService.selectOne(extEntityWrapper);
        if(sysUserExt==null){
            return null;
        }
        Integer operatorId = sysUserExt.getSysUserId();
        logger.info("微信用户所属运营商operatorId="+operatorId);
        if (ParamUtil.isNullOrEmptyOrZero(operatorId)) {
            LeaseException.throwSystemException(LeaseExceEnums.OPERATOR_DONT_EXISTS);
        }
        return operatorId;
    }

    @Override
    public SysMessageForAddpage listRoles() {
        SysMessageForAddpage sysMessageForAddpage = new SysMessageForAddpage();
        SysUser sysUser = sysUserService.getCurrentUser();
        logger.info("用户{[]}创建角色" + sysUser.getId());
        List<SysRole> sysRoles = sysRoleService.selectList(new EntityWrapper<SysRole>().eq("sys_user_id", sysUser.getId()));
        List<RoleDto> roleDtos = new ArrayList<>();
        for (SysRole role : sysRoles) {
            RoleDto roleDto = new RoleDto();
            roleDto.setRoleId(role.getId());
            roleDto.setRoleName(role.getRoleName());
            roleDtos.add(roleDto);
        }
        SysUserExt sysUserExt = sysUserExtService.selectById(sysUser.getId());
        if (!Objects.isNull(sysUserExt)) {
            sysMessageForAddpage.setIsBindWeChat(IsTrueEnum.TRUE.getCode());
        }
        sysMessageForAddpage.setRoleDtoList(roleDtos);
        return sysMessageForAddpage;
    }

    @Override
    public boolean addSysMessage(SysMessageAdd sysMessageAdd) {
        logger.info("添加消息");
        SysUser sysUser = sysUserService.getCurrentUser();
        sendMessage(sysMessageAdd, sysUser);
        return false;
    }

    @Override
    public void sendByTime(String date) {
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.like("ctime", date);
        List<SysMessage> sysMessages = selectList(entityWrapper);
        if (Objects.isNull(sysMessages)) {
            return;
        }
        for (SysMessage sysMessage : sysMessages) {
            if (Objects.equals(sysMessage.getIsSend(), IsTrueEnum.FALSE.getCode())) {
                sysMessage.setIsSend(IsTrueEnum.TRUE.getCode());
                updateById(sysMessage);
                List<SysMessageToUser> list = sysMessageToUserService.selectList(new EntityWrapper<SysMessageToUser>().eq("sys_message_id", sysMessage.getId()));
                for (SysMessageToUser sysMessageToUser : list) {
                    sysMessageToUser.setIsSend(IsTrueEnum.TRUE.getCode());
                    sysMessageToUserService.updateById(sysMessageToUser);
                }
                if (Objects.equals(IsTrueEnum.TRUE.getCode(), sysMessage.getIsBindWx())) {
                    SysUser sysUser = new SysUser();
                    sysUser.setId(sysMessage.getAddresserId());
                    sysUser.setUsername(sysMessage.getAddresserName());
                    SysMessageAdd sysMessageAdd = new SysMessageAdd();
                    sysMessageAdd.setTitle(sysMessage.getTitle());
                    sysMessageAdd.setContent(sysMessage.getContent());
                    SendMessageToWeChat(sysMessageAdd, sysUser);
                }
            }
        }
    }


    @Override
    public SysMessageListDto detail(Integer id) {
        logger.info("消息详情：" + id);
        SysMessage sysMessage = selectOne(new EntityWrapper<SysMessage>().eq("id", id)
                .eq("is_send", 1));
        sysMessage.setIsRead(IsTrueEnum.TRUE.getCode());
        updateById(sysMessage);
        SysMessageListDto sysMessageListDto = getSysMessageListDto(sysMessage);
        List<SysMessageToUser> list = sysMessageToUserService.selectList(new EntityWrapper<SysMessageToUser>().eq("sys_message_id", sysMessage.getId()));
        for (SysMessageToUser sysMessageToUser : list) {
            sysMessageToUser.setIsRead(IsTrueEnum.TRUE.getCode());
            sysMessageToUserService.updateById(sysMessageToUser);
        }
        return sysMessageListDto;
    }

    /**
     * 发送消息
     */
    public void sendMessage(SysMessageAdd sysMessageAdd, SysUser sysUser) {
        if (Objects.equals(sysMessageAdd.getIsSendToSysUser(), IsTrueEnum.TRUE.getCode())) {
            logger.info("发送消息到系统用户");
            //发送消息到系统用户
            insert(sysMessageAdd, sysUser);
        }

        if (Objects.equals(IsTrueEnum.TRUE.getCode(), sysMessageAdd.getIsSendNow())
                && Objects.equals(sysMessageAdd.getIsBindWeChat(), IsTrueEnum.TRUE.getCode())) {
            logger.info("发送消息到微信公众号");
            SendMessageToWeChat(sysMessageAdd, sysUser);

        }
    }

    /**
     * 推送微信消息
     */
    public void SendMessageToWeChat(SysMessageAdd sysMessageAdd, SysUser sysUser) {
        SysUserExt sysUserExt = sysUserExtService.selectById(sysUser.getId());
        if (ParamUtil.isNullOrEmptyOrZero(sysUserExt)) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_SYS_USER_EXT_NOT_EXIST);
        }
        String wxId = sysUserExt.getWxId();
        List<String> openIds = new ArrayList<>();
        List<UserWxExt> users = userWxExtService.selectList(new EntityWrapper<UserWxExt>().eq("is_deleted",DeleteStatus.NOT_DELETED.getCode()).eq("wx_id", wxId).eq("status", 1));
        if (ParamUtil.isNullOrEmptyOrZero(users)) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXXIN_USER_DONT_EXISTS);
        }
        for (UserWxExt user : users) {
            if (users.size() == 1) {
                openIds.add(user.getOpenid());
            }
            openIds.add(user.getOpenid());
        }
        JSONObject sendData = new JSONObject();
        sendData.put("touser", openIds);
        sendData.put("msgtype", "text");
        JSONObject textData = new JSONObject();
        String value = "标题：" + sysMessageAdd.getTitle() + "，内容：" + sysMessageAdd.getContent();
        textData.put("content", value);
        sendData.put("text", textData);
        //发送文本消息到微信
        WxUtil.sendManyNotices(sendData.toJSONString(), sysUserExt);
        logger_wx.info("推送消息时间：" + new Date() + "，微信id：" + wxId);
    }

    /**
     * 插入系统消息表和消息用户表
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(SysMessageAdd sysMessageAdd, SysUser sysUser) {
        SysMessage sysMessage = new SysMessage();
        sysMessage.setContent(sysMessageAdd.getContent());
        Date date = null;
        Integer isTrue = 0;
        Integer isBindWx = 0;
        if (Objects.equals(IsTrueEnum.FALSE.getCode(), sysMessageAdd.getIsSendNow())) {
            date = sysMessageAdd.getSendtime();
        } else if (Objects.equals(IsTrueEnum.TRUE.getCode(), sysMessageAdd.getIsSendNow())) {
            date = new Date();
            isTrue = 1;
        }

        if (Objects.equals(IsTrueEnum.TRUE.getCode(), sysMessageAdd.getIsBindWeChat())) {
            isBindWx = 1;
        }
        sysMessage.setCtime(date);
        sysMessage.setTitle(sysMessageAdd.getTitle());
        sysMessage.setAddresserId(sysUser.getId());
        sysMessage.setAddresserName(sysUser.getUsername());
        sysMessage.setIsSend(isTrue);
        sysMessage.setIsBindWx(isBindWx);
        logger.info("发送消息时间：" + date + ",发件人：" + sysUser.getId());
        insert(sysMessage);

        if (Objects.equals(sysMessageAdd.getIsSendToSysUser(), IsTrueEnum.TRUE.getCode())) {
            InsertSysMessageToUser(sysMessageAdd, sysUser, date, isTrue, sysMessage.getId());
        }
    }

    /**
     * 插入消息用户表
     */
    public void InsertSysMessageToUser(SysMessageAdd sysMessageAdd, SysUser sysUser, Date date, Integer isTrue, Integer sysMessageId) {
        List<RoleDto> roles = sysMessageAdd.getRoleDtoList();
        if (ParamUtil.isNullOrEmptyOrZero(roles)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        List<Integer> roleIds = new ArrayList<>(roles.size());
        for (RoleDto roleDto : roles) {
            roleIds.add(roleDto.getRoleId());
        }
        EntityWrapper<SysUserToRole> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_deleted", DeleteStatus.NOT_DELETED.getCode()).in("role_id", roleIds).groupBy("user_id");
        List<SysUserToRole> list = sysUserToRoleService.selectList(entityWrapper);

        for (SysUserToRole sysUserToRole : list) {
            SysMessageToUser sysMessageToUser = new SysMessageToUser();
            sysMessageToUser.setSysMessageId(sysMessageId);
            sysMessageToUser.setCtime(date);
            sysMessageToUser.setRoleId(sysUserToRole.getRoleId());
            String roleName = sysRoleService.selectById(sysUserToRole.getRoleId()).getRoleName();
            sysMessageToUser.setRoleName(roleName);
            sysMessageToUser.setUserId(sysUserToRole.getUserId());
            SysUser sysUser1 = sysUserService.selectById(sysUserToRole.getUserId());
            if (ParamUtil.isNullOrEmptyOrZero(sysUser1)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
            String username = sysUser1.getUsername();
            sysMessageToUser.setUsername(username);
            sysMessageToUser.setIsSend(isTrue);
            sysMessageToUser.setIsRead(0);
            sysMessageToUserService.insert(sysMessageToUser);
        }
    }

    /**
     * 收件箱
     */
    public Page<SysMessageListDto> getInboxPage(Pageable<SysMessageQueryDto> pageable, SysUser sysUser) {
        logger.info("收件箱");
        List<Integer> sysMessageIds = sysMessageToUserService.listSysMessageId(sysUser.getId());
        if (ParamUtil.isNullOrEmptyOrZero(sysMessageIds)){
            return new Page<SysMessageListDto>();
        }
        Page<SysMessage> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.in("id", sysMessageIds)
                .eq("is_send", 1).orderBy("ctime", false);
        Page<SysMessageListDto> result = getSysMessageListDtoPage(pageable, page, entityWrapper);
        return result;

    }

    /**
     * 发件箱
     */
    public Page<SysMessageListDto> getOutboxPage(Pageable<SysMessageQueryDto> pageable, SysUser sysUser) {
        logger.info("发件箱");
        Page<SysMessage> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("addresser_id", sysUser.getId())
                .eq("is_send", 1).orderBy("ctime", false);
        Page<SysMessageListDto> result = getSysMessageListDtoPage(pageable, page, entityWrapper);
        return result;
    }

    public Page<SysMessageListDto> getSysMessageListDtoPage(Pageable<SysMessageQueryDto> pageable, Page<SysMessage> page, EntityWrapper<SysMessage> entityWrapper) {
        Page<SysMessage> page1 = selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
        List<SysMessageListDto> sysMessageListDtoList = new ArrayList<>();
        for (SysMessage message : page1.getRecords()) {
            SysMessageListDto sysMessageListDto = getSysMessageListDto(message);
            sysMessageListDtoList.add(sysMessageListDto);
        }
        Page<SysMessageListDto> result = new Page<>();
        BeanUtils.copyProperties(page1, result);
        result.setRecords(sysMessageListDtoList);
        return result;
    }

    public SysMessageListDto getSysMessageListDto(SysMessage message) {
        SysMessageListDto sysMessageListDto = new SysMessageListDto();
        sysMessageListDto.setId(message.getId());
        sysMessageListDto.setTitle(message.getTitle());
        sysMessageListDto.setContent(message.getContent());
        sysMessageListDto.setSentPerson(message.getAddresserName());
        String reciepient = sysMessageToUserService.concatRoleName(message.getId());
        sysMessageListDto.setRecepit(reciepient);
        sysMessageListDto.setTime(message.getCtime());
        return sysMessageListDto;
    }

    /**
     * 沁尔康用户查询已完成订单消息（手机号／openId）
     */
    @Override
    public Page<SysMessage> getUserOrderMessage(Pageable<String> pageable) {
        String mobile = pageable.getQuery();
        logger.info("微信用户 openid或mobile="+mobile);
        if (ParamUtil.isNullOrEmptyOrZero(mobile)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        User user = userService.getUserByIdOrOpenidOrMobile(mobile);
        Page<SysMessage> page = new Page<>();
        BeanUtils.copyProperties(pageable, page);
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_read", 0).eq("addresser_id", user.getId());
        return selectPage(page, QueryResolverUtils.parse(pageable.getQuery(), entityWrapper));
    }

    @Override
    public void clearOrder(String mobile) {
        if (ParamUtil.isNullOrEmptyOrZero(mobile)) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        User user = userService.getUserByIdOrOpenidOrMobile(mobile);
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("is_read", 0).eq("addresser_id", user.getId());
        List<SysMessage> list = selectList(entityWrapper);
        for (SysMessage sysMessage : list) {
            sysMessage.setIsRead(1);
            updateById(sysMessage);
        }
    }

    @Override
    public void clearMessage(String mobile){
        Integer operatorId = getInteger(mobile);
        EntityWrapper<SysMessage> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("addresser_id",operatorId).eq("is_read",0);
        List<SysMessage> list = selectList(entityWrapper);
        for (SysMessage sysMessage : list) {
            sysMessage.setIsRead(1);
            updateById(sysMessage);
        }
    }
}
