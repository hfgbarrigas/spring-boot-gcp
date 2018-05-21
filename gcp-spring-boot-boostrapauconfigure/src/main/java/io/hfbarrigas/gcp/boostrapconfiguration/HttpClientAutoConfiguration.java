package io.hfbarrigas.gcp.boostrapconfiguration;

import io.hfbarrigas.gcp.properties.HttpClientProperties;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

    @Bean
    @ConditionalOnClass(PoolingHttpClientConnectionManager.class)
    @ConditionalOnMissingBean(PoolingHttpClientConnectionManager.class)
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(HttpClientProperties httpClientProperties) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(httpClientProperties.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(httpClientProperties.getMaxConnectionsPerRoute());
        return connectionManager;
    }

    @Bean
    @ConditionalOnClass(RequestConfig.class)
    public RequestConfig requestConfig(HttpClientProperties httpClientProperties) {
        return RequestConfig
                .custom()
                .setConnectionRequestTimeout(httpClientProperties.getReadTimeout())
                .setSocketTimeout(httpClientProperties.getSocketTimeout())
                .setConnectTimeout(httpClientProperties.getConnectTimeout())
                .build();
    }

    @Bean
    @ConditionalOnClass(HttpClient.class)
    @ConditionalOnMissingBean(HttpClient.class)
    @ConditionalOnBean({PoolingHttpClientConnectionManager.class})
    public HttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder
                .create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
