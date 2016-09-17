package com.ueb.wms.printer.client.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSON;
import com.ueb.wms.printer.client.config.HttpClientConfig;
import com.ueb.wms.printer.client.util.ApiMappingUtil;
import com.ueb.wms.printer.client.vo.ApiMappingVO;
import com.ueb.wms.printer.client.vo.ResponseVO;

public class HttpClientBaseService {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientBaseService.class);

	@Autowired
	@Qualifier("httpClientConfig")
	protected HttpClientConfig config;

	@Autowired
	private Executor clientExecutor;

	private List<ApiMappingVO> apiList;

	public HttpClientBaseService() {
		this.initialization();
	}

	private void initialization() {
		try {
			String content = ApiMappingUtil.readApiMappingFile();
			List<ApiMappingVO> apiList = JSON.parseArray(content, ApiMappingVO.class);
			this.apiList = apiList;
		} catch (IOException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
	}

	protected String findApiByName(String apiname) {
		String apipath = "";
		if (StringUtils.isBlank(apiname)) {
			return apipath;
		}
		for (Iterator<ApiMappingVO> it = apiList.iterator(); it.hasNext();) {
			ApiMappingVO apiVO = it.next();
			String name = apiVO.getApiname();
			if (StringUtils.equals(apiname, name)) {
				apipath = apiVO.getApipath();
				break;
			}
		}
		return apipath;
	}

	protected HttpEntity downloadPdfTemplate(Request request) throws Exception {
		Response response = null;
		try {
			response = clientExecutor.execute(request);
			HttpEntity entity = response.returnResponse().getEntity();

			Header header = entity.getContentType();
			String contentType = header.getValue();
			if (StringUtils.equalsIgnoreCase(contentType, "application/json;charset=UTF-8")) { // 后台出错
				String content = EntityUtils.toString(entity, Consts.UTF_8);
				ResponseVO resVo = JSON.parseObject(content, ResponseVO.class);
				throw new Exception(resVo.getMessage());
			}
			return entity;
		} catch (Exception e) {
			logger.info("an exception occurred during the request，the request is: {}, the exception details are：{}",
					request.toString(), e.getMessage());
			throw e;
		} finally {
			if (null != response) {
				response.discardContent();
			}
		}
	}

	protected String sendRequest(Request request) throws Exception {
		Response response = null;
		try {
			response = clientExecutor.execute(request);
			HttpEntity entity = response.returnResponse().getEntity();
			String content = EntityUtils.toString(entity, Consts.UTF_8);
			return content;
		} catch (Exception e) {
			logger.info("an exception occurred during the request，the request is: {}, the exception details are：{}",
					request.toString(), e.getMessage());
			throw e;
		} finally {
			if (null != response) {
				response.discardContent();
			}
		}
	}

	protected List<BasicNameValuePair> buildRequestParams(Map<String, String> params) {
		params = null == params ? new HashMap<String, String>(10) : params;
		List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>(params.size());
		for (Entry<String, String> entry : params.entrySet()) {
			BasicNameValuePair pair = new BasicNameValuePair(entry.getKey(), entry.getValue());
			pairList.add(pair);
		}
		return pairList;
	}

	protected Request buildPostRequest(String url, Map<String, String> params) {
		Request request = Request.Post(url);
		List<BasicNameValuePair> basicParams = this.buildRequestParams(params);
		if (null != basicParams && !basicParams.isEmpty()) {
			request.bodyForm(basicParams, Consts.UTF_8);
		}
		return request;
	}

	protected String buildRequestUri(String path) {
		if (StringUtils.isBlank(path)) {
			throw new IllegalArgumentException("path parameters can not be empty");
		}
		String remoteUri = config.getUrl();
		if (StringUtils.endsWith(remoteUri, "/")) {
			remoteUri = StringUtils.substring(remoteUri, 0, remoteUri.length() - 1);
		}

		StringBuffer uri = new StringBuffer(remoteUri);
		uri.append(":").append(config.getPort());

		String context = config.getContext();
		if (StringUtils.endsWith(context, "/")) {
			context = (1 == context.length() ? "" : StringUtils.substring(context, 0, context.length() - 1));
		}

		uri.append(context).append(path);
		return uri.toString();
	}
}
