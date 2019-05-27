package com.gizwits.lease.benefit.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;

/**
 * <p>
 * 分润单操作记录
 * </p>
 *
 * @author lilh
 * @since 2017-08-03
 */
@TableName("share_benefit_sheet_action_record")
public class ShareBenefitSheetActionRecord extends Model<ShareBenefitSheetActionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
	private Integer id;
    /**
     * 分润单ID
     */
	@TableField("sheet_id")
	private Integer sheetId;
    /**
     * 操作类型：0，创建分润单；1，审核通过；2、重新审核；3，执行分润
     */
	@TableField("action_type")
	private Integer actionType;
    /**
     * 操作者
     */
	@TableField("user_id")
	private Integer userId;
    /**
     * 操作时间
     */
	private Date ctime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSheetId() {
		return sheetId;
	}

	public void setSheetId(Integer sheetId) {
		this.sheetId = sheetId;
	}

	public Integer getActionType() {
		return actionType;
	}

	public void setActionType(Integer actionType) {
		this.actionType = actionType;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
