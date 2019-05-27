package com.gizwits.lease.manager.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.dto.SysUserForPullDto;
import com.gizwits.boot.sys.entity.SysUserExt;
import com.gizwits.lease.manager.dto.*;
import com.gizwits.lease.manager.entity.Operator;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 运营商表 服务类
 * </p>
 *
 * @author rongmc
 * @since 2017-06-20
 */
public interface OperatorService extends IService<Operator> {

    /**
     * 根据运营商的系统帐号查询微信配置信息
     *
     * @param operatorAccount 运营商系统账号
     * @return wx配置信息
     */
    SysUserExt getWxConfigByOperator(Integer operatorAccount);

    /**
     * 添加运营商
     *
     * @param dto 运营商信息
     * @return 是否成功
     */
    boolean add(OperatorForAddDto dto);


    /**
     * 待绑定的系统账号
     *
     * @return 账号列表
     */
    List<SysUserForPullDto> bindingExistedSysUserList();

    /**
     * 分页列表
     *
     * @param pageable 分页信息
     * @return 分页数据
     */
    Page<OperatorForListDto> page(Pageable<OperatorForQueryDto> pageable);


    /**
     * 详情
     */
    OperatorForDetailDto detail(Integer id);

    /**
     * 更新
     *
     * @param dto 更新信息
     * @return 是否成功
     */
    OperatorForDetailDto update(OperatorForUpdateDto dto);


    /**
     * 状态切换
     *
     * @param id id
     * @return 是否成功
     */
    boolean change(Integer id);

    /**
     * 获取直接创建的运营商列表
     *
     * @param creatorId 创建人
     * @return 运营商列表
     */
    List<Operator> getDirectOperatorByCreator(Integer creatorId);


    Page<Operator> getAssignableOperator(Pageable<Integer> pageable);

    /**
     * 获取直接父级运营商
     *
     * @param sysAccountId 子级运营商
     * @return 父级运营商或者为null
     */
    Operator getParentOperator(Integer sysAccountId);

    /**
     * 通过AccountId查询运营商
     * @param sysAccountId
     * @return
     */
    Operator getOperatorByAccountId(Integer sysAccountId);



    /**
     * 获取绑定的运营商帐号
     */
    List<Integer> resolveBindAccount();

    /**
     * 上传头像或logo
     * @param file
     * @return
     */
    String uploadAvatar(MultipartFile file);

    /**
     * 子级运营商的设备统计
     * @param pageable
     * @return
     */
    Page<MJSonOperatorDto> getMJOperator(Pageable<Integer> pageable);

    /**
     * 代理商的设备统计
     * @param pageable
     * @return
     */
    Page<MJSonOperatorDto> getMJAgent(Pageable<Integer> pageable);

    /**
     * 当前用户的设备统计
     * @return
     */
    MJSonOperatorDto getOperatorDeviceCount();

    MJSonOperatorDto getOperatorUnAllotDeviceCount();

    /**
     * 是否拥有分配权限
     * @return
     */
    boolean isAllot();

    Integer getRole();

    String delete(List<Integer> ids);

    /**
     * 获取所有父级运营商
     * @return
     */
    List<OperatorForCascaderDto> getAllParentOperator();

    /**
     * 根据父运营商获取下面子运营商
     * @param sysUserId
     * @return
     */
    List<OperatorForCascaderDto> getAllChildOperatorById(Integer sysUserId);
}
