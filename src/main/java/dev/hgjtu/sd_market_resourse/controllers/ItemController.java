package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemRequest;
import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
import dev.hgjtu.sd_market_resourse.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/market-resource/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all-by-category/{category}/{type}")
    public Flux<ItemMinResponse> allByCategory(@PathVariable String category,
                                               @PathVariable String type) {
        return itemService.getAllByCategoryAndType(category, type);
    }

    @GetMapping("/all-by-username/{username}/{type}")
    public Flux<ItemMinResponse> allByUsername(@PathVariable String username,
                                               @PathVariable String type) {
        return itemService.getAllByUsernameAndType(username, type);
    }

//    @GetMapping("/all-by-category/{categoryId}")
//    public Flux<ItemMinResponse> allByCategoryId(@PathVariable Integer categoryId) {
//        return itemService.getAllByCategoryId(categoryId);
//    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ItemResponse>> getItemById(@PathVariable Long id){
        return itemService.getItemById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public Mono<ResponseEntity<Long>> addItem(@RequestBody ItemRequest itemRequest,
                                                      @AuthenticationPrincipal Jwt jwt){
        return itemService.addItem(jwt.getClaim("sub"), itemRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/add-media/{itemId}")
    public Mono<Void> addItemMedia(@PathVariable Long itemId,
                                                   @RequestBody List<UUID> mediaIds,
                                                   @AuthenticationPrincipal Jwt jwt){
        return itemService.addItemMedia(itemId, jwt.getClaim("sub"),  mediaIds);
    }

    @PatchMapping("/edit/{itemId}")
    public Mono<ResponseEntity<Long>> editItem(@PathVariable Long itemId,
                                                       @RequestBody ItemRequest itemRequest,
                                                       @AuthenticationPrincipal Jwt jwt){
        return itemService.editItem(itemId, jwt.getClaim("sub"), itemRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{itemId}")
    public Mono<ResponseEntity<String>> deleteItem(@PathVariable Long itemId,
                                                   @AuthenticationPrincipal Jwt jwt){
        return itemService.deleteItem(itemId, jwt.getClaim("sub"))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete-media/{itemId}/{mediaId}")
    public Mono<ResponseEntity<Void>> deleteItemMedia(@PathVariable Long itemId,
                                                      @PathVariable UUID mediaId,
                                                      @AuthenticationPrincipal Jwt jwt){
        return itemService.deleteItemMedia(itemId, jwt.getClaim("sub"),  mediaId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

//    @PatchMapping("/editJump/{id}")
//    public Mono<ResponseEntity<UserResponse>> editJump(@PathVariable Long id,
//                                                       @RequestBody JumpRequest jumpRequest,
//                                                       @AuthenticationPrincipal Jwt jwt){
//        return jumpService.editJump(jwt.getClaim("sub"), id, jumpRequest)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/deleteJump/{id}")
//    public Mono<ResponseEntity<UserResponse>> deleteJump(@PathVariable Long id,
//                                                         @AuthenticationPrincipal Jwt jwt){
//        return jumpService.deleteJump(jwt.getClaim("sub"), id)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
}
