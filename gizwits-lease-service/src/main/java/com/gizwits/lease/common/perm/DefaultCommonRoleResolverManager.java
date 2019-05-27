package com.gizwits.lease.common.perm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.gizwits.lease.enums.CommonRole;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manager - 管理
 *
 * @author lilh
 * @date 2017/7/8 14:53
 */
@Component
public class DefaultCommonRoleResolverManager implements InitializingBean {

    @Autowired
    private List<AbstractCommonRoleResolver> resolvers = new ArrayList<>();

    @Autowired
    private List<AbstractDeviceLaunchAreaCommonRoleResolver> launchResolvers = new ArrayList<>();

    private Map<CommonRole, CommonRoleResolver> commonRoleResolverMap = new HashMap<>();

    private Map<CommonRole, CommonRoleResolver> launchResolverMap;

    public CommonRoleResolver getCommonRoleResolver(CommonRole commonRole) {
        return commonRoleResolverMap.get(commonRole);
    }

    public CommonRoleResolver getLaunchResolver(CommonRole commonRole) {
        return launchResolverMap.get(commonRole);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        commonRoleResolverMap = resolvers.stream().collect(Collectors.toMap(CommonRoleResolver::getCommonRole, item -> item));
        launchResolverMap = launchResolvers.stream().collect(Collectors.toMap(CommonRoleResolver::getCommonRole, item -> item));
    }
}
