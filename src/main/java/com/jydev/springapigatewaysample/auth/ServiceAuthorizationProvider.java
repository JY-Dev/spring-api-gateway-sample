package com.jydev.springapigatewaysample.auth;

import com.jydev.springapigatewaysample.config.AllowedServiceType;

public abstract class ServiceAuthorizationProvider {

    private final AllowedServiceType service;
    private final static String DEFAULT_AUTH_PATH = "/auth/verify";

    protected ServiceAuthorizationProvider(AllowedServiceType service) {
        this.service = service;
    }

    abstract public boolean authorization(String authorizationHeader);

    public boolean support(AllowedServiceType service) {
        return this.service == service;
    }

    protected String authorizationPath() {
        return DEFAULT_AUTH_PATH;
    }

    protected String authorizationUrl() {
        return service.getBaseUrl()+authorizationPath();
    }

}
