package com.gizwits.lease.message.entity.dto;

import java.io.Serializable;

/**
 * 意见反馈信息dto
 * Created by yinhui on 2017/7/31.
 */
public class FeedbackUserDto implements Serializable{
    private String content;
    private Integer pictureNum;
    private String pictureUrl;
    private String openId;
    private Integer origin;

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}

    public Integer getPictureNum() {return pictureNum;}

    public void setPictureNum(Integer pictureNum) {this.pictureNum = pictureNum;}

    public String getPictureUrl() {return pictureUrl;}

    public void setPictureUrl(String pictureUrl) {this.pictureUrl = pictureUrl;}

    public String getOpenId() {return openId;}

    public void setOpenId(String openId) {this.openId = openId;}

    public Integer getOrigin() {return origin;}

    public void setOrigin(Integer origin) {this.origin = origin;}
}
