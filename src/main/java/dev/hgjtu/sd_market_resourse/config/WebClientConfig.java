package dev.hgjtu.sd_market_resourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(ReactiveClientRegistrationRepository clientRegistrations,
                               ServerOAuth2AuthorizedClientRepository authorizedClients) {

        ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Filter =
                new ServerOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrations, authorizedClients);

        oauth2Filter.setDefaultOAuth2AuthorizedClient(true);

        return WebClient.builder()
                .filter(oauth2Filter)
                .build();
    }
}
