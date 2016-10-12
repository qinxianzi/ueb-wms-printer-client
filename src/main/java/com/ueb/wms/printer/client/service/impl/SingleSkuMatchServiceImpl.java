package com.ueb.wms.printer.client.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ueb.wms.printer.client.http.HttpClientService;
import com.ueb.wms.printer.client.service.ISingleSkuMatchService;
import com.ueb.wms.printer.client.util.LoginUtil;
import com.ueb.wms.printer.client.vo.ProductReviewVO;
import com.ueb.wms.printer.client.vo.SingleSkuMatchReqVO;
import com.ueb.wms.printer.client.vo.SingleSkuMatchResVO;

@Service("singleSkuMatchService")
public class SingleSkuMatchServiceImpl implements ISingleSkuMatchService {

	private static final Logger logger = LoggerFactory.getLogger(PrinterServiceImpl.class);

	@Autowired
	private LoginUtil loginUtil;

	@Autowired
	@Qualifier("httpClientService")
	private HttpClientService httpService;

	@Override
	public ProductReviewVO findCheckQty(String waveNO) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("waveNO", waveNO);
			String response = this.httpService.sendPostRequest("findCheckQty", params);
			ProductReviewVO reviewVo = JSON.parseObject(response, ProductReviewVO.class);
			return reviewVo;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public SingleSkuMatchResVO singleSkuMatch(String waveNo, String sku) throws Exception {
		try {
			SingleSkuMatchReqVO requestVo = new SingleSkuMatchReqVO();
			requestVo.setWaveNo(waveNo);
			requestVo.setWarehouseid_r(loginUtil.getWarehouse());
			requestVo.setSku(sku);
			requestVo.setUserID(loginUtil.getCurrentAccount());
			String matchContent = JSON.toJSONString(requestVo);

			Map<String, String> params = new HashMap<String, String>(10);
			params.put("matchContent", matchContent);
			String response = this.httpService.sendPostRequest("singleSkuMatch", params);
			SingleSkuMatchResVO resVo = JSON.parseObject(response, SingleSkuMatchResVO.class);
			return resVo;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public String getIDSequence(String warehouseid) throws Exception {
		try {
			warehouseid = StringUtils.isBlank(warehouseid) ? loginUtil.getWarehouse() : warehouseid;
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("warehouseid", warehouseid);
			String response = this.httpService.sendPostRequest("getIDSequence", params);
			return response;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}
}
