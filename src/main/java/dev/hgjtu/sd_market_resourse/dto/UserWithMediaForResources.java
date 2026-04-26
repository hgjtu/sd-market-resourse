package dev.hgjtu.sd_market_resourse.dto;


public class UserWithMediaForResources {
    private Long id;
    private String username;
    private String profileMediaUrl;

    public UserWithMediaForResources() {
    }

    public UserWithMediaForResources(Long id, String username, String profileMediaUrl) {
        this.id = id;
        this.username = username;
        this.profileMediaUrl = profileMediaUrl;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getProfileMediaUrl() {
        return profileMediaUrl;
    }
}
