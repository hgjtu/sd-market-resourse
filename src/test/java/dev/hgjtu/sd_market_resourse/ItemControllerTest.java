//package dev.hgjtu.sd_market_resourse;
//
//import dev.hgjtu.sd_market_resourse.controllers.ItemController;
//import dev.hgjtu.sd_market_resourse.dto.ItemMinResponse;
//import dev.hgjtu.sd_market_resourse.dto.ItemRequest;
//import dev.hgjtu.sd_market_resourse.dto.ItemResponse;
//import dev.hgjtu.sd_market_resourse.services.ItemService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.test.StepVerifier;
//
//import org.mockito.Mockito;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ItemControllerTest {
//
//    private ItemService itemService;
//    private ItemController itemController;
//
//    @BeforeEach
//    void setUp() {
//        itemService = mock(ItemService.class);
//        itemController = new ItemController(itemService);
//    }
//
//    @Test
//    void testAllByCategory() {
//        ItemMinResponse item = new ItemMinResponse(
//                1L, "Helmet", "img.jpg", 10000
//        );
//
//        when(itemService.getAllByCategoryAndType("helmet", "sell"))
//                .thenReturn(Flux.just(item));
//
//        Flux<ItemMinResponse> result = itemController.allByCategory("helmet", "sell");
//
//        StepVerifier.create(result)
//                .expectNext(item)
//                .verifyComplete();
//
//        verify(itemService, times(1))
//                .getAllByCategoryAndType("helmet", "sell");
//    }
//
//    @Test
//    void testAllByUsername() {
//        ItemMinResponse item = new ItemMinResponse(
//                2L, "altimeter", "img2.jpg", 500
//        );
//
//        when(itemService.getAllByUsername("admin"))
//                .thenReturn(Flux.just(item));
//
//        Flux<ItemMinResponse> result = itemController.allByUsername("admin");
//
//        StepVerifier.create(result)
//                .expectNext(item)
//                .verifyComplete();
//
//        verify(itemService, times(1))
//                .getAllByUsername("admin");
//    }
//
//    @Test
//    void testGetItemByIdFound() {
//        ItemResponse response = new ItemResponse(
//                1L, 2, "Title", "Desc", "admin",
//                List.of("img.jpg"), 1000, "loc",
//                LocalDate.now(), "buy", List.of()
//        );
//
//        when(itemService.getItemById(1L)).thenReturn(Mono.just(response));
//
//        Mono<?> result = itemController.getItemById(1L);
//
//        StepVerifier.create(result)
//                .expectNextMatches(resp ->
//                        ((ItemResponse) ((org.springframework.http.ResponseEntity<?>) resp).getBody())
//                                .getId().equals(1L)
//                )
//                .verifyComplete();
//
//        verify(itemService, times(1)).getItemById(1L);
//    }
//
//    @Test
//    void testGetItemByIdNotFound() {
//        when(itemService.getItemById(999L)).thenReturn(Mono.empty());
//
//        Mono<?> result = itemController.getItemById(999L);
//
//        StepVerifier.create(result)
//                .expectNextMatches(entity ->
//                        entity instanceof org.springframework.http.ResponseEntity<?> &&
//                                ((org.springframework.http.ResponseEntity<?>) entity).getStatusCode().value() == 404
//                )
//                .verifyComplete();
//
//        verify(itemService, times(1)).getItemById(999L);
//    }
//
//    // ====================================
//    // POST /add
//    // ====================================
//    @Test
//    void testAddItem() {
//        ItemRequest req = Mockito.mock(ItemRequest.class);
//
//        ItemResponse res = new ItemResponse(
//                1L, 2, "New", "Desc", "testUser",
//                List.of(), 12345, "loc", LocalDate.now(), "buy", List.of()
//        );
//
//        when(itemService.addItem(eq("testUser"), any(ItemRequest.class)))
//                .thenReturn(Mono.just(res));
//
//        Mono<?> result = itemController.addItem(req, mockJwt("testUser"));
//
//        StepVerifier.create(result)
//                .expectNextMatches(entity ->
//                        ((ItemResponse) ((org.springframework.http.ResponseEntity<?>) entity).getBody())
//                                .getTitle().equals("New")
//                )
//                .verifyComplete();
//
//        verify(itemService, times(1))
//                .addItem(eq("testUser"), any(ItemRequest.class));
//    }
//
//    @Test
//    void testEditItem() {
//        ItemRequest req = Mockito.mock(ItemRequest.class);
//
//        ItemResponse updated = new ItemResponse(
//                1L, 2, "Updated", "Desc", "testUser",
//                List.of(), 150, "loc", LocalDate.now(), "sell", List.of()
//        );
//
//        when(itemService.editItem(eq(1L), eq("testUser"), any()))
//                .thenReturn(Mono.just(updated));
//
//        Mono<?> result = itemController.editItem(1L, req, mockJwt("testUser"));
//
//        StepVerifier.create(result)
//                .expectNextMatches(entity ->
//                        ((ItemResponse) ((org.springframework.http.ResponseEntity<?>) entity).getBody())
//                                .getTitle().equals("Updated")
//                )
//                .verifyComplete();
//
//        verify(itemService, times(1))
//                .editItem(eq(1L), eq("testUser"), any());
//    }
//
//    @Test
//    void testDeleteItem() {
//        when(itemService.deleteItem(1L, "testUser"))
//                .thenReturn(Mono.just("Success"));
//
//        Mono<?> result = itemController.deleteItem(1L, mockJwt("testUser"));
//
//        StepVerifier.create(result)
//                .expectNextMatches(entity ->
//                        ((String) ((org.springframework.http.ResponseEntity<?>) entity).getBody())
//                                .equals("Success")
//                )
//                .verifyComplete();
//
//        verify(itemService, times(1))
//                .deleteItem(1L, "testUser");
//    }
//
//    private org.springframework.security.oauth2.jwt.Jwt mockJwt(String username) {
//        return org.springframework.security.oauth2.jwt.Jwt.withTokenValue("token")
//                .header("alg", "none")
//                .claim("sub", username)
//                .build();
//    }
//}
//
//
