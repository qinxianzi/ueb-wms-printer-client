package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

public class RolesVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1793283601299261761L;

	private String role_name;

	private String role_description;

	private int role_type;

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_description() {
		return role_description;
	}

	public void setRole_description(String role_description) {
		this.role_description = role_description;
	}

	public int getRole_type() {
		return role_type;
	}

	public void setRole_type(int role_type) {
		this.role_type = role_type;
	}
}
