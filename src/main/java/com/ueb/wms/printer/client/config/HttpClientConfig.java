package com.ueb.wms.printer.client.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Component;

@Component("httpClientConfig")
public class HttpClientConfig {

	public HttpClientConfig() {
		try {
			this.initProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initProperties() throws IOException {
		FileInputStream input = null;
		try {
			Properties properties = new Properties();
			input = new FileInputStream(this.getFile());
			properties.load(input);

			connectTimeout = Integer.valueOf(properties.getProperty("client.connectTimeout"));
			socketTimeout = Integer.valueOf(properties.getProperty("client.socketTimeout"));
			url = properties.getProperty("client.url");
			port = properties.getProperty("client.port");
			context = properties.getProperty("client.context");
		} finally {
			if (null != input) {
				input.close();
			}
		}
	}

	public File getFile() {
		File file = new File("resources/httpclient.properties");
		return file;
	}

	/**
	 * 连接超时时间(毫秒)
	 */
	private Integer connectTimeout;

	/**
	 * 响应的超时时间(毫秒)
	 */
	private Integer socketTimeout;

	/**
	 * 请求地址
	 */
	private String url;

	/**
	 * 请求端口号
	 */
	private String port;

	/**
	 * 服务器上下文根
	 */
	private String context;

	/**
	 * PDF模板文件路径
	 */
	private String pdfTpl;

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

	public String getPdfTpl() {
		return pdfTpl;
	}

	public void setPdfTpl(String pdfTpl) {
		this.pdfTpl = pdfTpl;
	}

	public Map<String, String> getValues() {
		Map<String, String> values = new HashMap<String, String>();
		values.put("client.connectTimeout", String.valueOf(connectTimeout));
		values.put("client.socketTimeout", String.valueOf(socketTimeout));
		values.put("client.url", url);
		values.put("client.port", port);
		values.put("client.context", context);
		values.put("client.pdfTpl", pdfTpl);
		return values;
	}
}
