package io.hfbarrigas.gcp.boostrapconfiguration;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.auth.Credentials;
import com.google.common.base.Strings;
import io.hfbarrigas.gcp.client.credentials.DefaultGoogleCredentials;
import io.hfbarrigas.gcp.properties.GoogleCloudCredentialsProperties;
import org.springframework.beans.InvalidPropertyException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MissingRequiredPropertiesException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Configuration
@ConditionalOnMissingBean(Credentials.class)
@ConditionalOnProperty(name = {"gcp.credentials.enabled"}, havingValue = "true")
@EnableConfigurationProperties(GoogleCloudCredentialsProperties.class)
@Order(1)
public class GoogleCloudCredentialsAutoConfiguration {

    @Bean
    @ConditionalOnProperty(name = {"gcp.credentials.type"})
    public GoogleCredential googleCredential(GoogleCloudCredentialsProperties googleCloudCredentialsProperties) throws IOException {

        switch (googleCloudCredentialsProperties.getType()) {
            case DEFAULT:
                return new DefaultGoogleCredentials().getCredentials();
            case FILE:
                if (googleCloudCredentialsProperties.getFile() == null || Strings.isNullOrEmpty(Objects.requireNonNull(googleCloudCredentialsProperties.getFile()).getName())) {
                    MissingRequiredPropertiesException ex = new MissingRequiredPropertiesException();
                    ex.getMissingRequiredProperties().add("gcp.credentials.file.name");
                    throw ex;
                }
                return new DefaultGoogleCredentials()
                        .getCredentials(new BufferedInputStream(
                                Files.newInputStream(Paths.get(googleCloudCredentialsProperties.getFile().getName()))
                        ));
            default:
                throw new InvalidPropertyException(GoogleCloudCredentialsProperties.class, "type", String.format("Unknown credentials type - %s.", googleCloudCredentialsProperties.getType()));
        }
    }
}


