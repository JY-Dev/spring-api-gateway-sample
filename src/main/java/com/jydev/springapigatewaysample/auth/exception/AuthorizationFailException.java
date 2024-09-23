package com.jydev.springapigatewaysample.auth.exception;

public class AuthorizationFailException extends RuntimeException {

    public AuthorizationFailException(String message) {
        super(message);
    }

    public AuthorizationFailException(String message, Throwable cause) {
        super(message, cause);
    }
}
