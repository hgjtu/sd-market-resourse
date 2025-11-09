package dev.hgjtu.sd_market_resourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import reactivefeign.client.ReactiveHttpRequestInterceptor;
import reactivefeign.spring.config.EnableReactiveFeignClients;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableReactiveFeignClients
public class ReactiveFeignClientConfiguration {

    @Bean
    public ReactiveHttpRequestInterceptor reactiveHttpRequestInterceptor() {
        return request -> ReactiveSecurityContextHolder.getContext()
                .map(ctx -> {
                    var auth = ctx.getAuthentication();

                    if (auth instanceof JwtAuthenticationToken jwtAuth) {
                        request.headers().put("Authorization",
                                List.of("Bearer " + jwtAuth.getToken().getTokenValue()));
                    } else if (auth instanceof BearerTokenAuthentication bearerAuth) {
                        request.headers().put("Authorization",
                                List.of("Bearer " + bearerAuth.getToken().getTokenValue()));
                    }

                    return request;
                })
                .switchIfEmpty(Mono.just(request));
    }
}
