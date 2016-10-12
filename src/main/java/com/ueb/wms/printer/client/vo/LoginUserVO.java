package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

public class LoginUserVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4265669078628488963L;

	private String user_id;

	private String user_name;

	private String password;

	private String active_flag;

	private String salt;

	private String priceaccess;

	private String user_level;

	private String role_id;

	private String role_id_name;

	private String value_int;

	private String value_int2;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActive_flag() {
		return active_flag;
	}

	public void setActive_flag(String active_flag) {
		this.active_flag = active_flag;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPriceaccess() {
		return priceaccess;
	}

	public void setPriceaccess(String priceaccess) {
		this.priceaccess = priceaccess;
	}

	public String getUser_level() {
		return user_level;
	}

	public void setUser_level(String user_level) {
		this.user_level = user_level;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

	public String getRole_id_name() {
		return role_id_name;
	}

	public void setRole_id_name(String role_id_name) {
		this.role_id_name = role_id_name;
	}

	public String getValue_int() {
		return value_int;
	}

	public void setValue_int(String value_int) {
		this.value_int = value_int;
	}

	public String getValue_int2() {
		return value_int2;
	}

	public void setValue_int2(String value_int2) {
		this.value_int2 = value_int2;
	}
}
