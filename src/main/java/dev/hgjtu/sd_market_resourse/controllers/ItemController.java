package dev.hgjtu.sd_market_resourse.controllers;

import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
import dev.hgjtu.sd_market_resourse.services.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Flux;

@Controller
//@RequiredArgsConstructor
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/all-by-category/{category}")
    public Flux<ItemMinResponse> allByCategory(@PathVariable String category) {
        return itemService.getAllByCategory(category);
    }

}
