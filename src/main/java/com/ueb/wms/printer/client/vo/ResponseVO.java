package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

import com.ueb.wms.printer.client.constants.WmsConstants;

/**
 * 响应消息VO
 * 
 * @author liangxf
 *
 */
public class ResponseVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8841512154435476070L;

	/**
	 * 响应码
	 */
	private int code;

	/**
	 * 响应消息
	 */
	private String message;

	/**
	 * 响应内容
	 */
	private String content;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() throws Exception {
		if (WmsConstants.RESPONSE_CODE_SUCCESS != this.getCode()) {
			throw new Exception(this.getMessage());
		}
		return content;
	}
}
