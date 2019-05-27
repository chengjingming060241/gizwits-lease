package com.gizwits.lease.intercepter;

import com.gizwits.boot.base.ResponseObject;
import com.gizwits.boot.dto.MenuDto;
import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.boot.sys.service.SysUserService;
import com.gizwits.boot.utils.SysConfigUtils;
import com.gizwits.lease.config.CommonSystemConfig;
import com.gizwits.lease.constant.BooleanEnum;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.OperatorService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Created by zhl on 2017/9/7.
 */
//@Component
//@Aspect
public class LeaseAuthIntercepter {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger("USER_LOGGER");

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OperatorService operatorService;

    private static Set<String> noAllotMenuNames = new HashSet<>();

    @Pointcut("execution(* com.gizwits.boot.sys.web.SysUserController.menu(..))")
    public void aspectSysUserMenuService(){}

    @Around(value = "aspectSysUserMenuService()")
    public Object doAfter(ProceedingJoinPoint joinPoint) throws Throwable{
        String methodName = joinPoint.getSignature().getName();
        Object returnValue = joinPoint.proceed();
        SysUser sysUser = sysUserService.getCurrentUser();
        logger.info("=====用户{}请求菜单===={}",sysUser.getId(),methodName);
        if(Objects.nonNull(returnValue)){
            Operator operator = operatorService.getOperatorByAccountId(sysUser.getId());
            if(Objects.nonNull(operator)){
                logger.info("====当前用户{}是运营商{}=====",sysUser.getId(),operator.getId());
                if(operator.getIsAllot().equals(BooleanEnum.FALSE.getCode())){//运营商未开放投放点
                    ResponseObject<List<MenuDto>> menuReponseObject = (ResponseObject<List<MenuDto>>)returnValue;
                    handleOperatorAllot(menuReponseObject);
                }
            }
        }
        return returnValue;
    }

    private Object handleOperatorAllot(ResponseObject<List<MenuDto>> menuReponseObject){
        if(menuReponseObject.getCode().equals("200")
                && CollectionUtils.isNotEmpty(menuReponseObject.getData())){
            List<MenuDto> allMenus = menuReponseObject.getData();
            List<MenuDto> resultMenus = new ArrayList<>();
            loadOperatorAllotNamesConfig();
            logger.info("=====当前是运营商用户,而且未开放投放点功能,以下菜单将不展示给用户:{}===", ArrayUtils.toString(noAllotMenuNames.toArray()));
            for(MenuDto menuDto:allMenus){
                if(!noAllotMenuNames.contains(menuDto.getName())){
                    resultMenus.add(menuDto);
                }
            }
            menuReponseObject.setData(resultMenus);
        }
        return menuReponseObject;
    }

    private void loadOperatorAllotNamesConfig(){
        String names = SysConfigUtils.get(CommonSystemConfig.class).getMahjongOperatorAllotNames();
        if(StringUtils.isBlank(names)){
            noAllotMenuNames.add("投放点管理");
            noAllotMenuNames.add("运营商管理");
            noAllotMenuNames.add("运营商列表");
        }else{
            String[] nameArr = names.split(",");
            for(String name:nameArr){
                noAllotMenuNames.add(name);
            }
        }
    }
}
