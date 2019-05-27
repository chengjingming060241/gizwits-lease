package com.gizwits.lease.common.perm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.gizwits.lease.common.perm.dto.AssignDestinationDto;
import com.gizwits.lease.enums.AssignDestinationType;
import com.gizwits.lease.enums.CommonRole;
import org.springframework.stereotype.Component;

/**
 * Resolver - 厂商角色
 *
 * @author lilh
 * @date 2017/7/8 14:27
 */
@Component
public class ManufacturerCommonRoleResolver extends AbstractCommonRoleResolver {

    @Override
    public CommonRole getCommonRole() {
        return CommonRole.MANUFACTURER;
    }

    @Override
    public List<AssignDestinationDto> resolveAssignDest() {
        //运营商和代理商
        return Stream.of(AssignDestinationType.OPERATOR,AssignDestinationType.AGENT).map(AssignDestinationDto::new).collect(Collectors.toList());
    }
}
