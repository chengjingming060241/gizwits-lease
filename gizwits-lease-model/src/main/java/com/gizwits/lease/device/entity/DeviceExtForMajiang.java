package com.gizwits.lease.device.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 设备扩展表(麻将机)
 * </p>
 *
 * @author yinhui
 * @since 2017-08-30
 */
@TableName("device_ext_for_majiang")
public class DeviceExtForMajiang extends Model<DeviceExtForMajiang> {

    private static final long serialVersionUID = 1L;

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
     * 设备序列号
     */
	private String sno;
    /**
     * 游戏模式：1 极速 2静音
     */
	private Integer mode;
    /**
     * 游戏类型：1标准 2自定义音
     */
	@TableField("game_type")
	private Integer gameType;
    /**
     * 游戏序号，如果是自定义时为空
     */
	@TableField("game_no")
	private Integer gameNo;
    /**
     * 下发指令
     */
	private String command;


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

	public String getSno() {
		return sno;
	}

	public void setSno(String sno) {
		this.sno = sno;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getGameType() {
		return gameType;
	}

	public void setGameType(Integer gameType) {
		this.gameType = gameType;
	}

	public Integer getGameNo() {
		return gameNo;
	}

	public void setGameNo(Integer gameNo) {
		this.gameNo = gameNo;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
