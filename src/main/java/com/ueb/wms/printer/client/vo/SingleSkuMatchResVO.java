package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

public class SingleSkuMatchResVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -200394277436920350L;

	/**
	 * 产品中文名
	 */
	private String descr_c;

	/**
	 * 产品英文名
	 */
	private String descr_e;

	/**
	 * 箱号
	 */
	private String toid;

	/**
	 * 装箱量
	 */
	private Float packingQty;

	/**
	 * 波次SKU总数
	 */
	private Float waveSkuCount;

	private String code;
	private String message;

	private ReportDataVO reportData;

	public String getDescr_c() {
		return descr_c;
	}

	public void setDescr_c(String descr_c) {
		this.descr_c = descr_c;
	}

	public String getDescr_e() {
		return descr_e;
	}

	public void setDescr_e(String descr_e) {
		this.descr_e = descr_e;
	}

	public String getToid() {
		return toid;
	}

	public void setToid(String toid) {
		this.toid = toid;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ReportDataVO getReportData() {
		return reportData;
	}

	public void setReportData(ReportDataVO reportData) {
		this.reportData = reportData;
	}

	public Float getPackingQty() {
		return packingQty;
	}

	public void setPackingQty(Float packingQty) {
		this.packingQty = packingQty;
	}

	public Float getWaveSkuCount() {
		return waveSkuCount;
	}

	public void setWaveSkuCount(Float waveSkuCount) {
		this.waveSkuCount = waveSkuCount;
	}

	public ReportDataVO getReportVo() {
		return this.reportData;
	}
}
