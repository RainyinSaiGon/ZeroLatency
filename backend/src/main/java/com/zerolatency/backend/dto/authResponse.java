package com.zerolatency.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class authResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long expiresAt;
    private UserDTO user;

    public authResponse(String token, Long expiresAt, UserDTO user) {
        this.token = token;
        this.tokenType = "Bearer";
        this.expiresAt = expiresAt;
        this.user = user;
    }
}
