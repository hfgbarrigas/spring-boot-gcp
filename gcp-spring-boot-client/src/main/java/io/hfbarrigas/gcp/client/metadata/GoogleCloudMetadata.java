package io.hfbarrigas.gcp.client.metadata;

public interface GoogleCloudMetadata {
    String getRegion();
    String getZone();
    String getProjectId();
}
