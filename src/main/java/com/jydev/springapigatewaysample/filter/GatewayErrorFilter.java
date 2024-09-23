package com.jydev.springapigatewaysample.filter;

import com.jydev.springapigatewaysample.auth.exception.AuthorizationFailException;
import com.jydev.springapigatewaysample.auth.exception.UnavailableServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GatewayErrorFilter implements Ordered, GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(throwable -> handleException(exchange, throwable));
    }

    private Mono<Void> handleException(ServerWebExchange exchange, Throwable throwable) {

        log.error(throwable.getMessage(), throwable);

        if (throwable instanceof AuthorizationFailException) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        } else if (throwable instanceof UnavailableServiceException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        } else {
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return exchange.getResponse().setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
