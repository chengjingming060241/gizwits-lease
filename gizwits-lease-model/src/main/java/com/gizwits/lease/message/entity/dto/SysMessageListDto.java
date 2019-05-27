package com.gizwits.lease.message.entity.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统消息展示dto
 * Created by yinhui on 2017/8/8.
 */
public class SysMessageListDto implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 收件人
     */
    private String recepit;
    /**
     *发件人
     */
    private String sentPerson;
    /**
     * 收件时间或发件时间
     */
    private Date time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRecepit() {
        return recepit;
    }

    public void setRecepit(String recepit) {
        this.recepit = recepit;
    }

    public String getSentPerson() {
        return sentPerson;
    }

    public void setSentPerson(String sentPerson) {
        this.sentPerson = sentPerson;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
