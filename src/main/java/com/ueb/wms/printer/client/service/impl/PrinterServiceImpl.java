package com.ueb.wms.printer.client.service.impl;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ueb.wms.printer.client.http.HttpClientService;
import com.ueb.wms.printer.client.service.IPrinterService;
import com.ueb.wms.printer.client.vo.ReportDataVO;

@Service("printerService")
public class PrinterServiceImpl implements IPrinterService {

	private static final Logger logger = LoggerFactory.getLogger(PrinterServiceImpl.class);

	@Autowired()
	@Qualifier("httpClientService")
	private HttpClientService httpService;

	public ReportDataVO findReportData(String search) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("search", search);
			String response = this.httpService.sendPostRequest("findReportData", params);
			ReportDataVO reportDataVO = JSON.parseObject(response, ReportDataVO.class);
			return reportDataVO;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	public List<ReportDataVO> findByWaveNO(String waveNO) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("waveNO", waveNO);
			String response = this.httpService.sendPostRequest("findByWaveNO", params);
			List<ReportDataVO> reportDataVO = JSON.parseArray(response, ReportDataVO.class);
			return reportDataVO;
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	public BufferedImage downloadPdfTemplate(String orderNO) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("orderNO", orderNO);
			HttpEntity entity = this.httpService.downloadPdfTemplate("downloadPdfTemplate", params);
			return this.tranferPdf2Image(entity);
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	protected BufferedImage tranferPdf2Image(HttpEntity entity) throws Exception {
		InputStream input = null;
		try {
			input = entity.getContent();
			PDDocument document = PDDocument.load(input);
			PDFRenderer renderer = new PDFRenderer(document);
			BufferedImage image = renderer.renderImageWithDPI(0, 150, ImageType.RGB); // 只取第1页
			return image;
		} catch (Exception e) {
			throw e;
		} finally {
			if (null != input) {
				input.close();
			}
		}
	}

	@Override
	public void addCarrieridTplTypeRel(String carrierid, int tplType) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("carrierid", carrierid);
			params.put("tplTtype", String.valueOf(tplType));
			this.httpService.sendPostRequest("addCarrieridTplTypeRel", params);
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}

	@Override
	public void updateHttpServerInfo(String url, String port, String context) throws Exception {
		this.httpService.updateHttpClientConfig(url, port, context);
	}

	@Override
	public Map<String, String> getConfigValues() {
		return this.httpService.getConfigValues();
	}

	@Override
	public void userLogin(String account, String password) throws Exception {
		try {
			Map<String, String> params = new HashMap<String, String>(10);
			params.put("account", account);
			params.put("password", password);
			this.httpService.sendPostRequest("userLogin", params);
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw e;
		}
	}
}
