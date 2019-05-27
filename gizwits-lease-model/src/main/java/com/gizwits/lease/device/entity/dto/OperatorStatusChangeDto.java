package com.gizwits.lease.device.entity.dto;

import com.gizwits.boot.sys.entity.SysUser;
import com.gizwits.lease.manager.entity.Agent;
import com.gizwits.lease.manager.entity.Operator;

/**
 * @author lilh
 * @date 2017/8/3 9:36
 */
public class OperatorStatusChangeDto {

    /** 运营商id */
    private Integer operatorId;

    /** 运营商名称 */
    private String operatorName;

    /** 运营商绑定的系统账号 */
    private Integer sysAccountId;

    /** 原状态 */
    private Integer from;

    /** 修改后的状态 */
    private Integer to;

    /** 当前操作人 */
    private SysUser current;

    public OperatorStatusChangeDto(Operator operator) {
        this.operatorId = operator.getId();
        this.operatorName = operator.getName();
        this.sysAccountId = operator.getSysAccountId();
        this.from = operator.getStatus();
    }

    public OperatorStatusChangeDto(Agent agent) {
        this.operatorId = agent.getId();
        this.operatorName = agent.getName();
        this.sysAccountId = agent.getSysAccountId();
        this.from = agent.getStatus();
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getTo() {
        return to;
    }

    public void setTo(Integer to) {
        this.to = to;
    }

    public SysUser getCurrent() {
        return current;
    }

    public void setCurrent(SysUser current) {
        this.current = current;
    }
}
