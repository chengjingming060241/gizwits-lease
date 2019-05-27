package com.gizwits.lease.message.entity.dto;

import java.io.Serializable;

/**
 * Created by yinhui on 2017/7/31.
 */
public class FeedbackBusinessDto implements Serializable {
    private String content;
    private Integer pictureNum;
    private String pictureUrl;
    private Integer recipientId;
    private String recipientName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPictureNum() {
        return pictureNum;
    }

    public void setPictureNum(Integer pictureNum) {
        this.pictureNum = pictureNum;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Integer recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}
