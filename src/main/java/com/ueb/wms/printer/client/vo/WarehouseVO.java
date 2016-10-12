package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

public class WarehouseVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4547591767707838078L;

	private String udf01;

	private String udf02;

	public String getUdf01() {
		return udf01;
	}

	public void setUdf01(String udf01) {
		this.udf01 = udf01;
	}

	public String getUdf02() {
		return udf02;
	}

	public void setUdf02(String udf02) {
		this.udf02 = udf02;
	}

	public ComboBoxItemVO adapt2ComboBoxItemVO() {
		ComboBoxItemVO itemVo = new ComboBoxItemVO();
		itemVo.setKey(udf01);
		itemVo.setValue(udf02);
		return itemVo;
	}
}
