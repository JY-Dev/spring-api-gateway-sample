package com.jydev.springapigatewaysample.filter;

import com.jydev.springapigatewaysample.auth.ServiceAuthorizationProvider;
import com.jydev.springapigatewaysample.auth.exception.AuthorizationFailException;
import com.jydev.springapigatewaysample.auth.exception.UnavailableServiceException;
import com.jydev.springapigatewaysample.config.AllowedServiceType;
import com.jydev.springapigatewaysample.config.RoutePathConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@Slf4j
@RequiredArgsConstructor
public class AuthorizationGatewayFilter implements GatewayFilter {

    private final List<ServiceAuthorizationProvider> authorizationProviders;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String authorizationHeader = exchange.getRequest()
                .getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        Predicate<Map.Entry<String, String>> findServiceId = (entry) -> RoutePathConstants.SERVICE_ID.equals(entry.getKey());
        var service = ServerWebExchangeUtils.getUriTemplateVariables(exchange)
                .entrySet()
                .stream()
                .filter(findServiceId)
                .map(e -> AllowedServiceType.find(e.getValue()))
                .findAny()
                .orElseThrow(() -> new UnavailableServiceException("존재 하지 않는 ServiceId 입니다"));

        Predicate<ServiceAuthorizationProvider> supportService = provider -> provider.support(service);
        var authorizationProvider = authorizationProviders.stream()
                .filter(supportService)
                .findAny()
                .orElseThrow(() -> new UnavailableServiceException("현재 지원 하지 않는 ServiceId 입니다"));

        var authorization = authorizationProvider.authorization(authorizationHeader);
        if(!authorization) {
            throw new AuthorizationFailException("Authorization Fail");
        }

        return chain.filter(exchange);
    }
}
