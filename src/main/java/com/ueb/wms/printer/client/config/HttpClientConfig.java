package com.ueb.wms.printer.client.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component("httpClientConfig")
@PropertySource("classpath:httpclient.properties")
public class HttpClientConfig {

	/**
	 * 连接超时时间(毫秒)
	 */
	@Value("${client.connectTimeout}")
	private Integer connectTimeout;

	/**
	 * 响应的超时时间(毫秒)
	 */
	@Value("${client.socketTimeout}")
	private Integer socketTimeout;

	/**
	 * 请求地址
	 */
	@Value("${client.url}")
	private String url;

	/**
	 * 请求端口号
	 */
	@Value("${client.port}")
	private String port;

	/**
	 * 服务器上下文根
	 */
	@Value("${client.context}")
	private String context;

	public Integer getConnectTimeout() {
		return connectTimeout;
	}

	public Integer getSocketTimeout() {
		return socketTimeout;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public Map<String, String> getValues() {
		Map<String, String> values = new HashMap<String, String>();
		values.put("client.connectTimeout", String.valueOf(connectTimeout));
		values.put("client.socketTimeout", String.valueOf(socketTimeout));
		values.put("client.url", url);
		values.put("client.port", port);
		values.put("client.context", context);
		return values;
	}
}
