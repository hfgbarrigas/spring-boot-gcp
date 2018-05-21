package io.hfbarrigas.gcp.client.credentials;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import java.io.IOException;
import java.io.InputStream;

public class DefaultGoogleCredentials implements GoogleCredentials {

    @Override
    public GoogleCredential getCredentials(InputStream in) throws IOException {
        return GoogleCredential.fromStream(in);
    }

    @Override
    public GoogleCredential getCredentials() throws IOException {
        return GoogleCredential.getApplicationDefault();
    }
}
