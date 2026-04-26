package dev.hgjtu.sd_market_resourse.feignClients;

import dev.hgjtu.sd_market_resourse.config.ReactiveFeignClientConfiguration;
import dev.hgjtu.sd_market_resourse.dto.UserWithMediaForResources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(
        name = "sd-users-resource",
        configuration = ReactiveFeignClientConfiguration.class
)
public interface UserClient {

    @GetMapping("/api/users-resource/users/exists-by-username/{username}")
    Mono<Long> checkUserExistenceByUsername(@PathVariable("username") String username);

    @GetMapping("/api/users-resource/users/get-username/{id}")
    Mono<String> getUsernameById(@PathVariable("id") Long id);

    @GetMapping("/api/users-resource/users/user-data-with-media/userId/{userId}")
    Mono<UserWithMediaForResources> getUserDataWithMedia(@PathVariable("userId") Long userId);
}