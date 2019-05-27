package com.gizwits.lease.message.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gizwits.boot.annotation.Query;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 系统消息查询dto
 * Created by yinhui on 2017/7/26.
 */
public class SysMessageQueryDto implements Serializable{
    @Query(field = "title",operator = Query.Operator.like)
    private String title;
    @Query(field = "content",operator = Query.Operator.like)
    private String content;
    @Query(field = "addresser_name",operator = Query.Operator.like)
    private String addresserName;

    @Query(field = "ctime",operator = Query.Operator.ge)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date begin;

    @Query(field = "ctime",operator = Query.Operator.le)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date end;

    @NotNull
    private Integer type;

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public String getAddresserName() {return addresserName;}

    public void setAddresserName(String addresserName) {this.addresserName = addresserName;}

    public Date getBegin() {return begin;}

    public void setBegin(Date begin) {this.begin = begin;}

    public Date getEnd() {return end;}

    public void setEnd(Date end) {this.end = end;}

    public Integer getType() {return type;}

    public void setType(Integer type) {this.type = type;}
}
