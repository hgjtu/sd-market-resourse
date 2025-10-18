package dev.hgjtu.sd_market_resourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .filter(addAuthHeaderFilter())
                .build();
    }

    private ExchangeFilterFunction addAuthHeaderFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest ->
                ReactiveSecurityContextHolder.getContext()
                        .map(SecurityContext::getAuthentication)
                        .filter(auth -> auth instanceof JwtAuthenticationToken)
                        .cast(JwtAuthenticationToken.class)
                        .map(jwtAuth -> {
                            String tokenValue = jwtAuth.getToken().getTokenValue();

                            return ClientRequest.from(clientRequest)
                                    .header("Authorization", "Bearer " + tokenValue)
                                    .build();
                        })
                        .defaultIfEmpty(clientRequest)
        );
    }

}
