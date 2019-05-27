package com.gizwits.lease.message.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 业务系统表
 * </p>
 *
 * @author yinhui
 * @since 2017-07-26
 */
@TableName("feedback_business")
public class FeedbackBusiness extends Model<FeedbackBusiness> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增长
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;

    /**
     * 用户账号
     */
	@TableField("username")
	private String userName;
	/**
	 * 发件人名称
	 */
	@TableField("nick_name")
	private String nickName;
    /**
     * 收件人手机号
     */
	private String mobile;
	/**
	 * 头像地址
	 */
	private String avatar;
    /**
     * 内容
     */
	private String content;
    /**
     * 图片地址
     */
	@TableField("picture_url")
	private String pictureUrl;
    /**
     * 图片数
     */
	@TableField("picture_num")
	private Integer pictureNum;
    /**
     * 发件人ID
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 收件人ID
     */
	@TableField("recipient_id")
	private Integer recipientId;
    /**
     * 收件人名称
     */
	@TableField("recipient_name")
	private String recipientName;
    /**
     * 是否已读：0 未读，1已读
     */
	@TableField("is_read")
	private Integer isRead;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getUtime() {
		return utime;
	}

	public void setUtime(Date utime) {
		this.utime = utime;
	}

	public String getUserName() {return userName;}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {return nickName;}

	public void setNickName(String nickName) {this.nickName = nickName;}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAvatar() {return avatar;}

	public void setAvatar(String avatar) {this.avatar = avatar;}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public Integer getPictureNum() {
		return pictureNum;
	}

	public void setPictureNum(Integer pictureNum) {
		this.pictureNum = pictureNum;
	}

	public Integer getUserId() {return userId;}

	public void setUserId(Integer userId) {this.userId = userId;}

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

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
