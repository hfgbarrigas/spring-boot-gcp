package io.hfbarrigas.gcp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("gcp.credentials")
public class GoogleCloudCredentialsProperties {

    public enum Type {
        DEFAULT,
        FILE
    }

    private File file;
    private boolean enabled = false;
    private Type type = Type.DEFAULT;

    public static class File {
        private String name = "";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
