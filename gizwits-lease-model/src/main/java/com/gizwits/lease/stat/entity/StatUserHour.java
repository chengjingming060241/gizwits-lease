package com.gizwits.lease.stat.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 控制设备时段用户统计表
 * </p>
 *
 * @author gagi
 * @since 2017-07-11
 */
@TableName("stat_user_hour")
public class StatUserHour extends Model<StatUserHour> {

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
     * 0点用户控制设备数量
     */
	private Integer zero;
    /**
     * 1点用户控制设备数量
     */
	@TableField("ONE")
	private Integer one;
    /**
     * 2点用户控制设备数量
     */
	private Integer two;
    /**
     * 3点用户控制设备数量
     */
	private Integer three;
    /**
     * 4点用户控制设备数量
     */
	private Integer four;
    /**
     * 5点用户控制设备数量
     */
	private Integer five;
    /**
     * 6点用户控制设备数量
     */
	private Integer six;
    /**
     * 7点用户控制设备数量
     */
	private Integer seven;
    /**
     * 8点用户控制设备数量
     */
	private Integer eight;
    /**
     * 9点用户控制设备数量
     */
	private Integer nine;
    /**
     * 10点用户控制设备数量
     */
	private Integer ten;
    /**
     * 11点用户控制设备数量
     */
	private Integer eleven;
    /**
     * 12点用户控制设备数量
     */
	private Integer twelve;
    /**
     * 13点用户控制设备数量
     */
	private Integer thriteen;
    /**
     * 14点用户控制设备数量
     */
	private Integer fourteen;
    /**
     * 15点用户控制设备数量
     */
	private Integer fifteen;
    /**
     * 16点用户控制设备数量
     */
	private Integer siteen;
    /**
     * 17点用户控制设备数量
     */
	private Integer seventeen;
    /**
     * 18点用户控制设备数量
     */
	private Integer eighteen;
    /**
     * 19点用户控制设备数量
     */
	private Integer nineteen;
    /**
     * 20点用户控制设备数量
     */
	private Integer twenty;
    /**
     * 21点用户控制设备数量
     */
	@TableField("twenty_one")
	private Integer twentyOne;
    /**
     * 22点用户控制设备数量
     */
	@TableField("twenty_two")
	private Integer twentyTwo;
    /**
     * 23点用户控制设备数量
     */
	@TableField("twenty_three")
	private Integer twentyThree;


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

	public Integer getZero() {
		return zero;
	}

	public void setZero(Integer zero) {
		this.zero = zero;
	}

	public Integer getOne() {
		return one;
	}

	public void setOne(Integer one) {
		this.one = one;
	}

	public Integer getTwo() {
		return two;
	}

	public void setTwo(Integer two) {
		this.two = two;
	}

	public Integer getThree() {
		return three;
	}

	public void setThree(Integer three) {
		this.three = three;
	}

	public Integer getFour() {
		return four;
	}

	public void setFour(Integer four) {
		this.four = four;
	}

	public Integer getFive() {
		return five;
	}

	public void setFive(Integer five) {
		this.five = five;
	}

	public Integer getSix() {
		return six;
	}

	public void setSix(Integer six) {
		this.six = six;
	}

	public Integer getSeven() {
		return seven;
	}

	public void setSeven(Integer seven) {
		this.seven = seven;
	}

	public Integer getEight() {
		return eight;
	}

	public void setEight(Integer eight) {
		this.eight = eight;
	}

	public Integer getNine() {
		return nine;
	}

	public void setNine(Integer nine) {
		this.nine = nine;
	}

	public Integer getTen() {
		return ten;
	}

	public void setTen(Integer ten) {
		this.ten = ten;
	}

	public Integer getEleven() {
		return eleven;
	}

	public void setEleven(Integer eleven) {
		this.eleven = eleven;
	}

	public Integer getTwelve() {
		return twelve;
	}

	public void setTwelve(Integer twelve) {
		this.twelve = twelve;
	}

	public Integer getThriteen() {
		return thriteen;
	}

	public void setThriteen(Integer thriteen) {
		this.thriteen = thriteen;
	}

	public Integer getFourteen() {
		return fourteen;
	}

	public void setFourteen(Integer fourteen) {
		this.fourteen = fourteen;
	}

	public Integer getFifteen() {
		return fifteen;
	}

	public void setFifteen(Integer fifteen) {
		this.fifteen = fifteen;
	}

	public Integer getSiteen() {
		return siteen;
	}

	public void setSiteen(Integer siteen) {
		this.siteen = siteen;
	}

	public Integer getSeventeen() {
		return seventeen;
	}

	public void setSeventeen(Integer seventeen) {
		this.seventeen = seventeen;
	}

	public Integer getEighteen() {
		return eighteen;
	}

	public void setEighteen(Integer eighteen) {
		this.eighteen = eighteen;
	}

	public Integer getNineteen() {
		return nineteen;
	}

	public void setNineteen(Integer nineteen) {
		this.nineteen = nineteen;
	}

	public Integer getTwenty() {
		return twenty;
	}

	public void setTwenty(Integer twenty) {
		this.twenty = twenty;
	}

	public Integer getTwentyOne() {
		return twentyOne;
	}

	public void setTwentyOne(Integer twentyOne) {
		this.twentyOne = twentyOne;
	}

	public Integer getTwentyTwo() {
		return twentyTwo;
	}

	public void setTwentyTwo(Integer twentyTwo) {
		this.twentyTwo = twentyTwo;
	}

	public Integer getTwentyThree() {
		return twentyThree;
	}

	public void setTwentyThree(Integer twentyThree) {
		this.twentyThree = twentyThree;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
