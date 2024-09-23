package com.jydev.springapigatewaysample.config;

import com.jydev.springapigatewaysample.auth.ServiceAuthorizationProvider;
import com.jydev.springapigatewaysample.auth.exception.AuthorizationFailException;
import com.jydev.springapigatewaysample.auth.exception.UnavailableServiceException;
import com.jydev.springapigatewaysample.filter.AuthorizationGatewayFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
public class MediaFileRouteConfig {

    @Bean
    public RouteLocator mediaFileRouteLocator(
            RouteLocatorBuilder builder,
            AuthorizationGatewayFilter mediaFileAuthorizationGatewayFilter
    ) {
        return builder.routes()
                .route(RouteIdConstants.MEDIA_FILE, r -> r.path("/{" + RoutePathConstants.SERVICE_ID + "}" + "/media-file/**")
                        .filters(f -> f.filter(mediaFileAuthorizationGatewayFilter))
                        .uri(RouteUrlConstants.TEST)
                ).build();
    }

    @Bean
    public AuthorizationGatewayFilter mediaFileAuthorizationGatewayFilter(List<ServiceAuthorizationProvider> providers) {
        return new AuthorizationGatewayFilter(providers);
    }
}
