package com.ueb.wms.printer.client.util;

import org.springframework.stereotype.Component;

import com.ueb.wms.printer.client.vo.LoginUserVO;

@Component("loginUtil")
public class LoginUtil {

	// /**
	// * 登陆账号
	// */
	// private AccountVO loginAccount;

	/**
	 * 当前登陆用户
	 */
	private LoginUserVO loginUserVo;

	/**
	 * 当前使用的业务仓库
	 */
	private String warehouse;

	// public AccountVO getLoginAccount() {
	// return loginAccount;
	// }
	//
	// public void setLoginAccount(AccountVO loginAccount) {
	// this.loginAccount = loginAccount;
	// }

	public LoginUserVO getLoginUserVo() {
		return loginUserVo;
	}

	public void setLoginUserVo(LoginUserVO loginUserVo) {
		this.loginUserVo = loginUserVo;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	// public boolean hasRole(String role) {
	// List<RolesVO> rolesList = loginAccount.getRolesList();
	// if (null == rolesList || rolesList.isEmpty()) {
	// return false;
	// }
	// for (Iterator<RolesVO> it = rolesList.iterator(); it.hasNext();) {
	// RolesVO roleVo = it.next();
	// if (StringUtils.equals(role, roleVo.getRole_name())) {
	// return true;
	// }
	// }
	// return false;
	// }
	//
	public String getCurrentAccount() {
		return loginUserVo.getUser_id();
	}
}
