package com.jydev.springapigatewaysample.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AllowedServiceType {
    TEST("http://localhost:8081/test"),
    ;

    private final String baseUrl;

    public static AllowedServiceType find(String service) {
        return Arrays.stream(values())
                .filter(v -> v.name().equalsIgnoreCase(service))
                .findAny()
                .orElse(null);
    }
}
