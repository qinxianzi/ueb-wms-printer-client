package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

public class SingleSkuMatchReqVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -200394277436920350L;

	/**
	 * 仓库，传空值或*号
	 */
	private String warehouseid_r;

	/**
	 * 客户ID，固定值UEB
	 */
	private String customerid;

	/**
	 * 波次编号
	 */
	private String waveNo;

	/**
	 * SKU编号
	 */
	private String sku;

	/**
	 * 操作台（空值或空字符串）
	 */
	private String workStation;

	private String language;

	/**
	 * 登陆账号
	 */
	private String userID;

	/**
	 * 输出参数（箱号）
	 */
	private String toid;

	/**
	 * 输出参数（订单编号）
	 */
	private String orderNo;

	/**
	 * 返回代码
	 */
	private String return_code;

	public String getWarehouseid_r() {
		return warehouseid_r;
	}

	public void setWarehouseid_r(String warehouseid_r) {
		this.warehouseid_r = warehouseid_r;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getWaveNo() {
		return waveNo;
	}

	public void setWaveNo(String waveNo) {
		this.waveNo = waveNo;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getWorkStation() {
		return workStation;
	}

	public void setWorkStation(String workStation) {
		this.workStation = workStation;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getToid() {
		return toid;
	}

	public void setToid(String toid) {
		this.toid = toid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
}
