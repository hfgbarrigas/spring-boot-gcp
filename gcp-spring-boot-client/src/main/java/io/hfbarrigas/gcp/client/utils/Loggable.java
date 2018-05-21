package io.hfbarrigas.gcp.client.utils;

public interface Loggable {

    /**
     * @return Logger for target Class
     */
    default org.slf4j.Logger logger() {
        return org.slf4j.LoggerFactory.getLogger(this.getClass());
    }
}
