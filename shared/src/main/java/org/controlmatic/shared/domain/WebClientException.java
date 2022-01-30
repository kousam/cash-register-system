package org.controlmatic.shared.domain;

import java.util.Optional;

/**
 * Class for representing {@link org.controlmatic.shared.util.WebClient} exceptions.
 */
public class WebClientException extends Exception {
    private final Integer statusCode;

    public WebClientException(int statusCode) {
        super("Received non-success status code " + statusCode);
        this.statusCode = statusCode;
    }

    public WebClientException(String message) {
        super(message);
        this.statusCode = null;
    }

    public WebClientException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = null;
    }

    public Optional<Integer> getStatusCode() {
        return Optional.ofNullable(statusCode);
    }
}
