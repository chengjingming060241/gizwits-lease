package com.gizwits.lease.stat.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户趋势及性别，使用次数统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@TableName("stat_user_trend")
public class StatUserTrend extends Model<StatUserTrend> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键,自增
     */
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 添加时间
     */
	private Date ctime;
    /**
     * 归属系统用户id
     */
	@TableField("sys_user_id")
	private Integer sysUserId;
    /**
     * 新增用户数量
     */
	@TableField("new_count")
	private Integer newCount;
    /**
     * 活跃用户数量
     */
	@TableField("active_count")
	private Integer activeCount;
    /**
     * 用户总数
     */
	@TableField("total_count")
	private Integer totalCount;
    /**
     * 男性用户数量
     */
	private Integer male;
    /**
     * 女性用户数量
     */
	private Integer female;
    /**
     * 使用该产品0~1次用户数量
     */
	@TableField("zero")
	private Integer zero;
    /**
     * 使用该产品1~2次用户数量
     */
	@TableField("one_two")
	private Integer oneTwo;
    /**
     * 使用该产品3~4次用户数量
     */
	@TableField("three_four")
	private Integer threeFour;
    /**
     * 使用该产品5~6次用户数量
     */
	@TableField("five_six")
	private Integer fiveSix;
    /**
     * 使用该产品7~8次用户数量
     */
	@TableField("seven_eight")
	private Integer sevenEight;
    /**
     * 使用该产品9~10次用户数量
     */
	@TableField("nine_ten")
	private Integer nineTen;
    /**
     * 使用该产品10次及其以上用户数量
     */
	@TableField("ten_more")
	private Integer tenMore;


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

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public Integer getNewCount() {
		return newCount;
	}

	public void setNewCount(Integer newCount) {
		this.newCount = newCount;
	}

	public Integer getActiveCount() {
		return activeCount;
	}

	public void setActiveCount(Integer activeCount) {
		this.activeCount = activeCount;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getMale() {
		return male;
	}

	public void setMale(Integer male) {
		this.male = male;
	}

	public Integer getFemale() {
		return female;
	}

	public void setFemale(Integer female) {
		this.female = female;
	}



	public Integer getOneTwo() {
		return oneTwo;
	}

	public void setOneTwo(Integer oneTwo) {
		this.oneTwo = oneTwo;
	}

	public Integer getZero() {
		return zero;
	}

	public void setZero(Integer zero) {
		this.zero = zero;
	}

	public Integer getThreeFour() {
		return threeFour;
	}

	public void setThreeFour(Integer threeFour) {
		this.threeFour = threeFour;
	}

	public Integer getFiveSix() {
		return fiveSix;
	}

	public void setFiveSix(Integer fiveSix) {
		this.fiveSix = fiveSix;
	}

	public Integer getSevenEight() {
		return sevenEight;
	}

	public void setSevenEight(Integer sevenEight) {
		this.sevenEight = sevenEight;
	}

	public Integer getNineTen() {
		return nineTen;
	}

	public void setNineTen(Integer nineTen) {
		this.nineTen = nineTen;
	}

	public Integer getTenMore() {
		return tenMore;
	}

	public void setTenMore(Integer tenMore) {
		this.tenMore = tenMore;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
