package com.jydev.springapigatewaysample.auth.exception;

public class UnavailableServiceException extends RuntimeException {

    public UnavailableServiceException(String message) {
        super(message);
    }

    public UnavailableServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
