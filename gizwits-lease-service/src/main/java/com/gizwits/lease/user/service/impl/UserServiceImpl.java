package com.gizwits.lease.user.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.response.AlipayUserUserinfoShareResponse;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.gizwits.boot.api.SmsApi;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.dto.JwtAuthenticationDto;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.dto.SysUserForgetPasswordDto;
import com.gizwits.boot.exceptions.SysExceptionEnum;
import com.gizwits.boot.exceptions.SystemException;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.*;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.SexType;
import com.gizwits.lease.constant.ThirdPartyUserType;
import com.gizwits.lease.constant.UserStatus;

import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.constant.WalletEnum;
import com.gizwits.lease.enums.MoveType;
import com.gizwits.lease.event.NameModifyEvent;
import com.gizwits.lease.exceptions.LeaseExceEnums;
import com.gizwits.lease.exceptions.LeaseException;
import com.gizwits.lease.order.service.OrderBaseService;
import com.gizwits.lease.user.dao.UserDao;
import com.gizwits.lease.user.dto.*;
import com.gizwits.lease.user.entity.User;
import com.gizwits.lease.user.entity.UserWxExt;
import com.gizwits.lease.user.service.UserChargeCardService;
import com.gizwits.lease.user.dto.UserForDetailDto;
import com.gizwits.lease.user.dto.UserForInfoDto;
import com.gizwits.lease.user.dto.UserForListDto;
import com.gizwits.lease.user.dto.UserForMoveDto;
import com.gizwits.lease.user.dto.UserForQueryDto;
import com.gizwits.lease.user.dto.UserForRegisterDto;
import com.gizwits.lease.user.dto.UserForUpdatePwdDto;
import com.gizwits.lease.user.dto.UserLoginDto;
import com.gizwits.lease.user.dto.UserUpdateDto;
import com.gizwits.lease.user.service.UserService;
import com.gizwits.lease.user.service.UserWxExtService;
import com.gizwits.lease.wallet.entity.UserWallet;
import com.gizwits.lease.wallet.service.UserWalletService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 用户表,不要前缀,因为用户模块计划抽象成通用功能 服务实现类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    protected final static Logger logger = LoggerFactory.getLogger("USER_LOGGER");

    @Autowired
    private UserWxExtService userWxExtService;

    @Autowired
    private SimpleCacheManager cacheManager;

    @Autowired
    private OrderBaseService orderBaseService;

    private static Map<MoveType, UserStatus> map = new HashMap<>();

    private static Map<String, String> code = new HashMap<>();

    static {
        map.put(MoveType.MOVE_IN_BLACK, UserStatus.BLACK);
        map.put(MoveType.MOVE_OUT_BLACK, UserStatus.NORMAL);
    }

    @Autowired
    private UserDao userDao;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 根据openid获取用户信息
     *
     * @param userIdentify 如果是微信用户,此处的openid对应的是各个公众号的生成的openid
     */
    @Autowired
    private UserWalletService userWalletService;

    @Autowired
    private UserChargeCardService userChargeCardService;

    @Override
    public User getUserByIdOrOpenidOrMobile(String userIdentify) {
        if (ParamUtil.isNullOrEmptyOrZero(userIdentify)) {
            return null;
        }
        //先判断ID
        User dbUser = selectById(userIdentify);
        if (dbUser != null) {
            return dbUser;
        }

        //判断是否是微信用户
        EntityWrapper<UserWxExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("openid", userIdentify);
        UserWxExt dbUserWxExt = userWxExtService.selectOne(extEntityWrapper);
        if (dbUserWxExt != null) {
            dbUser = userDao.findByOpenid(dbUserWxExt.getUserOpenid());
            if (dbUser == null) {
                dbUser = userDao.findByOpenid(dbUserWxExt.getOpenid());
            }
            dbUser.setMoveInBlackTime(dbUserWxExt.getMoveInBlackTime());
            dbUser.setMoveOutBlackTime(dbUserWxExt.getMoveOutBlackTime());
            dbUser.setStatus(dbUserWxExt.getStatus());
            dbUser.setOpenid(dbUserWxExt.getOpenid());
            dbUser.setAuthorizationTime(dbUserWxExt.getAuthorizationTime());
            dbUser.setWxId(dbUserWxExt.getWxId());
            return dbUser;
        }

        //在判断是否是手机号或第三方ID
        dbUser = userDao.findByOpenid(userIdentify);
        if (dbUser != null) {
            EntityWrapper<UserWxExt> userWxExtEntityWrapper = new EntityWrapper<>();
            userWxExtEntityWrapper.eq("user_openid", dbUser.getOpenid()).orderBy("ctime", false);
            dbUserWxExt = userWxExtService.selectOne(userWxExtEntityWrapper);
            if (Objects.nonNull(dbUserWxExt)) {
                dbUser.setMoveInBlackTime(dbUserWxExt.getMoveInBlackTime());
                dbUser.setMoveOutBlackTime(dbUserWxExt.getMoveOutBlackTime());
                dbUser.setStatus(dbUserWxExt.getStatus());
                dbUser.setOpenid(dbUserWxExt.getOpenid());
                dbUser.setAuthorizationTime(dbUserWxExt.getAuthorizationTime());
                dbUser.setWxId(dbUserWxExt.getWxId());
            }

            return dbUser;
        } else {
            return null;
        }
    }

    @Override
    public User getUserByOpenidAndExistAndNotInBlack(String openid) {
        EntityWrapper<UserWxExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("openid", openid);
        UserWxExt dbUserWxExt = userWxExtService.selectOne(extEntityWrapper);
        if (dbUserWxExt == null) {//说明不是微信用户
            User user = userDao.findByOpenid(openid);
            if (Objects.isNull(user) || user.getStatus().equals(UserStatus.BLACK)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
            return user;
        } else {
            if (dbUserWxExt.getStatus().equals(UserStatus.BLACK)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
            User user = userDao.findByOpenid(dbUserWxExt.getUserOpenid());
            if (user == null) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
            user.setMoveInBlackTime(dbUserWxExt.getMoveInBlackTime());
            user.setMoveOutBlackTime(dbUserWxExt.getMoveOutBlackTime());
            user.setStatus(dbUserWxExt.getStatus());
            user.setAuthorizationTime(dbUserWxExt.getAuthorizationTime());
            user.setOpenid(dbUserWxExt.getOpenid());
            user.setSysUserId(dbUserWxExt.getSysUserId());
            return user;
        }
    }

    /**
     * 根据微信用户openid获取微信主体信息,返回对象的openid,为用户的unionid
     */
    @Override
    public User getUserNoRecoverByWxOpenid(String wxOpenid) {
        EntityWrapper<UserWxExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("openid", wxOpenid);
        UserWxExt dbUserWxExt = userWxExtService.selectOne(extEntityWrapper);
        if (dbUserWxExt == null) {//说明不是微信用户
            User user = userDao.findByOpenid(wxOpenid);
            if (Objects.isNull(user) || user.getStatus().equals(UserStatus.BLACK)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
        } else {
            EntityWrapper<User> userEntityWrapper = new EntityWrapper<>();
            userEntityWrapper.eq("openid", dbUserWxExt.getUserOpenid());
            User user = selectOne(userEntityWrapper);
            if (user == null) {
                user = selectOne(new EntityWrapper<User>().eq("openid", dbUserWxExt.getOpenid()));
            }
            return user;
        }
        return null;
    }

    /**
     * 获取支付宝信息
     */
    public User addUserByAlipay(AlipayUserUserinfoShareResponse userinfoShareResponse) {
        String userid = userinfoShareResponse.getUserId();
        User dbUser = userDao.findByOpenid(userid);
        if (dbUser != null) {
            dbUser.setAvatar(userinfoShareResponse.getAvatar());
            dbUser.setNickname(userinfoShareResponse.getNickName());
            dbUser.setGender(getAlipayUserGender(userinfoShareResponse.getGender()));
            dbUser.setProvince(userinfoShareResponse.getProvince());
            dbUser.setCity(userinfoShareResponse.getCity());
            dbUser.setUtime(new Date());
            updateById(dbUser);
        } else {
            dbUser = new User();
            String username = userid.substring(userid.length() - 5, userid.length()).replaceAll("-", "").replaceAll("_", "") + IdGenerator.getRandomString(5);
            dbUser.setCtime(new Date());
            dbUser.setUtime(new Date());
            dbUser.setAlipayUnionid(userid);
            dbUser.setNickname(userinfoShareResponse.getNickName());
            dbUser.setUsername(username);
            //dbUser.setPassword("qwe!123");

            dbUser.setGender(getAlipayUserGender(userinfoShareResponse.getGender()));
            dbUser.setProvince(userinfoShareResponse.getProvince());
            dbUser.setCity(userinfoShareResponse.getCity());
            dbUser.setAvatar(userinfoShareResponse.getAvatar());
            dbUser.setThirdParty(ThirdPartyUserType.ALIPAY.getCode());
            insert(dbUser);
        }
        return dbUser;
    }

    private int getAlipayUserGender(String gender) {
        if (ParamUtil.isNullOrEmptyOrZero(gender)) {
            return SexType.OTHER.getCode();
        } else if (gender.toLowerCase().equals("m")) {
            return SexType.MALE.getCode();
        } else if (gender.toLowerCase().equals("f")) {
            return SexType.FEMALE.getCode();
        } else {
            return SexType.OTHER.getCode();
        }
    }

    @Override
    public User addUserByWx(String wxUserinfoJson, SysUserExt sysUserExt, Integer sysUserid) {
        logger.info("userInfo =" + wxUserinfoJson);
        if (StringUtils.isBlank(wxUserinfoJson))
            return null;
        JSONObject jsonObject = JSON.parseObject(wxUserinfoJson);
        String openid = jsonObject.getString("openid");
        String unionid = jsonObject.getString("unionid");
        if (ParamUtil.isNullOrEmptyOrZero(unionid)) {
            unionid = openid;
        }
        // TODO: 2017/8/19 享智云确定只有一个公众号,因此不需要unionid的存在
        /**
         * 注:由于微信用户体系的缘故,相同的用户在每个公众号的openid是不同的,因此需要使用unionId作为微信用户的唯一标示
         * 在User_wx_ext表中存储对应公众号的用户openid
         */
        //查询User主表的ID
        User dbUser = userDao.findByOpenid(unionid);

        EntityWrapper<UserWxExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("openid", openid);
        UserWxExt dbUserWxExt = userWxExtService.selectOne(extEntityWrapper);

        //用户第一次使用平台
        if (dbUser == null) {
            String username = openid.substring(openid.length() - 5, openid.length()).replaceAll("-", "").replaceAll("_", "") + IdGenerator.getRandomString(5);
            dbUser = new User();
            dbUser.setCtime(new Date());
            dbUser.setUtime(new Date());
            dbUser.setOpenid(unionid);
            dbUser.setNickname(jsonObject.getString("nickname"));
            dbUser.setUsername(username);
            //dbUser.setPassword("qwe!123");

            dbUser.setGender(jsonObject.getInteger("sex"));
            dbUser.setProvince(jsonObject.getString("province"));
            dbUser.setCity(jsonObject.getString("city"));
            dbUser.setAvatar(jsonObject.getString("headimgurl"));
            dbUser.setThirdParty(ThirdPartyUserType.WEIXIN.getCode());
            Long lo = jsonObject.getLong("subscribe_time");
            if (!ParamUtil.isNullOrEmptyOrZero(lo)) {
                Date date = new Date(lo * 1000L);
                dbUser.setAuthorizationTime(date);
            }

            dbUserWxExt = new UserWxExt();
            dbUserWxExt.setUserOpenid(unionid);
            dbUserWxExt.setOpenid(openid);
            dbUserWxExt.setWxId(sysUserExt.getWxId());
            dbUserWxExt.setSysUserId(sysUserExt.getSysUserId());
            dbUserWxExt.setCtime(new Date());

        } else {
            if (ParamUtil.isNullOrEmptyOrZero(dbUser.getNickname())) {
                dbUser.setNickname(jsonObject.getString("nickname"));
            }
            if (ParamUtil.isNullOrEmptyOrZero(dbUser.getAuthorizationTime())) {
                Long lo = jsonObject.getLong("subscribe_time");
                if (!ParamUtil.isNullOrEmptyOrZero(lo)) {
                    Date date = new Date(lo * 1000L);
                    dbUser.setAuthorizationTime(date);
                }
            }
            dbUser.setGender(jsonObject.getInteger("sex"));
            dbUser.setProvince(jsonObject.getString("province"));
            dbUser.setCity(jsonObject.getString("city"));
            dbUser.setAvatar(jsonObject.getString("headimgurl"));
            dbUser.setOpenid(unionid);
            dbUser.setUtime(new Date());
            dbUser.setThirdParty(ThirdPartyUserType.WEIXIN.getCode());
            if (dbUserWxExt == null) {
                dbUserWxExt = new UserWxExt();
                dbUserWxExt.setUserOpenid(unionid);
                dbUserWxExt.setOpenid(openid);
                dbUserWxExt.setWxId(sysUserExt.getWxId());
                dbUserWxExt.setSysUserId(sysUserExt.getSysUserId());
                dbUserWxExt.setCtime(new Date());
            } else {
                dbUserWxExt.setUtime(new Date());
            }
        }
        if (sysUserid != null) {
            dbUser.setSysUserId(sysUserid);
        } else {
            dbUser.setSysUserId(sysUserExt.getSysUserId());
        }

        if (insertOrUpdate(dbUser)) {
            userWxExtService.insertOrUpdate(dbUserWxExt);
            return dbUser;
        }
        return null;
    }


    @Override
    public Page<UserForListDto> page(Pageable<UserForQueryDto> pageable) {
        List<Integer> accessableIds = sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser());

        UserForQueryDto queryDto = pageable.getQuery();
        queryDto.setSysUserIds(accessableIds);
        if (pageable.getCurrent() < 1) {
            pageable.setCurrent(1);
        }
        queryDto.setBegin(pageable.getOffsetCurrent());
        queryDto.setSize(pageable.getSize());

        List<User> list = userDao.listPage(queryDto);
        if (ParamUtil.isNullOrEmptyOrZero(list)) {
            return new Page<>();
        }

        Page<UserForListDto> result = new Page<>();
        result.setRecords(new ArrayList<>(list.size()));
        list.forEach(item -> {
            UserForListDto dto = new UserForListDto(item);
            // 暂时将创建时间当作授权时间
            dto.setAuthorizationTime(item.getCtime());
            dto.setGenderDesc(SexType.getName(item.getGender()));
            result.getRecords().add(dto);
        });
        result.setCurrent(pageable.getCurrent());
        result.setSize(pageable.getSize());
        result.setTotal(userDao.findTotalSize(queryDto));
        return result;
    }

    @Override
    public UserForDetailDto detail(String openid) {
        User dbUser = getUserByIdOrOpenidOrMobile(openid);
        if (dbUser != null) {
            UserForDetailDto result = new UserForDetailDto(dbUser);
            result.setGenderDesc(SexType.getName(dbUser.getGender()));
            Integer userId = dbUser.getId();
            UserWallet userWallet = userWalletService.selectUserWallet(userId, WalletEnum.BALENCE.getCode());
            UserWallet userWallet1 = userWalletService.selectUserWallet(userId, WalletEnum.DISCOUNT.getCode());
            Double money = userWallet.getMoney() + userWallet1.getMoney();
            BigDecimal b = new BigDecimal(money);
            double balance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            result.setBalance(balance);
            return result;
        }

        EntityWrapper<UserWxExt> extEntityWrapper = new EntityWrapper<>();
        extEntityWrapper.eq("openid", openid);
        UserWxExt dbUserWxExt = userWxExtService.selectOne(extEntityWrapper);
        if (dbUserWxExt == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }

        User user = userDao.findByOpenid(dbUserWxExt.getUserOpenid());
        user.setMoveInBlackTime(dbUserWxExt.getMoveInBlackTime());
        user.setMoveOutBlackTime(dbUserWxExt.getMoveOutBlackTime());
        user.setStatus(dbUserWxExt.getStatus());
        user.setAuthorizationTime(dbUserWxExt.getAuthorizationTime());
        user.setOpenid(dbUserWxExt.getOpenid());
        user.setSysUserId(dbUserWxExt.getSysUserId());

        UserForDetailDto result = new UserForDetailDto(user);
        result.setGenderDesc(SexType.getName(user.getGender()));
        Integer userId = user.getId();
        UserWallet userWallet = userWalletService.selectUserWallet(userId, WalletEnum.BALENCE.getCode());
        UserWallet userWallet1 = userWalletService.selectUserWallet(userId, WalletEnum.DISCOUNT.getCode());
        Double money = userWallet.getMoney() + userWallet1.getMoney();
        BigDecimal b = new BigDecimal(money);
        double balance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        result.setBalance(balance);
        return result;
    }

    /**
     * 移入移出黑名单
     *
     * @param dto 传递的是openid
     */
    @Override
    public boolean move(UserForMoveDto dto, MoveType moveType) {
        UserStatus toUserStatus = map.get(moveType);
        if (Objects.isNull(toUserStatus)) {
            return false;
        }
        List<User> users = userDao.findByUnionids(dto.getUserIds(), sysUserService.resolveAccessableUserIds(sysUserService.getCurrentUser()));
        if (CollectionUtils.isNotEmpty(users)) {
            List<User> needToMove = users.stream().filter(user -> !Objects.equals(user.getStatus(), toUserStatus.getCode())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(needToMove)) {
                needToMove.forEach(item -> {
                    item.setStatus(toUserStatus.getCode());
                    item.setUtime(new Date());
                    if (Objects.equals(MoveType.MOVE_IN_BLACK, moveType)) {
                        //移入
                        item.setMoveInBlackTime(new Date());
                        item.setMoveOutBlackTime(null);
                    } else {
                        //移出
                        item.setMoveOutBlackTime(new Date());
                        item.setMoveInBlackTime(null);
                    }
                });
                updateBatchById(needToMove);
            }
        }


        EntityWrapper<UserWxExt> entityWrapper = new EntityWrapper<>();
        List openids = users.stream().map(user -> user.getOpenid()).collect(Collectors.toList());
        entityWrapper.in("user_openid", openids);
        List<UserWxExt> wxExtList = userWxExtService.selectList(entityWrapper);

        if (CollectionUtils.isNotEmpty(wxExtList)) {
            List<UserWxExt> needToMove = wxExtList.stream().filter(user -> !Objects.equals(user.getStatus(), toUserStatus.getCode())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(needToMove)) {
                needToMove.forEach(item -> {
                    item.setStatus(toUserStatus.getCode());
                    item.setUtime(new Date());
                    if (Objects.equals(MoveType.MOVE_IN_BLACK, moveType)) {
                        //移入
                        item.setMoveInBlackTime(new Date());
                        item.setMoveOutBlackTime(null);
                    } else {
                        //移出
                        item.setMoveOutBlackTime(new Date());
                        item.setMoveInBlackTime(null);
                    }
                });
                //TODO 操作记录
                userWxExtService.updateBatchById(needToMove);
            }
        }
        return true;
    }

    @Override
    public JwtAuthenticationDto login(UserLoginDto userLoginDto) {
        String mobile = userLoginDto.getMobile();
        String openid = userLoginDto.getOpenid();
       /* if(ParamUtil.isNullOrEmptyOrZero(mobile) || ParamUtil.isNullOrEmptyOrZero(openid)){
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }*/
        logger.info("登录手机号：" + mobile);
        User user = getUserByMobile(mobile);
        User userDB = getUserByIdOrOpenidOrMobile(openid);
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.PHONE_NOT_REGISTER);
        }
       /* if (!user.getOpenid().equals(userDB.getOpenid())) {
            LeaseException.throwSystemException(LeaseExceEnums.PHONE_BIND_USER_ERROR);
        }*/
        if (!Objects.equals(mobile, user.getMobile())) {
            LeaseException.throwSystemException(LeaseExceEnums.PHONE_OR_PASSWORD_ERROR);
        }
        if (!PasswordUtil.verify(userLoginDto.getPassword(), user.getPassword())) {
            LeaseException.throwSystemException(LeaseExceEnums.PHONE_OR_PASSWORD_ERROR);
        }
        String accessToken = UUID.randomUUID().toString();
        Cache cache = cacheManager.getCache(Constants.ACCESS_TOKEN_CACHE_NAME);
        cache.put(accessToken, user);
        return new JwtAuthenticationDto(accessToken);
    }

    @Override
    public void register(UserForRegisterDto userForRegisterDto) {
        if (!Objects.equals(userForRegisterDto.getMessage(), code.get(userForRegisterDto.getMobile()))) {
            throw new SystemException(SysExceptionEnum.MESSAGE_ERROR.getCode(), SysExceptionEnum.MESSAGE_ERROR.getMessage());
        }
        if (StringUtils.isEmpty(userForRegisterDto.getWeChatUnionId())) {
            LeaseException.throwSystemException(LeaseExceEnums.WEIXIN_OPENID_IS_NULL);
        }
        User userDB = selectOne(new EntityWrapper<User>().eq("mobile", userForRegisterDto.getMobile()));
        if (!Objects.isNull(userDB)) {
            LeaseException.throwSystemException(LeaseExceEnums.PHONE_REGISTERED);
        }
        User user = getUserNoRecoverByWxOpenid(userForRegisterDto.getWeChatUnionId());
        if (Objects.isNull(user)) {
            user = new User();
            String mobile = userForRegisterDto.getMobile();
            String username = mobile.substring(mobile.length() - 5, mobile.length()).replaceAll("-", "").replaceAll("_", "") + IdGenerator.getRandomString(5);
            user.setUsername(username);
        }
        user.setMobile(userForRegisterDto.getMobile());
        user.setPassword(PasswordUtil.generate(userForRegisterDto.getPassword()));
        user.setSinaUnionid(userForRegisterDto.getSinaUnionid());
        user.setBaiduUnionid(userForRegisterDto.getBaiduUniond());
        user.setCtime(new Date());
        user.setUtime(new Date());
        user.setNickname(userForRegisterDto.getUsername());
        user.setGender(userForRegisterDto.getGender());
        insertOrUpdate(user);
        code.clear();

    }

    @Override
    public void register(UserForRegisterDto userForRegisterDto, Integer browserAgentType) {
        if (!Objects.equals(userForRegisterDto.getMessage(), code.get(userForRegisterDto.getMobile()))) {
            throw new SystemException(SysExceptionEnum.MESSAGE_ERROR.getCode(), SysExceptionEnum.MESSAGE_ERROR.getMessage());
        }
        User user = null;
        //微信端绑定
        if (browserAgentType.equals(ThirdPartyUserType.WEIXIN.getCode())) {
            user = getUserNoRecoverByWxOpenid(userForRegisterDto.getWeChatUnionId());
            //普通浏览器注册
        } else if (browserAgentType.equals(ThirdPartyUserType.NORMAL.getCode())) {
            user = new User();
            String mobile = userForRegisterDto.getMobile();
            String username = mobile.substring(mobile.length() - 5, mobile.length()).replaceAll("-", "").replaceAll("_", "") + IdGenerator.getRandomString(5);
            user.setUsername(username);
            user.setMobile(userForRegisterDto.getMobile());

        } else if (browserAgentType.equals(ThirdPartyUserType.ALIPAY.getCode())
                || browserAgentType.equals(ThirdPartyUserType.BAIDU.getCode())
                || browserAgentType.equals(ThirdPartyUserType.SINA.getCode())) {
            user = getUserByIdOrOpenidOrMobile(userForRegisterDto.getAlipayUnionid());

        }
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }

        user.setMobile(userForRegisterDto.getMobile());
        user.setPassword(PasswordUtil.generate(userForRegisterDto.getPassword()));
        user.setCtime(new Date());
        user.setUtime(new Date());
        insertOrUpdate(user);
        code.clear();
    }


    @Override
    public void messageCodeForRegister(String mobile) {
        User user = selectOne(new EntityWrapper<User>().eq("mobile", mobile));
        if (user != null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_PHONE_EXISTS);
        }

        String message = sendMessageCode(mobile);


        code.put(mobile, message);
    }

    private String sendMessageCode(String mobile) {
//        String appMessage = SysConfigUtils.get(CommonSystemConfig.class).getMessageCodeParamApp();
        String tplValue = SysConfigUtils.get(CommonSystemConfig.class).getMessageCodeTemplate();
        String apiKey = SysConfigUtils.get(CommonSystemConfig.class).getMessageApiKey();
        String templageId = SysConfigUtils.get(CommonSystemConfig.class).getMessageCodeTemplateId();

        Map<String, String> params = new HashedMap();
//        params.put("app", appMessage);

        return SmsApi.tplSendSms(apiKey, templageId, mobile, tplValue, params);
    }

    @Override
    public void messageCode(String mobile) {

        SysUser user = sysUserService.selectOne(new EntityWrapper<SysUser>().eq("mobile", mobile));
        if (ParamUtil.isNullOrEmptyOrZero(user)) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_PARAM.getCode(), SysExceptionEnum.ILLEGAL_PARAM.getMessage());
        }
        String _code = sendMessageCode(mobile);
        code.put(mobile, _code);

        user.setCode(_code);
        sysUserService.updateById(user);
    }

    @Override
    public User getUserByMobile(String mobile) {
        User user = selectOne(new EntityWrapper<User>().eq("mobile", mobile));
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        return user;
    }

    @Override
    public void updateUsername(UserUpdateDto data) {
        String mobile = data.getMobile();
        User user = getUserByMobile(mobile);
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        String oldname = user.getNickname();
        if (!ParamUtil.isNullOrEmptyOrZero(data.getNickname())) {
            user.setNickname(data.getNickname());
            if (!data.getNickname().equals(user.getNickname())) {
                User forUpdate = new User();
                forUpdate.setId(user.getId());
                forUpdate.setNickname(data.getNickname());
                updateById(forUpdate);
                NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(user, user.getId(), oldname, data.getNickname());
                CommonEventPublisherUtils.publishEvent(nameModifyEvent);
            }
        }
        if (!ParamUtil.isNullOrEmptyOrZero(data.getGender()))
            user.setGender(data.getGender());
        updateById(user);

    }

    @Override
    public UserForDetailDto detailByMobile(String mobile) {
        User user = getUserByIdOrOpenidOrMobile(mobile);
        UserForDetailDto result = getUserForDetailDto(user);
        return result;
    }

    private UserForDetailDto getUserForDetailDto(User user) {
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        UserForDetailDto result = new UserForDetailDto(user);
        result.setGenderDesc(SexType.getName(user.getGender()));
        return result;
    }

    @Override
    public User getUserByOpenid(String openid) {
        User user = userDao.findByOpenid(openid);
        return user;
    }

    @Override
    public void messageCodeForForgetPassword(String mobile) {

        User user = selectOne(new EntityWrapper<User>().eq("mobile", mobile));
        if (ParamUtil.isNullOrEmptyOrZero(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        String _code = sendMessageCode(mobile);
        code.put(mobile, _code);

        user.setCode(_code);
        updateById(user);
    }

    @Override
    public void forgetPwd(SysUserForgetPasswordDto userForgetPasswordDto) {
        String mobile = userForgetPasswordDto.getMobile();
        User user = getUserByMobile(mobile);

        if (!Objects.equals(userForgetPasswordDto.getMessage(), user.getCode())) {
//        if (!Objects.equals(userForgetPasswordDto.getMessage(), code.get(mobile))) {

            throw new SystemException(SysExceptionEnum.MESSAGE_ERROR.getCode(), SysExceptionEnum.MESSAGE_ERROR.getMessage());
        }
        user.setPassword(PasswordUtil.generate(userForgetPasswordDto.getNewPassword()));
        user.setUtime(new Date());
        user.setCode(" ");
        updateById(user);
    }

    @Override
    public void resetPwd(UserForUpdatePwdDto userForUpdatePwdDto) {
        User user = getUserByMobile(userForUpdatePwdDto.getMobile());
        if (!PasswordUtil.verify(userForUpdatePwdDto.getOldPassword(), user.getPassword())) {
            throw new SystemException(SysExceptionEnum.ILLEGAL_USER.getCode(), SysExceptionEnum.ILLEGAL_USER.getMessage());
        }
        user.setPassword(PasswordUtil.generate(userForUpdatePwdDto.getNewPassword()));
        user.setUtime(new Date());
        user.setCode(" ");
        updateById(user);
    }

    @Override
    public UserForInfoDto userInfo(String mobile) {
        User user = getUserByMobile(mobile);
        UserForInfoDto userForInfoDto = new UserForInfoDto();
        userForInfoDto.setUsername(user.getUsername());
        userForInfoDto.setAvatar(user.getAvatar());
        userForInfoDto.setGender(user.getGender());
        userForInfoDto.setMobile(user.getMobile());
        userForInfoDto.setNickname(user.getNickname());
        userForInfoDto.setHasPassword(0);
        if (!ParamUtil.isNullOrEmptyOrZero(user.getOpenid())) {
            userForInfoDto.setIsBindWeChat(1);
        }
        if (!ParamUtil.isNullOrEmptyOrZero(user.getAlipayUnionid())) {
            userForInfoDto.setIsBindAlipay(1);
        }
        if (!ParamUtil.isNullOrEmptyOrZero(user.getPassword())) {
            userForInfoDto.setHasPassword(1);
        }
        //TODO 需要查询用户钱包
        UserWallet userWallet = userWalletService.selectOne(new EntityWrapper<UserWallet>().eq("username", user.getUsername()).eq("wallet_type", WalletEnum.DEPOSIT.getCode()));
        if (userWallet == null) {
            userForInfoDto.setDeposit(null);
        } else {
            userForInfoDto.setDeposit(userWallet.getMoney());
        }

        return userForInfoDto;
    }

    @Override
    public UserForInfoDto getUserInfo(UserChargeCardOpenidDto openidDto) {
        User user = getUserByIdOrOpenidOrMobile(openidDto.getOpenid());
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        UserForInfoDto userForInfoDto = new UserForInfoDto();
        userForInfoDto.setAvatar(user.getAvatar());
        userForInfoDto.setNickname(user.getNickname());

        userForInfoDto.setCardCount(userChargeCardService.countChargeCard(user.getId()));

        return userForInfoDto;
    }

    public User getUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public boolean update(UserForInfoDto userForInfoDto) {
        User user = userDao.findByOpenid(userForInfoDto.getMobile());
        if (Objects.isNull(user)) {
            UserWxExt wxExt = userWxExtService.getByOpenid(userForInfoDto.getOpenid());
            if (Objects.isNull(wxExt)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
            user = userDao.findByOpenid(wxExt.getUserOpenid());
            if (Objects.isNull(user)) {
                LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
            }
        }
        user.setUtime(new Date());
        String oldName = user.getNickname();
        String nickname = userForInfoDto.getNickname();
        if (!ParamUtil.isNullOrEmptyOrZero(nickname) && !nickname.equals(oldName)) {
            NameModifyEvent<Integer> nameModifyEvent = new NameModifyEvent<Integer>(user, user.getId(), oldName, nickname);
            CommonEventPublisherUtils.publishEvent(nameModifyEvent);
        }
        user.setNickname(nickname);
        user.setGender(userForInfoDto.getGender());
        if (StringUtils.isNotBlank(userForInfoDto.getProvince())) {
            user.setProvince(userForInfoDto.getProvince());
        }
        if (StringUtils.isNotBlank(userForInfoDto.getCity())) {
            user.setCity(userForInfoDto.getCity());
        }
        if (Objects.equals(userForInfoDto.getIsBindWeChat(), 0)) {
            userWxExtService.delete(new EntityWrapper<UserWxExt>().eq("openid", user.getOpenid()));//删除微信用户扩展表信息
            user.setOpenid(" ");
        }
        if (Objects.equals(userForInfoDto.getIsBindAlipay(), 0)) {
            user.setAlipayUnionid(" ");
        }

        return updateById(user);
    }

    @Override
    public void bindMobile(UserForRegisterDto userForRegisterDto) {
        String mobile = userForRegisterDto.getMobile();
        User mobileUser = selectOne(new EntityWrapper<User>().eq("mobile", mobile));
        if (!Objects.equals(userForRegisterDto.getMessage(), code.get(userForRegisterDto.getMobile()))) {
            throw new SystemException(SysExceptionEnum.MESSAGE_ERROR.getCode(), SysExceptionEnum.MESSAGE_ERROR.getMessage());
        }
        //绑定操作,第三方用户肯定不为空
        User thirdUser = null;
        if (!ParamUtil.isNullOrEmptyOrZero(userForRegisterDto.getWeChatUnionId())) {
            thirdUser = userDao.findByOpenid(userForRegisterDto.getWeChatUnionId());
            UserWxExt wxExt = userWxExtService.getByOpenid(userForRegisterDto.getWeChatUnionId());
            if (Objects.isNull(wxExt)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
            thirdUser = userDao.findByOpenid(wxExt.getUserOpenid());
            if (Objects.isNull(thirdUser)) {
                LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
            }
        } else if (!ParamUtil.isNullOrEmptyOrZero(userForRegisterDto.getAlipayUnionid())) {
            thirdUser = userDao.findByOpenid(userForRegisterDto.getAlipayUnionid());
        } else if (!ParamUtil.isNullOrEmptyOrZero(userForRegisterDto.getBaiduUniond())) {
            thirdUser = userDao.findByOpenid(userForRegisterDto.getBaiduUniond());
        } else if (!ParamUtil.isNullOrEmptyOrZero(userForRegisterDto.getSinaUnionid())) {
            thirdUser = userDao.findByOpenid(userForRegisterDto.getSinaUnionid());
        }

        //手机用户和第三方用户是不同的用户
        if (mobileUser != null && thirdUser != null && thirdUser.getId() != mobileUser.getId()) {//第三方用户和手机用户不是同一个用户,需要进行用户合并,将手机用户的信息合并到第三放用户
            mobileUser.setOpenid(thirdUser.getOpenid());
            mobileUser.setAddress(thirdUser.getAddress());
            mobileUser.setAlipayUnionid(thirdUser.getAlipayUnionid());
            mobileUser.setAvatar(thirdUser.getAvatar());
            mobileUser.setBaiduUnionid(thirdUser.getBaiduUnionid());
            mobileUser.setSinaUnionid(thirdUser.getSinaUnionid());
            mobileUser.setWxId(thirdUser.getWxId());
            mobileUser.setThirdParty(thirdUser.getThirdParty());
            mobileUser.setSysUserId(thirdUser.getSysUserId());
            mobileUser.setProvince(thirdUser.getProvince());
            mobileUser.setGender(thirdUser.getGender());
            mobileUser.setNickname(thirdUser.getNickname());

            deleteById(thirdUser.getId());
            mobileUser.setUtime(new Date());
            updateById(mobileUser);

            //手机用户不存在
        } else if (mobileUser == null) {
            thirdUser.setMobile(mobile);
            thirdUser.setUtime(new Date());
            updateById(thirdUser);
        }


    }

    /**
     * 修改用户手机号
     */
    public boolean updateUserMobile(UserForUpdateMobileDto mobileDto) {
        if (StringUtils.isBlank(mobileDto.getOpenid())) {
            LeaseException.throwSystemException(LeaseExceEnums.PARAMS_ERROR);
        }
        UserWxExt wxExt = userWxExtService.getByOpenid(mobileDto.getOpenid());
        if (Objects.isNull(wxExt)) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        User user = userDao.findByOpenid(wxExt.getUserOpenid());
        if (Objects.isNull(user)) {
            LeaseException.throwSystemException(LeaseExceEnums.ENTITY_NOT_EXISTS);
        }
        if (!Objects.equals(mobileDto.getNewCode(), code.get(mobileDto.getNewMobile()))) {
            throw new SystemException(SysExceptionEnum.MESSAGE_ERROR.getCode(), SysExceptionEnum.MESSAGE_ERROR.getMessage());
        }
        user.setMobile(mobileDto.getNewMobile());
        user.setUtime(new Date());
        return updateById(user);
    }

    @Override
    public UserForDetailDto getUserDetail(String mobile) {
        User user = getUserByIdOrOpenidOrMobile(mobile);
        if (user == null) {
            LeaseException.throwSystemException(LeaseExceEnums.USER_DONT_EXISTS);
        }
        UserForDetailDto result = new UserForDetailDto(user);
        result.setGenderDesc(SexType.getName(user.getGender()));
        Integer userId = user.getId();
        UserWallet userWallet = userWalletService.selectUserWallet(userId, WalletEnum.BALENCE.getCode());
        UserWallet userWallet1 = userWalletService.selectUserWallet(userId, WalletEnum.DISCOUNT.getCode());
        Double money = userWallet.getMoney() + userWallet1.getMoney();
        BigDecimal b = new BigDecimal(money);
        double balance = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        result.setBalance(balance);
        return result;
    }
}
