package com.gizwits.lease.listener;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gizwits.lease.device.entity.dto.DeviceAssignRecordDto;
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.event.DeviceAssignEvent;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;
import com.gizwits.lease.manager.service.AgentService;
import com.gizwits.lease.manager.service.OperatorService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Listener - 更新运营商或代理商状态
 *
 * @author lilh
 * @date 2017/8/25 11:02
 */
@Component
public class UpdateAgentOrOperatorStatusAfterAssignListener implements ApplicationListener<DeviceAssignEvent> {

    @Autowired
    private AgentService agentService;

    @Autowired
    private OperatorService operatorService;

    @Async
    @Override
    public void onApplicationEvent(DeviceAssignEvent event) {
        List<DeviceAssignRecordDto> records = event.getRecords();
        if (CollectionUtils.isNotEmpty(records) && Objects.nonNull(records.get(0)) && Objects.equals(event.getType(), DeviceAssignEvent.Type.ASSIGN)) {
            DeviceAssignRecordDto record = records.get(0);
            Integer assignedId = record.getDestinationOperator();
            updateOperator(assignedId, record.getIsOperator());
            updateAgent(assignedId, record.getIsAgent());
        }
    }

    private void updateAgent(Integer assignedId, Boolean isAgent) {
        if (Objects.nonNull(isAgent) && isAgent) {
            //代理商
            Agent agent = agentService.selectOne(new EntityWrapper<Agent>().eq("sys_account_id", assignedId));
            if (Objects.nonNull(agent) && Objects.equals(StatusType.NEED_TO_ASSIGN.getCode(), agent.getStatus())) {
                agent.setStatus(StatusType.OPERATING.getCode());
                agent.setUtime(new Date());
                agentService.updateById(agent);
            }
        }
    }

    private void updateOperator(Integer assignedId, Boolean isOperator) {
        if (Objects.nonNull(isOperator) && isOperator) {
            //运营商
            Operator operator = operatorService.selectOne(new EntityWrapper<Operator>().eq("sys_account_id", assignedId));
            if (Objects.nonNull(operator) && Objects.equals(StatusType.NEED_TO_ASSIGN.getCode(), operator.getStatus())) {
                operator.setStatus(StatusType.OPERATING.getCode());
                operator.setUtime(new Date());
                operatorService.updateById(operator);
            }
        }
    }
}
