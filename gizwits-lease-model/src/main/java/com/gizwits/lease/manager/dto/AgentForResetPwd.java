package com.gizwits.lease.manager.dto;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author Jin
 * @date 2019/2/22
 */
public class AgentForResetPwd {
    @NotNull
    private Integer id;

    @NotBlank
    private String newPwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
