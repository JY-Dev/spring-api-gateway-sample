package com.jydev.springapigatewaysample.auth;

import com.jydev.springapigatewaysample.auth.exception.AuthorizationFailException;
import com.jydev.springapigatewaysample.config.AllowedServiceType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class TestServiceAuthorizationProvider extends ServiceAuthorizationProvider {

    public TestServiceAuthorizationProvider() {
        super(AllowedServiceType.TEST);
    }

    @Override
    public boolean authorization(String authorizationHeader) {

        try {
            var statusCode = RestClient.create(authorizationUrl())
                    .get()
                    .header(HttpHeaders.AUTHORIZATION, authorizationHeader)
                    .retrieve()
                    .toEntity(Void.class)
                    .getStatusCode();
            return statusCode.is2xxSuccessful();
        } catch (Exception e) {
            throw new AuthorizationFailException(e.getMessage());
        }
    }
}
