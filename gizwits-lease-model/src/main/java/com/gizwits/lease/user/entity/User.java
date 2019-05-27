package com.gizwits.lease.user.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户表,不要前缀,因为用户模块计划抽象成通用功能
 * </p>
 *
 * @author rongmc
 * @since 2017-06-28
 */
@TableName(value = "user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;


	/**
	 * 主键,自增长
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;

    /**
     * 用户名
     */
	private String username;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 更新时间
     */
	private Date utime;
    /**
     * 别名
     */
	private String nickname;
    /**
     * 第三方开发平台ID
     */
	private String openid;
    /**
     * 第三方平台,1:微信 2:支付宝 3:百度 4:新浪
     */
	@TableField("third_party")
	private Integer thirdParty;
    /**
     * 密码
     */
	private String password;
    /**
     * 手机,绑定时程序上控制唯一
     */
	private String mobile;
    /**
     * 电子邮件,绑定时程序上控制唯一
     */
	private String email;
    /**
     * 性别: 1,男; 2,女; 0,未知
     */
	private Integer gender;
    /**
     * 头像地址
     */
	private String avatar;
    /**
     * 生日
     */
	private Date birthday;
    /**
     * 所属省份
     */
	private String province;
    /**
     * 所属城市
     */
	private String city;
    /**
     * 详细地址
     */
	private String address;
    /**
     * 备注
     */
	private String remark;

	/**
	 * 微信ID,,,不实例化到数据库
     */
	@Transient
	private String wxId;

	/**
	 * 微信用户所属运营商用户ID
     */
	@TableField("sys_user_id")
	private Integer sysUserId;

	/**
	 * 用户状态: 1,正常;2,黑名单
     */
	private Integer status;

	/**
	 * 移入黑名单时间
	 */
	@TableField("move_in_black_time")
	private Date moveInBlackTime;

	/**
	 * 移出黑名单时间
	 */
	@TableField("move_out_black_time")
	private Date moveOutBlackTime;

	/**
	 * 授权时间
	 */
	@TableField("authorization_time")
	private Date authorizationTime;
	/**
	 * 支付宝
	 */
	@TableField("alipay_unionid")
	private String alipayUnionid;
	/**
	 * 新浪
	 */
	@TableField("sina_unionid")
	private String sinaUnionid;
	/**
	 * 百度
	 */
	@TableField("baidu_unionid")
	private String baiduUnionid;
	/**
	 * 验证码
	 */
	private String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getThirdParty() {
		return thirdParty;
	}

	public void setThirdParty(Integer thirdParty) {
		this.thirdParty = thirdParty;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getMoveInBlackTime() {
		return moveInBlackTime;
	}

	public void setMoveInBlackTime(Date moveInBlackTime) {
		this.moveInBlackTime = moveInBlackTime;
	}

	public Date getMoveOutBlackTime() {
		return moveOutBlackTime;
	}

	public void setMoveOutBlackTime(Date moveOutBlackTime) {
		this.moveOutBlackTime = moveOutBlackTime;
	}

	public Date getAuthorizationTime() {
		return authorizationTime;
	}

	public void setAuthorizationTime(Date authorizationTime) {
		this.authorizationTime = authorizationTime;
	}

	public String getAlipayUnionid() {return alipayUnionid;}

	public void setAlipayUnionid(String alipayUnionid) {this.alipayUnionid = alipayUnionid;}

	public String getSinaUnionid() {return sinaUnionid;}

	public void setSinaUnionid(String sinaUnionid) {this.sinaUnionid = sinaUnionid;}

	public String getBaiduUnionid() {return baiduUnionid;}

	public void setBaiduUnionid(String baiduUnionid) {this.baiduUnionid = baiduUnionid;}

	public String getCode() {return code;}

	public void setCode(String code) {this.code = code;}

	@Override
	protected Serializable pkVal() {
		return this.username;
	}

}
