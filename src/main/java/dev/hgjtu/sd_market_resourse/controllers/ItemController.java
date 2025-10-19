package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
import dev.hgjtu.sd_market_resourse.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/items")
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

//    @GetMapping("/all-by-category/{categoryId}")
//    public Flux<ItemMinResponse> allByCategoryId(@PathVariable Integer categoryId) {
//        return itemService.getAllByCategoryId(categoryId);
//    }

    @PostMapping("/add")
    public Mono<ItemResponse> addItem(){
        return itemService.addItem();
    }

}
