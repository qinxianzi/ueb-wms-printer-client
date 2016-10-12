package com.ueb.wms.printer.client.vo;

import java.io.Serializable;

public class ProductReviewVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7673930939487695522L;

	/**
	 * 总数量
	 */
	private Float checktotalqty;

	/**
	 * 已复核数量
	 */
	private Float checkpackedqty;

	/**
	 * 未复核数量
	 */
	private Float checknopackedqty;

	public Float getChecktotalqty() {
		return checktotalqty;
	}

	public void setChecktotalqty(Float checktotalqty) {
		this.checktotalqty = checktotalqty;
	}

	public Float getCheckpackedqty() {
		return checkpackedqty;
	}

	public void setCheckpackedqty(Float checkpackedqty) {
		this.checkpackedqty = checkpackedqty;
	}

	public Float getChecknopackedqty() {
		return checknopackedqty;
	}

	public void setChecknopackedqty(Float checknopackedqty) {
		this.checknopackedqty = checknopackedqty;
	}
}
