package com.ueb.wms.printer.client.init;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Executor;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ueb.wms.printer.client.config.HttpClientConfig;

@Configuration
public class HttpClientInit {

	@Autowired
	@Qualifier("httpClientConfig")
	private HttpClientConfig config;

	@Bean(name = "requestConfig")
	public RequestConfig buildRequestConfig() {
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		if (null != config.getConnectTimeout()) {
			requestConfigBuilder.setConnectionRequestTimeout(config.getConnectTimeout());
		}
		if (null != config.getSocketTimeout()) {
			requestConfigBuilder.setSocketTimeout(config.getSocketTimeout());
		}
		return requestConfigBuilder.build();
	}

	@Bean(name = "httpClient", destroyMethod = "close")
	public HttpClient buildHttpClient(RequestConfig requestConfig) {
		HttpClientBuilder clientBuilder = HttpClientBuilder.create();
		clientBuilder.setDefaultRequestConfig(requestConfig);
		clientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
		return clientBuilder.build();
	}

	@Bean(name = "clientExecutor", destroyMethod = "closeIdleConnections")
	public Executor httpClientExecutor(HttpClient httpClient) {
		return Executor.newInstance(httpClient);
	}
}
