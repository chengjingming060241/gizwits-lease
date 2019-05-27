package com.gizwits.lease.device.web;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.gizwits.boot.base.BaseController;
import com.gizwits.boot.base.Constants;
import com.gizwits.boot.base.RequestObject;
import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.Pageable;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysRoleService;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.lease.device.entity.DeviceLaunchArea;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaAssociatedOperatorDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaListDto;
import com.gizwits.lease.device.entity.dto.DeviceLaunchAreaQueryDto;
import com.gizwits.lease.device.entity.dto.QueryForCreatorDto;
import com.gizwits.lease.device.service.DeviceLaunchAreaService;

import com.gizwits.lease.wallet.entity.UserWalletChargeOrder;
import com.gizwits.lease.wallet.service.UserWalletChargeOrderService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import javax.validation.Valid;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * 设备投放点表 前端控制器
 * </p>
 *
 * @author yinhui
 * @since 2017-07-12
 */
@RestController
@EnableSwagger2
@Api(description = "设备投放点接口")
@RequestMapping("/device/deviceLaunchArea")
public class DeviceLaunchAreaController extends BaseController{
    protected final static Logger logger = LoggerFactory.getLogger("DEVICE_LOGGER");

    @Autowired
    private DeviceLaunchAreaService deviceLaunchAreaService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private UserWalletChargeOrderService userWalletChargeOrderService;

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "分页列表", notes = " 分页列表", consumes = "application/json")
    @PostMapping(value = "/list")
    public ResponseObject<Page<DeviceLaunchAreaListDto>> list(@RequestBody @Valid RequestObject<Pageable<DeviceLaunchAreaQueryDto>> requestObject) {
        Pageable<DeviceLaunchAreaQueryDto> pageable = requestObject.getData();
        SysUser current = sysUserService.getCurrentUser();
        if (Objects.isNull(pageable.getQuery())) {
            pageable.setQuery(new DeviceLaunchAreaQueryDto());
        }
        if (Objects.isNull(pageable.getQuery().getSelfOperating())) {
            pageable.getQuery().setSelfOperating(true);//若不设置，则只查询当前用户创建的投放点
        }
        if (pageable.getQuery().getSelfOperating()) {
            pageable.getQuery().setAccessableUserIds(Collections.singletonList(current.getId()));
        } else {
            //若没有子级用户,则直接返回
            List<Integer> userIds = sysUserService.resolveAccessableUserIds(current, false, true);
            userIds = userIds.stream().filter(item -> !Objects.equals(item, current.getId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userIds)) {
                return success(new Page<>());
            }
            pageable.getQuery().setAccessableUserIds(userIds);
        }
        logger.info("sys_user_id=" + pageable.getQuery().getAccessableUserIds() + ",查询条件：投放点名称:" + requestObject.getData());
        Page<DeviceLaunchAreaListDto> result = deviceLaunchAreaService.getDeviceLaunchAreaListPage(pageable);
        return success(result);
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "运营商或代理商直接创建的投放点", consumes = "application/json")
    @PostMapping({"/children", "/listByOperator"})
    public ResponseObject<Page<DeviceLaunchAreaListDto>> children(@RequestBody RequestObject<Pageable<QueryForCreatorDto>> requestObject) {
        Pageable<QueryForCreatorDto> data = requestObject.getData();
        Pageable<DeviceLaunchAreaQueryDto> pageable = new Pageable<>();
        BeanUtils.copyProperties(data, pageable, "query");
        pageable.setQuery(new DeviceLaunchAreaQueryDto());
        if (Objects.isNull(data.getQuery()) || Objects.isNull(data.getQuery().getCreatorId())) {
            throw new IllegalArgumentException("参数错误");
        }
        pageable.getQuery().setAccessableUserIds(Collections.singletonList(data.getQuery().getCreatorId()));
        return success(deviceLaunchAreaService.getDeviceLaunchAreaListPage(pageable));
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = " 添加",notes = " 添加",consumes = "application/json")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject add(@RequestBody @Valid RequestObject<DeviceLaunchArea> requestObject){
        SysUser sysUser = sysUserService.getCurrentUser();
        DeviceLaunchArea deviceLaunchArea = requestObject.getData();
        deviceLaunchArea.setSysUserId(sysUser.getId());
        deviceLaunchArea.setOwnerId(sysUser.getId());
        deviceLaunchArea.setSysUserName(sysUser.getUsername());
        logger.info("添加deviceLaunchArea："+deviceLaunchArea.toString());
        deviceLaunchAreaService.addDeviceLaunchArea(deviceLaunchArea);
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = " 维护人员",notes = " 维护人员",consumes = "application/json")
    @RequestMapping(value = "/maintainer",method = RequestMethod.POST)
    public @ResponseBody ResponseObject maintainer(){
        logger.info("维护人员列表");
        return success(deviceLaunchAreaService.getDeviceMaintainerDtos());
    }


    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "更新",notes = " 更新",consumes = "application/json")
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject update(@RequestBody @Valid RequestObject<DeviceLaunchArea> requestObject){
        DeviceLaunchArea deviceLaunchArea = requestObject.getData();
        logger.info("更新deviceLaunchArea,id="+deviceLaunchArea.getId());
        deviceLaunchAreaService.updateDeviceLaunchArea(deviceLaunchArea);
        return success();
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "删除",notes = " 删除",consumes = "application/json")
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<String> delete(@RequestBody @Valid RequestObject<List<Integer>> requestObject){
        logger.info("投放点的id列表，批量删除"+requestObject.getData());
        List<Integer> areaIds = requestObject.getData();
        return success(deviceLaunchAreaService.deleteDeviceLaunchAreas(areaIds));
    }

    @ApiOperation(value = " 投放点名称是否存在",notes = "投放点名称是否存在",consumes = "application/json")
    @RequestMapping(value = "/isExist",method = RequestMethod.POST)
    public ResponseObject isExist(@RequestBody @Valid RequestObject<String> requestObject){
        logger.info(" 投放点名称："+requestObject.getData());
        return success(deviceLaunchAreaService.deviceLaunchAreaIsExist(requestObject.getData()));
    }

    @ApiImplicitParam(paramType = "header",name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "关联运营商",notes = "关联运营商",consumes = "application/json")
    @RequestMapping(value = "/associatedOperator",method = RequestMethod.POST)
    public @ResponseBody
    ResponseObject<List<Integer>> associatedOperator(@RequestBody @Valid RequestObject<DeviceLaunchAreaAssociatedOperatorDto> requestObject){
        DeviceLaunchAreaAssociatedOperatorDto deviceLaunchAreaAssociatedOperatorDto = requestObject.getData();
        List<Integer> ids = deviceLaunchAreaService.associatedOperator(deviceLaunchAreaAssociatedOperatorDto);
        logger.info("关联运营商信息，投放点Id："+deviceLaunchAreaAssociatedOperatorDto.getDeviceLaunchAreaIds()+",运营商信息id："+
        deviceLaunchAreaAssociatedOperatorDto.getOperatorAccountId()+",名称："+deviceLaunchAreaAssociatedOperatorDto.getOperatorName());
        return  success(ids);
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值记录", notes = "并联在该投放点的充值记录", consumes = "application/json")
    @PostMapping(value = "/chargeList")
    public ResponseObject<Page<UserWalletChargeOrder>> chargeList(@RequestBody @Valid RequestObject<Pageable<Integer>> requestObject) {

        Page<UserWalletChargeOrder> pageable = new Page<>();
        BeanUtils.copyProperties(requestObject.getData(), pageable);

        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(requestObject.getData());

        Wrapper<UserWalletChargeOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("launch_area_name", deviceLaunchArea.getName());

        return success(userWalletChargeOrderService.selectPage(pageable, wrapper));
    }

    @ApiImplicitParam(paramType = "header", name = Constants.TOKEN_HEADER_NAME)
    @ApiOperation(value = "充值记录汇总金额及订单数量", notes = "搜索结果充值记录汇总金额及订单数量")
    @PostMapping(value = "/sumCharge")
    public ResponseObject<Map<String, Double>> sumCharge(@RequestBody @Valid RequestObject<Integer> requestObject) {
        Double money = 0.0;
        Integer sum = 0;

        DeviceLaunchArea deviceLaunchArea = deviceLaunchAreaService.selectById(requestObject.getData());

        Wrapper<UserWalletChargeOrder> wrapper = new EntityWrapper<>();
        wrapper.eq("launch_area_name", deviceLaunchArea.getName());

        List<UserWalletChargeOrder> list = userWalletChargeOrderService.selectList(wrapper);
        for (UserWalletChargeOrder item:list) {
            if (item.getStatus() == 5) {
                money += item.getFee();
                sum += 1;
            }
        }

        Map<String, Double> map = new HashMap<>(3);
        map.put("money", money);
        map.put("sum", sum.doubleValue());

        return success(map);
    }
}
