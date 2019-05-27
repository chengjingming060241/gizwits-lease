package com.gizwits.lease.message.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.annotation.Query;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题反馈查询dto
 * Created by yinhui on 2017/7/26.
 */
public class FeedbackQueryDto implements Serializable{

    @Query(field = "user_name",operator = Query.Operator.like)
    private String userName;

    @Query(field = "nick_name",operator = Query.Operator.like)
    private String nickName;

    @Query(field = "content",operator = Query.Operator.like)
    private String content;

    @Query(field = "ctime",operator = Query.Operator.ge)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date begin;

    @Query(field = "ctime",operator = Query.Operator.le)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;

    @Query(field = "origin")
    private Integer origin;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Integer getOrigin() {return origin;}

    public void setOrigin(Integer origin) {this.origin = origin;}
}
