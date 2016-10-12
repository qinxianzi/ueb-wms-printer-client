package com.ueb.wms.printer.client.service;

import com.ueb.wms.printer.client.vo.AccountVO;
import com.ueb.wms.printer.client.vo.BeforeLoginVO;
import com.ueb.wms.printer.client.vo.LoginUserVO;

public interface IUserService {

	/**
	 * 用户登陆
	 * 
	 * @param account
	 *            登陆账号
	 * @param password
	 *            登陆密码
	 * @return
	 * @throws Exception
	 */
	AccountVO userLogin(String account, String password) throws Exception;

	BeforeLoginVO readyToLogin(String userId) throws Exception;

	LoginUserVO findUuser(String userId) throws Exception;
}
