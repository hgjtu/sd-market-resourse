package dev.hgjtu.sd_market_resourse.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Controller
//@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {
    private final WebClient webClient;

    @Autowired
    public ItemController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/api/from-service-b")
    public Mono<String> callServiceB(@AuthenticationPrincipal Jwt jwt) {
        System.out.println("✅ Current user token: " + jwt.getTokenValue().substring(0, 20) + "...");

        return webClient
                .get()
                .uri("http://localhost:7070/api/username/hgjtu")
                .retrieve()
                .bodyToMono(String.class);
    }

}
