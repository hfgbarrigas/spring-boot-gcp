package io.hfbarrigas.gcp.client.metadata;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import io.hfbarrigas.gcp.client.utils.Loggable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class DefaultGoogleCloudMetadata implements GoogleCloudMetadata, Loggable {

    private static final String EMPTY_STRING = "", REGION = "region", ZONE = "zone";
    private static final String[] metataPaths = new String[]{
            "/computeMetadata/v1/project/project-id",
            "/computeMetadata/v1/instance/zone"
    };

    private HttpClient httpClient;
    private String metadataUrl, zone, region, projectId;

    public DefaultGoogleCloudMetadata(HttpClient httpClient, String metadataUrl) {
        this.httpClient = httpClient;
        this.metadataUrl = metadataUrl;
    }

    @Override
    public String getRegion() {
        if (Strings.isNullOrEmpty(this.region)) {
            Map<String, String> data = parseZoneMetadata(this.getGcpMetadata(metataPaths[1]));
            this.region = data.getOrDefault(REGION, EMPTY_STRING);
            this.zone = data.getOrDefault(ZONE, EMPTY_STRING);
        }
        return this.region;
    }

    @Override
    public String getZone() {
        if (Strings.isNullOrEmpty(this.zone)) {
            Map<String, String> data = parseZoneMetadata(this.getGcpMetadata(metataPaths[1]));
            this.region = data.getOrDefault(REGION, EMPTY_STRING);
            this.zone = data.getOrDefault(ZONE, EMPTY_STRING);
        }
        return this.zone;
    }

    @Override
    public String getProjectId() {
        if (Strings.isNullOrEmpty(projectId)) {
            this.projectId = this.getGcpMetadata(metataPaths[0]);
        }
        return projectId;
    }

    private static HttpGet buildRequest(final String url) {
        HttpGet httpGet = new HttpGet();
        httpGet.setHeader("Metadata-Flavor", "Google");
        httpGet.setURI(URI.create(url));
        return httpGet;
    }

    private Map<String, String> parseZoneMetadata(String zoneMetadata) {
        String metadataPieces[] = zoneMetadata.split("/");

        if (metadataPieces.length > 1) {
            String zone = metadataPieces[metadataPieces.length - 1];

            //region-az - us-east1-east1a // europe-west1
            String[] zoneElements = zone.split("-");
            if (zoneElements.length > 1) {
                return ImmutableMap.of(
                        REGION, zoneElements.length > 2 ? zoneElements[0] + '-' + zoneElements[1] : zoneElements[0],
                        ZONE, zoneElements[zoneElements.length - 1]
                );
            }

        }
        return ImmutableMap.<String, String>builder().build();
    }

    private String getGcpMetadata(String path) {
        String responseBody = EMPTY_STRING;
        HttpGet httpGet = buildRequest(metadataUrl.concat(path));

        logger().debug("Requesting GCP metadata.");

        try {

            HttpResponse httpResponse = httpClient.execute(httpGet);

            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity httpEntity = httpResponse.getEntity();

                responseBody = httpEntity != null ? EntityUtils.toString(httpEntity) : null;
                logger().debug("GCP metadata response: {}", responseBody);

                return responseBody;
            } else {
                logger().error(String.format("GCP metadata error %s", statusCode));
            }

        } catch (IOException e) {
            //silence, shh
        }

        return responseBody;
    }

}
