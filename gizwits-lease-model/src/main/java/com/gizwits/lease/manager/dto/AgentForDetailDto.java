package com.gizwits.lease.manager.dto;

import com.gizwits.boot.dto.SysUserForBasicDto;
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.manager.entity.Agent;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 代理商详情
 *
 * @author lilh
 * @date 2017/8/1 12:10
 */
public class AgentForDetailDto {


    /** id */
    private Integer id;

    /** 代理商名称 */
    private String name;

    /** 状态 */
    private Integer status;

    /** 状态描述 */
    private String statusDesc;

    /** 省 */
    private String province;

    /** 市 */
    private String city;

    /** 区 */
    private String area;

    /** 详细地址 */
    private String address;

    /** 联系人 */
    private String contact;

    /** 联系人手机号 */
    private String mobile;

    /** 运营商数量 */
    private Integer operatorCount;

    /** 设备数量 */
    private Integer deviceCount;

    /** 绑定的系统账号 */
    private Integer sysAccountId;

    /** 创建人 */
    private String sysUserName;

    private Integer coverLevel;

    public Integer getCoverLevel() {
        return coverLevel;
    }

    public void setCoverLevel(Integer coverLevel) {
        this.coverLevel = coverLevel;
    }

//系统帐号信息

    private SysUserForBasicDto account;

    public AgentForDetailDto(Agent agent) {
        BeanUtils.copyProperties(agent, this);
        this.statusDesc = StatusType.getDesc(agent.getStatus());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getOperatorCount() {
        return operatorCount;
    }

    public void setOperatorCount(Integer operatorCount) {
        this.operatorCount = operatorCount;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Integer getSysAccountId() {
        return sysAccountId;
    }

    public void setSysAccountId(Integer sysAccountId) {
        this.sysAccountId = sysAccountId;
    }

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public SysUserForBasicDto getAccount() {
        return account;
    }

    public void setAccount(SysUserForBasicDto account) {
        this.account = account;
    }
}
