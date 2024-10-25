package com.lamashkevich.aggregationservice.client.shatem.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TokenResource {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private LocalDateTime expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    public void setExpiresIn(Long time) {
        this.expiresIn = LocalDateTime.now().plusSeconds(time).minusMinutes(1);
    }
}
