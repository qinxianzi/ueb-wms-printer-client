package com.ueb.wms.printer.client.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ueb.wms.printer.client.vo.ResponseVO;

@Service("httpClientService")
public class HttpClientService extends HttpClientBaseService {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientService.class);

	public String sendPostRequest(String apiname, Map<String, String> params) throws Exception {
		String apipath = this.findApiByName(apiname);
		try {
			String requestUri = this.buildRequestUri(apipath);
			Request request = this.buildPostRequest(requestUri, params);
			String response = this.sendRequest(request);

			ResponseVO responseVO = JSON.parseObject(response, ResponseVO.class);
			return responseVO.getContent();
		} catch (Exception e) {
			logger.info("请求后台{}接口出现异常，异常详细信息是:{}", apiname, e.getMessage());
			throw e;
		}
	}

	public HttpEntity downloadPdfTemplate(String apiname, Map<String, String> params) throws Exception {
		String apipath = this.findApiByName(apiname);
		try {
			String requestUri = this.buildRequestUri(apipath);
			Request request = this.buildPostRequest(requestUri, params);

			HttpEntity entity = this.downloadPdfTemplate(request);
			return entity;
		} catch (Exception e) {
			logger.info("请求后台{}接口出现异常，异常详细信息是:{}", apiname, e.getMessage());
			throw e;
		}
	}

	public void updateHttpClientConfig(String url, String port, String context, String pdfTpl) throws Exception {
		this.config.setUrl(url);
		this.config.setPort(port);
		this.config.setContext(context);
		if (StringUtils.isNotBlank(pdfTpl)) {
			this.config.setPdfTpl(pdfTpl);
		}
		this.saveHttpClientConfig2properties();
	}

	private void saveHttpClientConfig2properties() throws IOException {
		// String file =
		// this.getClass().getResource("httpclient.properties").getFile();
		// File file = new File("config/httpclient.properties");
		File file = config.getFile();
		FileOutputStream output = null;
		Properties properties = null;
		try {
			output = new FileOutputStream(file, false);
			properties = new Properties();
			properties.putAll(config.getValues());
			properties.store(output, "");
		} finally {
			if (null != output) {
				output.flush();
				output.close();
			}
			if (null != properties) {
				properties.clear();
			}
		}
	}

	public Map<String, String> getConfigValues() {
		return this.config.getValues();
	}
}
