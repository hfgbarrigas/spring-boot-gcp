package io.hfbarrigas.gcp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gcp.metadata")
public class GoogleCloudMetadataProperties {

    private String baseUrl = "http://metadata.google.internal";
    private boolean enabled = false;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
