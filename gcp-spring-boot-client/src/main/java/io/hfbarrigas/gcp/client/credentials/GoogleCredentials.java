package io.hfbarrigas.gcp.client.credentials;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public interface GoogleCredentials {
    GoogleCredential getCredentials(InputStream in) throws IOException, URISyntaxException;
    GoogleCredential getCredentials() throws IOException;
}
