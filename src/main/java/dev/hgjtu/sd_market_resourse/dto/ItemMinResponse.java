package dev.hgjtu.sd_market_resourse.dto;

public class ItemMinResponse {
    private Long id;
    private String title;
    private String mainImageUrl;
    private Integer price;

    public ItemMinResponse(Long id, String title, String mainImageUrl, Integer price) {
        this.id = id;
        this.title = title;
        this.mainImageUrl = mainImageUrl;
        this.price = price;
    }
}
