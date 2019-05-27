package com.gizwits.lease.user.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gizwits.boot.annotation.Query;
import com.gizwits.boot.base.Constants;

/**
 * Dto - 用户查询
 *
 * @author lilh
 * @date 2017/8/2 16:37
 */
public class UserForQueryDto {

    @Query(field = "u.nickname", operator = Query.Operator.like)
    private String nickname;

    @Query(field = "u.gender")
    private Integer gender;

    /**
     * 当bindedMobile等于1时，则为 mobile is not null,反之(由mutex控制)，则为mobile is null
     */
    @Query(field = "u.mobile", operator = Query.Operator.isNotNull, condition = "1", mutex = true)
    private Integer hasMobile;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "u.ctime", operator = Query.Operator.ge)
    private Date startTime;

    @JsonFormat(pattern = Constants.DEFAULT_DATE_PATTERN, timezone = "GMT+8")
    @Query(field = "u.ctime", operator = Query.Operator.le)
    private Date endTime;

    @Query(field = "u.status")
    private Integer status = 1;

    @JsonIgnore
    @Query(field = "u.id", operator = Query.Operator.in)
    private List<Integer> ids;

    @JsonIgnore
    @Query(field = "u.sys_user_id", operator = Query.Operator.in)
    private List<Integer> accessableUserIds;

    @Query(field = "u.user_name", operator = Query.Operator.like)
    private String userName;

    @JsonIgnore
    private int size;

    @JsonIgnore
    private int current;

    @JsonIgnore
    private int begin;

    @JsonIgnore
    private int end;

    @JsonIgnore
    private String wxId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private List<Integer> sysUserIds;

    public int getBegin() {
        return begin;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public List<Integer> getSysUserIds() {
        return sysUserIds;
    }

    public void setSysUserIds(List<Integer> sysUserIds) {
        this.sysUserIds = sysUserIds;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getHasMobile() {
        return hasMobile;
    }

    public void setHasMobile(Integer hasMobile) {
        this.hasMobile = hasMobile;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
            this.status = status;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getAccessableUserIds() {
        return accessableUserIds;
    }

    public void setAccessableUserIds(List<Integer> accessableUserIds) {
        this.accessableUserIds = accessableUserIds;
    }

    public String getWxId() { return wxId; }

    public void setWxId(String wxId) { this.wxId = wxId; }
}
