package io.hfbarrigas.gcp.boostrapconfiguration;

import io.hfbarrigas.gcp.client.metadata.DefaultGoogleCloudMetadata;
import io.hfbarrigas.gcp.client.metadata.GoogleCloudMetadata;
import io.hfbarrigas.gcp.properties.GoogleCloudMetadataProperties;
import org.apache.http.client.HttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@EnableConfigurationProperties(GoogleCloudMetadataProperties.class)
@AutoConfigureAfter({ HttpClientAutoConfiguration.class})
@ConditionalOnProperty(name = {"gcp.metadata.enabled"}, havingValue = "true")
@Order(1)
public class GoogleCloudMetadataConfiguration {

    @Bean
    @ConditionalOnMissingBean(GoogleCloudMetadata.class)
    @ConditionalOnBean(HttpClient.class)
    public GoogleCloudMetadata googleCloudRegionAwareness(GoogleCloudMetadataProperties googleCloudMetadataProperties, HttpClient httpClient) {
        return new DefaultGoogleCloudMetadata(httpClient, googleCloudMetadataProperties.getBaseUrl());
    }
}
