package com.ueb.wms.printer.client.vo;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class AccountVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7791481196937354574L;

	private String account;

	private String name;

	private List<RolesVO> rolesList;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<RolesVO> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<RolesVO> rolesList) {
		this.rolesList = rolesList;
	}

	public void setRoles(String roles) {
		List<RolesVO> roleList = JSON.parseArray(roles, RolesVO.class);
		this.setRolesList(roleList);
	}
}
