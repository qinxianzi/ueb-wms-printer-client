package com.ueb.wms.printer.client.service;

import com.ueb.wms.printer.client.vo.ProductReviewVO;
import com.ueb.wms.printer.client.vo.SingleSkuMatchResVO;

public interface ISingleSkuMatchService {

	ProductReviewVO findCheckQty(String waveNO) throws Exception;

	SingleSkuMatchResVO singleSkuMatch(String waveNo, String sku) throws Exception;

	String getIDSequence(String warehouseid) throws Exception;
}
