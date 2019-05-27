package com.gizwits.lease.manager.dto;

import com.gizwits.boot.dto.SysUserForBasicDto;
import com.gizwits.lease.enums.StatusType;
import com.gizwits.lease.manager.entity.Operator;
import org.springframework.beans.BeanUtils;

/**
 * Dto - 运营详情
 *
 * @author lilh
 * @date 2017/8/1 12:10
 */
public class OperatorForDetailDto {

    //运营商信息

    /** id */
    private Integer id;

    /** 运营商名称 */
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

    /** 投放点数量 */
    private Integer launchAreaCount;

    /** 设备数量 */
    private Integer deviceCount;

    /** 绑定的系统账号 */
    private Integer sysAccountId;

    /** 创建人 */
    private String sysUserName;


    private String logoUrl;

    private String phone;

    private String description;

    private String webSite;


    /** 运营商扩展信息 **/
    private OperatorExtDto ext;

    //系统帐号信息

    private SysUserForBasicDto account;

    private Integer coverLevel;

    private Integer isAllot;

    public Integer getIsAllot() {
        return isAllot;
    }

    public void setIsAllot(Integer isAllot) {
        this.isAllot = isAllot;
    }

    public Integer getCoverLevel() {
        return coverLevel;
    }

    public void setCoverLevel(Integer coverLevel) {
        this.coverLevel = coverLevel;
    }

    public OperatorForDetailDto() {}

    public OperatorForDetailDto(Operator operator) {
        BeanUtils.copyProperties(operator, this);
        this.statusDesc = StatusType.getDesc(operator.getStatus());
    }

    public OperatorExtDto getExt() {
        return ext;
    }

    public void setExt(OperatorExtDto ext) {
        this.ext = ext;
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

    public Integer getLaunchAreaCount() {
        return launchAreaCount;
    }

    public void setLaunchAreaCount(Integer launchAreaCount) {
        this.launchAreaCount = launchAreaCount;
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
