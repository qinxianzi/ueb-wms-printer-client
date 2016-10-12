package com.ueb.wms.printer.client.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ueb.wms.printer.client.http.HttpClientService;
import com.ueb.wms.printer.client.service.IUserService;
import com.ueb.wms.printer.client.vo.AccountVO;
import com.ueb.wms.printer.client.vo.BeforeLoginVO;
import com.ueb.wms.printer.client.vo.LoginUserVO;

@Service("userService")
public class UserServiceImpl implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired()
	@Qualifier("httpClientService")
	private HttpClientService httpService;

	@Override
	public AccountVO userLogin(String account, String password) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("account", account);
			params.put("password", password);
			String response = this.httpService.sendPostRequest("userLogin", params);
			AccountVO accountVo = JSON.parseObject(response, AccountVO.class);
			return accountVo;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public BeforeLoginVO readyToLogin(String userId) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("userId", userId);
			String response = this.httpService.sendPostRequest("readyToLogin", params);
			BeforeLoginVO resVo = JSON.parseObject(response, BeforeLoginVO.class);
			return resVo;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public LoginUserVO findUuser(String userId) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("userId", userId);
			String response = this.httpService.sendPostRequest("findUuser", params);
			LoginUserVO userVo = JSON.parseObject(response, LoginUserVO.class);
			return userVo;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}
}
